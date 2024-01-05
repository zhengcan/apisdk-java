package com.github.zhengcan.apisdk.aop;

import com.github.zhengcan.apisdk.http.HttpRequest;

public class DefaultTokenGenerator implements TokenGenerator {
  @Override
  public String generateToken(HttpRequest request) {
    return "TOKEN";
  }
}
