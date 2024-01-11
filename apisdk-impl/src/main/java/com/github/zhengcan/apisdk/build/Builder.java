package com.github.zhengcan.apisdk.build;

import com.github.zhengcan.apisdk.aop.Interceptor;

import java.lang.reflect.Method;
import java.util.stream.Stream;

public interface Builder<T> {
  Class<? extends T> getApiClass();

  String getBaseUrl();

  ApiModuleBuilder<T,?> findModule(Method method);

  Stream<Interceptor<?>> getInterceptors();
}
