package com.github.zhengcan.apisdk;

import java.lang.reflect.Method;
import java.util.concurrent.CompletionStage;

public interface ApiContext<T> {
  T getApi();
  Method getApiMethod();
  String getHttpMethod();
  String getRawPath();
  String getHttpUrl();
  <R> R execute();
  <R> CompletionStage<R> executeAsync();
}
