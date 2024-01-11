package com.github.zhengcan.apisdk.build;

import com.github.zhengcan.apisdk.BuildException;
import com.github.zhengcan.apisdk.aop.InterceptorChain;
import com.github.zhengcan.apisdk.response.HttpExtractor;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public interface ApiMethodBuilder<T> extends Builder<T> {
  String getMethodId();

  String getHttpMethod();

  String getPath();

  InterceptorChain buildInterceptorChain(Method method);

  HttpExtractor buildHttpExtractor() throws BuildException;

  Annotation getRequestBuilder();

  void buildMethod(CtClass ctClass, CtMethod ctMethod) throws CannotCompileException;

  MethodBody genMethod(ClassGenerator cg) throws BuildException;
}
