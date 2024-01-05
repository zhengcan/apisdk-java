package com.github.zhengcan.apisdk.aop;

import java.lang.annotation.Annotation;

public interface InterceptorFactory {
  <T> Interceptor<T> create(Class<T> apiClass, Annotation annotation);
}
