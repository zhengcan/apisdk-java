package com.github.zhengcan.apisdk.build;

import com.github.zhengcan.apisdk.BuildException;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

public interface ClassGenerator {
  String RUNTIME_VAR = "__runtime";

  String getClassName();

  <T> void addInterface(Class<? extends T> apiClass) throws BuildException;

  void addStaticField(Type fieldType, String fieldName) throws BuildException;

  <T> Class<? extends T> generate() throws BuildException;

  MethodBody addMethod(Method apiMethod) throws BuildException;
}
