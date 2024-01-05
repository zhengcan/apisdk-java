package com.github.zhengcan.apisdk.aop;

import com.github.zhengcan.apisdk.ApiContext;
import com.github.zhengcan.apisdk.ApiRuntime;
import com.github.zhengcan.apisdk.http.HttpRequest;
import com.github.zhengcan.apisdk.http.HttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;

public abstract class AbstractTokenBasedAuth<T> implements Authenticator<T> {
  private final TokenCarrier carrier;
  private final String headerName;
  private final String queryParamName;

  public AbstractTokenBasedAuth(TokenCarrier carrier, String headerName, String queryParamName) {
    this.carrier = carrier;
    this.headerName = headerName;
    this.queryParamName = queryParamName;
  }

  @Override
  public boolean init(Class<? extends T> apiClass, ApiRuntime runtime) {
    return true;
  }

  @Override
  public HttpResponse process(HttpRequest request, ApiContext context, InterceptorChain next) {
    Authenticate annotation = context.getApiMethod().getAnnotation(Authenticate.class);
    if (annotation == null) {
      return this.authenticate(request, context, next);
    } else if (annotation.value().equals(this.getClass())) {
      if (annotation.enabled()) {
        return this.authenticate(request, context, next);
      } else {
        return next.process(request, context);
      }
    } else {
      return next.process(request, context);
    }
  }

  protected HttpResponse authenticate(HttpRequest request, ApiContext context, InterceptorChain next) {
    String token = this.generateToken(request, context);
    switch (this.carrier) {
      case BEARER_AUTH:
        request.setHeader(HttpHeaderNames.AUTHORIZATION.toString(), "Bearer " + token);
        break;
      case SCHEMALESS_AUTH:
        request.setHeader(HttpHeaderNames.AUTHORIZATION.toString(), token);
        break;
      case CUSTOM_HEADER:
        request.setHeader(this.headerName, token);
        break;
      case CUSTOM_QUERY_PARAM:
        request.addQueryParam(this.queryParamName, token);
        break;
    }
    return next.process(request, context);
  }

  public abstract String generateToken(HttpRequest request, ApiContext context);
}
