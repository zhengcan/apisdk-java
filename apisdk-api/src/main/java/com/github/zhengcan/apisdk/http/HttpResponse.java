package com.github.zhengcan.apisdk.http;

import com.github.zhengcan.apisdk.ApiException;

public interface HttpResponse {
  String getStatusText();
  String getResponseText() throws ApiException;
}
