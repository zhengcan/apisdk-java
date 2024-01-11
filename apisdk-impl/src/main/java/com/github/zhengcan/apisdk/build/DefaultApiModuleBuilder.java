package com.github.zhengcan.apisdk.build;

import com.github.zhengcan.apisdk.ApiSession;
import com.github.zhengcan.apisdk.BuildException;
import com.github.zhengcan.apisdk.DefaultApiContext;
import com.github.zhengcan.apisdk.Defaults;
import com.github.zhengcan.apisdk.MimeTypes;
import com.github.zhengcan.apisdk.ahc.HttpRequestImpl;
import com.github.zhengcan.apisdk.aop.Interceptor;
import com.github.zhengcan.apisdk.aop.InterceptorChain;
import com.github.zhengcan.apisdk.http.HttpResponse;
import com.github.zhengcan.apisdk.request.JsonRequest;
import com.github.zhengcan.apisdk.request.Param;
import com.github.zhengcan.apisdk.request.PathParam;
import com.github.zhengcan.apisdk.response.HttpExtractor;
import com.github.zhengcan.apisdk.utils.ClassUtils;
import com.github.zhengcan.apisdk.utils.UrlUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.slf4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class DefaultApiModuleBuilder<T, M> extends AbstractBuilder<T> implements ApiModuleBuilder<T, M> {
  private final Builder<T> parent;
  private final Class<? extends M> moduleClass;
  private final String path;
  private final Map<Method, ApiMethodBuilder<T>> apiMethods;
  private final List<Method> unknownMethods;

  public DefaultApiModuleBuilder(Builder<T> parent, Class<? extends M> moduleClass, String path, Logger apiLogger) {
    super(parent.getApiClass(), apiLogger);
    this.parent = parent;
    this.moduleClass = moduleClass;
    this.path = path;
    this.apiMethods = new HashMap<>();
    this.unknownMethods = new ArrayList<>();
  }

  @Override
  public Class<? extends T> getApiClass() {
    return this.parent.getApiClass();
  }

  @Override
  public String getBaseUrl() {
    if (this.path == null || this.path.equals(Defaults.NO_VALUE)) {
      return this.parent.getBaseUrl();
    } else {
      return UrlUtils.join(this.parent.getBaseUrl(), this.path);
    }
  }

  @Override
  public ApiModuleBuilder<T, ?> findModule(Method method) {
    return this.parent.findModule(method);
  }

  @Override
  public Stream<Interceptor<?>> getInterceptors() {
    return Stream.concat(this.parent.getInterceptors(), this.interceptors.stream());
  }

  public void addApiMethod(Method apiMethod, String method, String path) throws BuildException {
    DefaultApiMethodBuilder<T> methodBuilder
      = new DefaultApiMethodBuilder<>(this, apiMethod, method, path, this.apiLogger);
    methodBuilder.processAnnotations(apiMethod.getAnnotations());
    this.apiMethods.put(apiMethod, methodBuilder);
  }

  public void addUnknownMethod(Method method) {
    this.unknownMethods.add(method);
    System.out.println("Unknown method: " + method);
  }

  public M build() {
    @SuppressWarnings("unchecked")
    M module = (M) Proxy.newProxyInstance(this.moduleClass.getClassLoader(), new Class[]{this.moduleClass}, new InvocationHandler() {
      @Override
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        DefaultApiModuleBuilder<T, M> self = DefaultApiModuleBuilder.this;
        ApiModuleBuilder<T, ?> moduleBuilder = self.parent.findModule(method);
        if (moduleBuilder != null && moduleBuilder != self) {
          return moduleBuilder.build();
        }

        if (method.getDeclaringClass().equals(ApiSession.class)) {
          // ?X
          return proxy;
        }

        ApiMethodBuilder<T> methodBuilder = self.apiMethods.get(method);
        if (methodBuilder == null) {
          return null;
        }

        String path = methodBuilder.getPath();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < method.getParameterCount(); i++) {
          for (Annotation annotation : parameterAnnotations[i]) {
            if (annotation.annotationType().equals(PathParam.class)) {
              PathParam pathParam = (PathParam) annotation;
              path = path.replace("{" + pathParam.name() + "}", String.valueOf(args[i]));
            }
          }
        }

        DefaultAsyncHttpClient httpClient = new DefaultAsyncHttpClient();
        String fullPath = UrlUtils.join(self.getBaseUrl(), path);
        HttpRequestImpl request = new HttpRequestImpl(
          httpClient.prepare(methodBuilder.getHttpMethod(), fullPath)
        );

        Annotation reqBuilderAnno = methodBuilder.getRequestBuilder();
        if (reqBuilderAnno instanceof JsonRequest) {
          JsonObject payload = new JsonObject();
          for (int i = 0; i < method.getParameterCount(); i++) {
            for (Annotation annotation : parameterAnnotations[i]) {
              if (annotation.annotationType().equals(Param.class)) {
                Param param = (Param) annotation;
                payload.add(param.name(), new Gson().toJsonTree(args[i]));
                break;
              }
            }
          }
          request.setContentType(MimeTypes.APPLICATION_JSON);
          request.setBody(payload.toString());
        }

        InterceptorChain chain = methodBuilder.buildInterceptorChain(method);
        HttpResponse response = chain.process(request, new DefaultApiContext<>(this, method));

        HttpExtractor httpExtractor = methodBuilder.buildHttpExtractor();
        return httpExtractor.extract(response, method.getGenericReturnType());
      }
    });
    return module;
  }

  @Override
  public void genClass(ClassGenerator cg) throws BuildException {
    // 定义方法
    for (Map.Entry<Method, ApiMethodBuilder<T>> entry: this.apiMethods.entrySet()) {
      ApiMethodBuilder<T> methodBuilder = entry.getValue();

      // 添加字段
      cg.addStaticField(Method.class, methodBuilder.getMethodId());

      // 创建方法
      methodBuilder.genMethod(cg).freeze();
    }
  }

  @Override
  public void initClass(Class<?> implClass) throws BuildException {
    try {
      for (Map.Entry<Method, ApiMethodBuilder<T>> entry: this.apiMethods.entrySet()) {
        Field methodField = implClass.getField(entry.getValue().getMethodId());
        methodField.setAccessible(true);
        methodField.set(null, entry.getKey());
      }
    } catch (Exception e) {
      throw new BuildException(e);
    }
  }

  @Override
  public Class<? extends M> buildClass(CtClass ctClass) throws NotFoundException, CannotCompileException {
    for (Method method : this.moduleClass.getMethods()) {
      System.out.println(method);
      CtMethod ctMethod = ClassUtils.findCtMethod(ctClass, method);
      System.out.println(ctMethod);
      if (method.getDeclaringClass().equals(Object.class)) {
        System.out.println("\t in Object");
        this.makeObjectMethod(ctClass, ctMethod, method);
      } else if (method.getDeclaringClass().equals(ApiSession.class)) {
        System.out.println("\t in ApiSession");
        this.makeSessionMethod(ctClass, ctMethod, method);
      } else {
        ApiMethodBuilder<T> methodBuilder = this.apiMethods.get(method);
        if (methodBuilder == null) {
          this.makeOtherMethod(ctClass, ctMethod, method);
        } else {
          methodBuilder.buildMethod(ctClass, ctMethod);
        }
      }
    }

    @SuppressWarnings("unchecked")
    Class<? extends M> clazz = ctClass.getClassPool().toClass(ctClass);
    return clazz;
  }

  private void makeObjectMethod(CtClass ctClass, CtMethod ctMethod, Method method) throws CannotCompileException {
    CtMethod newMethod = CtNewMethod.copy(ctMethod, ctClass, null);
    newMethod.setBody("throw new RuntimeException(\"not implemented\");");
    ctClass.addMethod(newMethod);
  }

  private void makeSessionMethod(CtClass ctClass, CtMethod ctMethod, Method method) throws CannotCompileException {
    CtMethod newMethod = CtNewMethod.copy(ctMethod, ctClass, null);
    newMethod.setBody("throw new RuntimeException(\"not implemented\");");
    ctClass.addMethod(newMethod);
  }

  private void makeOtherMethod(CtClass ctClass, CtMethod ctMethod, Method method) throws CannotCompileException {
    CtMethod newMethod = CtNewMethod.copy(ctMethod, ctClass, null);
    newMethod.setBody("throw new RuntimeException(\"not implemented\");");
    ctClass.addMethod(newMethod);
  }
}
