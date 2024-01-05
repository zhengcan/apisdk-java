package com.github.zhengcan.apisdk.aop;

import com.github.zhengcan.apisdk.ApiContext;
import com.github.zhengcan.apisdk.http.HttpRequest;

public class DefaultHashedTokenAuth<T> extends AbstractTokenBasedAuth<T> {
  public DefaultHashedTokenAuth(HashedTokenAuth annotation) {
    super(annotation.carrier(), annotation.headerName(), annotation.queryParamName());
  }

  @Override
  public String generateToken(HttpRequest request, ApiContext context) {
    return "context";
  }
}
