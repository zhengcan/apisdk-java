package com.github.zhengcan.apisdk;

import java.lang.reflect.Method;
import java.util.concurrent.CompletionStage;

public class DefaultApiContext<T> implements ApiContext<T> {
  private final T api;
  private final Method apiMethod;

  public DefaultApiContext(T api, Method apiMethod) {
    this.api = api;
    this.apiMethod = apiMethod;
  }

  @Override
  public T getApi() {
    return null;
  }

  @Override
  public Method getApiMethod() {
    return this.apiMethod;
  }

  @Override
  public String getHttpMethod() {
    return null;
  }

  @Override
  public String getRawPath() {
    return null;
  }

  @Override
  public String getHttpUrl() {
    return null;
  }

  @Override
  public <R> R execute() {
    return null;
  }

  @Override
  public <R> CompletionStage<R> executeAsync() {
    return null;
  }

  public void setHttpMethod(String httpMethod) {
  }

  public void setRawPath(String rawPath) {
  }

  public void setPath(String path) {
  }
}
