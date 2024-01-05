package com.github.zhengcan.apisdk.aop;

import com.github.zhengcan.apisdk.http.HttpRequest;

public interface TokenGenerator {
  String generateToken(HttpRequest request);
}
