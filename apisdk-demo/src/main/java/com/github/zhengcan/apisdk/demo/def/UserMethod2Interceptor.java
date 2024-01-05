package com.github.zhengcan.apisdk.demo.def;

import com.github.zhengcan.apisdk.ApiContext;
import com.github.zhengcan.apisdk.ApiRuntime;
import com.github.zhengcan.apisdk.aop.Interceptor;
import com.github.zhengcan.apisdk.aop.InterceptorChain;
import com.github.zhengcan.apisdk.http.HttpRequest;
import com.github.zhengcan.apisdk.http.HttpResponse;

public class UserMethod2Interceptor implements Interceptor<Object> {
  @Override
  public boolean init(Class<?> apiClass, ApiRuntime runtime) {
    return false;
  }

  @Override
  public HttpResponse process(HttpRequest request, ApiContext<?> context, InterceptorChain next) {
    return next.process(request, context);
  }
}
