package com.github.zhengcan.apisdk.aop;

import com.github.zhengcan.apisdk.ApiContext;
import com.github.zhengcan.apisdk.ApiRuntime;
import com.github.zhengcan.apisdk.http.HttpRequest;
import com.github.zhengcan.apisdk.http.HttpResponse;

public interface Interceptor<T> {
  boolean init(Class<? extends T> apiClass, ApiRuntime runtime);
  HttpResponse process(HttpRequest request, ApiContext<? extends T> context, InterceptorChain next);
}
