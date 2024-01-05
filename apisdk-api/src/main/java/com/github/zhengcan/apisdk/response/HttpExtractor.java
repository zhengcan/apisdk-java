package com.github.zhengcan.apisdk.response;

import com.github.zhengcan.apisdk.ApiException;
import com.github.zhengcan.apisdk.BuildException;
import com.github.zhengcan.apisdk.http.HttpResponse;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public interface HttpExtractor {
  <T> void init(Class<? extends T> apiClass, Annotation annotation) throws BuildException;

  <T> T extract(HttpResponse response, Type returnType) throws ApiException;
}
