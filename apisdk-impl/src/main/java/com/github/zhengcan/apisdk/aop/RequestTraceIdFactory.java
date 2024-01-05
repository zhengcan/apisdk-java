package com.github.zhengcan.apisdk.aop;

import com.github.zhengcan.apisdk.ApiContext;
import com.github.zhengcan.apisdk.ApiRuntime;
import com.github.zhengcan.apisdk.http.HttpRequest;
import com.github.zhengcan.apisdk.http.HttpResponse;

import java.lang.annotation.Annotation;

public class RequestTraceIdFactory implements RequestTraceId.Factory {
  @Override
  public <T> Interceptor<T> create(Class<T> apiClass, Annotation annotation) {
    System.out.println("create @ " + this.getClass());
    return new DefaultRequestTraceId<>((RequestTraceId) annotation);
  }

  @Override
  public boolean init(Class<?> apiClass, ApiRuntime runtime) {
    throw new IllegalStateException();
  }

  @Override
  public HttpResponse process(HttpRequest request, ApiContext context, InterceptorChain next) {
    throw new IllegalStateException();
  }
}
