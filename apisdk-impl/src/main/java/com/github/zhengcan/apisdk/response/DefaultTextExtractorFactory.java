package com.github.zhengcan.apisdk.response;

import com.github.zhengcan.apisdk.ApiException;
import com.github.zhengcan.apisdk.BuildException;
import com.github.zhengcan.apisdk.http.HttpResponse;
import com.github.zhengcan.apisdk.utils.ObjectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class DefaultTextExtractorFactory extends DefaultHttpExtractor implements TextExtractor.Factory {
  private Class<?> targetClass;
  private Class<? extends TextExtractor> implClass;
  private TextExtractor implInstance;

  @Override
  public <T> void init(Class<? extends T> apiClass, Annotation annotation) throws BuildException {
    super.init(apiClass, annotation);
    if (annotation instanceof TextExtract) {
      TextExtract extract = (TextExtract) annotation;
      if (!extract.using().equals(TextExtractor.class)) {
        this.implClass = extract.using();
      }
      if (this.implClass != null) {
        this.implInstance = ObjectUtils.createInstance(this.implClass);
      }
    }
  }

  @Override
  public <T> T extract(HttpResponse response, Type returnType) throws ApiException {
    String text = response.getResponseText();
    if (this.implInstance != null) {
      return this.implInstance.extract(text, returnType);
    } else {
      // FIXME:
      throw new ApiException();
    }
  }
}
