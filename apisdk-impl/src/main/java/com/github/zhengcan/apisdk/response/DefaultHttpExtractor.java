package com.github.zhengcan.apisdk.response;

import com.github.zhengcan.apisdk.ApiException;
import com.github.zhengcan.apisdk.BuildException;
import com.github.zhengcan.apisdk.http.HttpResponse;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class DefaultHttpExtractor implements HttpExtractor {
  protected String[] accepts = new String[0];

  @Override
  public <T> void init(Class<? extends T> apiClass, Annotation annotation) throws BuildException {
    if (annotation instanceof HttpExtract) {
      this.accepts = ((HttpExtract) annotation).accept();
    }
  }

  @Override
  public <T> T extract(HttpResponse response, Type returnType) throws ApiException {
    @SuppressWarnings("unchecked")
    T result = (T) response;
    return result;
  }
}
