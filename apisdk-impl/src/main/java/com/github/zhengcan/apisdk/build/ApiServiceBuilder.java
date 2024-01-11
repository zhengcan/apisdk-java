package com.github.zhengcan.apisdk.build;

import com.github.zhengcan.apisdk.BuildException;
import javassist.ClassPool;

import java.lang.reflect.Method;

public interface ApiServiceBuilder<T> extends Builder<T> {
  T build();

  Class<? extends T> buildClass(ClassPool classPool) throws BuildException;

  ApiModuleBuilder<T, ?> findModule(Method method);

  ApiModuleBuilder<T, T> getRootModule();
}
