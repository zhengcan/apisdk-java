package com.github.zhengcan.apisdk.http;

public interface HttpRequest {
  void setHeader(String name, String value);

  void addQueryParam(String name, String value);
}
