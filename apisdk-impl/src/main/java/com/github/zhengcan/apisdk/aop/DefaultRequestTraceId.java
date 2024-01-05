package com.github.zhengcan.apisdk.aop;

import com.github.zhengcan.apisdk.ApiContext;
import com.github.zhengcan.apisdk.ApiRuntime;
import com.github.zhengcan.apisdk.http.HttpRequest;
import com.github.zhengcan.apisdk.http.HttpResponse;

public class DefaultRequestTraceId<T> implements Interceptor<T> {
  private final RequestTraceId annotation;

  public DefaultRequestTraceId(RequestTraceId annotation) {
    this.annotation = annotation;
  }

  @Override
  public boolean init(Class<? extends T> apiClass, ApiRuntime runtime) {
    return true;
  }

  @Override
  public HttpResponse process(HttpRequest request, ApiContext context, InterceptorChain next) {
    return next.process(request, context);
  }
}
