package com.github.zhengcan.apisdk.build;

import com.github.zhengcan.apisdk.ApiModule;
import com.github.zhengcan.apisdk.ApiRuntime;
import com.github.zhengcan.apisdk.ApiService;
import com.github.zhengcan.apisdk.BuildException;
import com.github.zhengcan.apisdk.Defaults;
import com.github.zhengcan.apisdk.aop.Interceptor;
import com.github.zhengcan.apisdk.request.ApiMethod;
import com.github.zhengcan.apisdk.runtime.DefaultApiRuntime;
import javassist.ClassPool;
import org.slf4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class DefaultApiServiceBuilder<T> implements ApiServiceBuilder<T> {
  private final Class<? extends T> apiClass;
  private final ApiService apiService;
  private final Logger apiLogger;
  private final DefaultApiModuleBuilder<T, T> rootModule;
  private final Map<Method, ApiModuleBuilder<T, ?>> submodules;

  public DefaultApiServiceBuilder(Class<? extends T> apiClass, ApiService apiService, Logger apiLogger) {
    this.apiClass = apiClass;
    this.apiService = apiService;
    this.apiLogger = apiLogger;
    this.rootModule = new DefaultApiModuleBuilder<>(this, apiClass, null, apiLogger);
    this.submodules = new HashMap<>();
  }

  @Override
  public Class<? extends T> getApiClass() {
    return this.apiClass;
  }

  @Override
  public String getBaseUrl() {
    return this.apiService.value();
  }

  public void load() throws BuildException {
    this.initModule(this.apiClass, this.rootModule);
  }

  private <M> void addModule(Method method, Class<? extends M> moduleClass, ApiModule moduleAnno) throws BuildException {
    // Skip root module
    if (moduleClass.equals(this.apiClass)) {
      return;
    }
    // Skip existed module
    if (this.submodules.containsKey(method)) {
      return;
    }

    DefaultApiModuleBuilder<T, M> moduleBuilder = new DefaultApiModuleBuilder<>(this.rootModule, moduleClass, moduleAnno.path(), this.apiLogger);
    this.submodules.put(method, moduleBuilder);
    this.initModule(moduleClass, moduleBuilder);
  }

  private <M> void initModule(Class<? extends M> moduleClass, DefaultApiModuleBuilder<T, M> moduleBuilder) throws BuildException {
    // Annotations
    moduleBuilder.processAnnotations(moduleClass.getAnnotations());

    // Methods
    for (Method method : moduleClass.getDeclaredMethods()) {
      Class<?> returnType = method.getReturnType();

      ApiModule moduleAnno = method.getAnnotation(ApiModule.class);
      if (moduleAnno == null || moduleAnno.path().equals(Defaults.NO_VALUE)) {
        moduleAnno = returnType.getAnnotation(ApiModule.class);
      }
      if (moduleAnno != null) {
        this.addModule(method, returnType, moduleAnno);
        continue;
      }

      String httpMethod = null;
      String path = null;
      for (Annotation annotation : method.getAnnotations()) {
        if (annotation instanceof ApiMethod) {
          httpMethod = ((ApiMethod) annotation).method();
          path = ((ApiMethod) annotation).path();
          break;
        }
        ApiMethod methodAnno = annotation.annotationType().getAnnotation(ApiMethod.class);
        if (methodAnno != null) {
          httpMethod = methodAnno.method();
          try {
            path = (String) annotation.annotationType().getMethod("value").invoke(annotation);
          } catch (Exception e) {
            throw new BuildException(e);
          }
          break;
        }
      }
      if (httpMethod != null) {
        moduleBuilder.addApiMethod(method, httpMethod, path);
        continue;
      }

      moduleBuilder.addUnknownMethod(method);
    }
  }

  @Override
  public T build() {
    return this.rootModule.build();
  }

  @Override
  public Class<? extends T> buildClass(ClassPool classPool) throws BuildException {
    // 生成类定义
    ClassGenerator cg = new DefaultClassGenerator(classPool, this.apiClass.getName() + "$$Impl");
    cg.addInterface(this.apiClass);
    cg.addStaticField(ApiRuntime.class, ClassGenerator.RUNTIME_VAR);
    this.rootModule.genClass(cg);

    // 静态初始化
    Class<? extends T> implClass = cg.generate();

    // 注入 Runtime
    try {
      Field staticRuntimeField = implClass.getField(ClassGenerator.RUNTIME_VAR);
      staticRuntimeField.setAccessible(true);
      staticRuntimeField.set(null, new DefaultApiRuntime());
    } catch (Exception e) {
      throw new BuildException(e);
    }

    // 注入 MethodVar
    this.rootModule.initClass(implClass);

    return implClass;
  }

  @Override
  public ApiModuleBuilder<T, ?> findModule(Method method) {
    ApiModuleBuilder<T, ?> moduleBuilder = this.submodules.get(method);
    if (moduleBuilder == null) {
      if (method.getReturnType().equals(this.apiClass)) {
        return this.rootModule;
      } else {
        return null;
      }
    } else {
      return moduleBuilder;
    }
  }

  @Override
  public ApiModuleBuilder<T, T> getRootModule() {
    return this.rootModule;
  }

  @Override
  public Stream<Interceptor<?>> getInterceptors() {
    return Stream.empty();
  }
}
