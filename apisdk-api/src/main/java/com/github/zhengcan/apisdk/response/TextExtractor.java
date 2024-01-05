package com.github.zhengcan.apisdk.response;

import com.github.zhengcan.apisdk.ApiException;

import java.lang.reflect.Type;

public interface TextExtractor {
  interface Factory extends HttpExtractor {
  }

  <T> T extract(String text, Type returnType) throws ApiException;
}
