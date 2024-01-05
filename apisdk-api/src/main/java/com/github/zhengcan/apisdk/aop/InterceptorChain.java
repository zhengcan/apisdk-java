package com.github.zhengcan.apisdk.aop;

import com.github.zhengcan.apisdk.ApiContext;
import com.github.zhengcan.apisdk.http.HttpRequest;
import com.github.zhengcan.apisdk.http.HttpResponse;

public interface InterceptorChain {
  HttpResponse process(HttpRequest request, ApiContext<?> context);
}
