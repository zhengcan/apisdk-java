package com.github.zhengcan.apisdk.build;

import com.github.zhengcan.apisdk.BuildException;
import com.github.zhengcan.apisdk.DefaultApiContext;
import com.github.zhengcan.apisdk.DefaultInterceptorChain;
import com.github.zhengcan.apisdk.aop.Interceptor;
import com.github.zhengcan.apisdk.aop.InterceptorChain;
import com.github.zhengcan.apisdk.request.PathParam;
import com.github.zhengcan.apisdk.response.DefaultHttpExtractor;
import com.github.zhengcan.apisdk.response.HttpExtractor;
import com.github.zhengcan.apisdk.utils.ObjectUtils;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.stream.Stream;

public class DefaultApiMethodBuilder<T> extends AbstractBuilder<T> implements ApiMethodBuilder<T> {
  private final ApiModuleBuilder<T, ?> parent;
  private final Method apiMethod;
  private final String apiMethodId;
  private final String httpMethod;
  private final String path;

  public DefaultApiMethodBuilder(ApiModuleBuilder<T, ?> parent, Method apiMethod, String httpMethod, String path, Logger apiLogger) {
    super(parent.getApiClass(), apiLogger);
    this.parent = parent;
    this.apiMethod = apiMethod;
    this.apiMethodId = "__" + this.apiMethod.getName() + "_" + DigestUtils.md5Hex(apiMethod.toGenericString());
    this.httpMethod = httpMethod;
    this.path = path;
  }

  @Override
  public Class<? extends T> getApiClass() {
    return this.parent.getApiClass();
  }

  @Override
  public String getBaseUrl() {
    return this.parent.getBaseUrl();
  }

  @Override
  public ApiModuleBuilder<T, ?> findModule(Method method) {
    return this.parent.findModule(method);
  }

  @Override
  public Stream<Interceptor<?>> getInterceptors() {
    return Stream.concat(
      this.parent.getInterceptors(),
      this.interceptors.stream()
    );
  }

  @Override
  public String getMethodId() {
    return this.apiMethodId;
  }

  @Override
  public String getHttpMethod() {
    return this.httpMethod;
  }

  @Override
  public String getPath() {
    return this.path;
  }

  @Override
  public InterceptorChain buildInterceptorChain(Method method) {
    return new DefaultInterceptorChain(this.getInterceptors().iterator());
  }

  @Override
  public HttpExtractor buildHttpExtractor() throws BuildException {
    HttpExtractor httpExtractor;
    if (this.httpExtract == null) {
      httpExtractor = new DefaultHttpExtractor();
    } else {
      httpExtractor = ObjectUtils.createInstance(this.httpExtract.using());
    }

    httpExtractor.init(this.getApiClass(), this.httpExtract);
    if (this.entityExtract != null) {
      httpExtractor.init(this.getApiClass(), this.entityExtract);
    }

    return httpExtractor;
  }

  @Override
  public Annotation getRequestBuilder() {
    if (this.requestBuilder == null) {
      return this.parent.getRequestBuilder();
    } else {
      return this.requestBuilder;
    }
  }

  @Override
  public void buildMethod(CtClass ctClass, CtMethod ctMethod) throws CannotCompileException {
    CtMethod newMethod = CtNewMethod.copy(ctMethod, ctClass, null);

    // Create ApiContext
    // Add Interceptors
    // Add Headers
    // Add Cookies
    // Add QueryParams
    // Execute
    newMethod.setBody("throw new RuntimeException(\"not implemented\");");

    ctClass.addMethod(newMethod);
  }

  @Override
  public MethodBody genMethod(ClassGenerator cg) throws BuildException {
    MethodBody body = cg.addMethod(this.apiMethod);
    body.add("System.out.println(\"## \" + %s.%s);", cg.getClassName(), ClassGenerator.RUNTIME_VAR);
    body.add("System.out.println(\"## \" + %s.%s);", cg.getClassName(), this.getMethodId());

    // Initial
    body.add(
      "%s context = (%s) %s.%s.createContext(this, %s.class, %s.%s);",
      DefaultApiContext.class.getName(),
      DefaultApiContext.class.getName(),
      cg.getClassName(),
      ClassGenerator.RUNTIME_VAR,
      this.getApiClass().getName(),
      cg.getClassName(),
      this.getMethodId()
    );

    // Http
    body.add("context.setHttpMethod(\"%s\");", body.escape(this.httpMethod));
    body.add("String path = \"%s\";", body.escape(this.path));
    body.add("context.setRawPath(path);");
    if (this.path.contains("{")) {
      Annotation[][] parameterAnnotations = this.apiMethod.getParameterAnnotations();
      for (int i = 0; i < this.apiMethod.getParameterCount(); i++) {
        for (Annotation annotation : parameterAnnotations[i]) {
          if (annotation.annotationType().equals(PathParam.class)) {
            PathParam pathParam = (PathParam) annotation;
            body.add("path = path.replace(\"{%s}\", String.valueOf($%d));", body.escape(pathParam.name()), i + 1);
          }
        }
      }
    }
    body.add("context.setPath(path);");
    body.add("System.out.println(context);");

    // Params
//    this.requestBuilder
//    this.httpExtract
    body.add("System.out.println(context);");

    // Execute

    body.add("throw new RuntimeException();");
    return body;
  }
}
