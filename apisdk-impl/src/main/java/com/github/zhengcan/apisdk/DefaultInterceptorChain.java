package com.github.zhengcan.apisdk;

import com.github.zhengcan.apisdk.ahc.HttpRequestImpl;
import com.github.zhengcan.apisdk.aop.Interceptor;
import com.github.zhengcan.apisdk.aop.InterceptorChain;
import com.github.zhengcan.apisdk.http.HttpRequest;
import com.github.zhengcan.apisdk.http.HttpResponse;

import java.util.Iterator;
import java.util.List;

public class DefaultInterceptorChain implements InterceptorChain {
  private final Iterator<Interceptor<?>> iterator;

  public DefaultInterceptorChain(Iterator<Interceptor<?>> iterator) {
    this.iterator = iterator;
  }

  @Override
  public HttpResponse process(HttpRequest request, ApiContext context) {
    System.out.println("process in chain");
    if (this.iterator.hasNext()) {
      Interceptor<?> interceptor = this.iterator.next();
      System.out.println(interceptor);
      return interceptor.process(request, context, this);
    } else {
      System.out.println("empty");
      return ((HttpRequestImpl) request).execute();
    }
  }
}
