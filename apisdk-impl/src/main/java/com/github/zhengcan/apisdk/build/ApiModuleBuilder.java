package com.github.zhengcan.apisdk.build;

import com.github.zhengcan.apisdk.BuildException;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;

import java.lang.annotation.Annotation;

public interface ApiModuleBuilder<T, M> extends Builder<T> {
  M build();

  void genClass(ClassGenerator cb) throws BuildException;

  void initClass(Class<?> implClass) throws BuildException;

  Annotation getRequestBuilder();

  Class<? extends M> buildClass(CtClass ctClass) throws NotFoundException, CannotCompileException;
}
