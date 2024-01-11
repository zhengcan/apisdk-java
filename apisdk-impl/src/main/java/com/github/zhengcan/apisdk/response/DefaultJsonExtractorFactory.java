package com.github.zhengcan.apisdk.response;

import com.github.zhengcan.apisdk.ApiException;
import com.github.zhengcan.apisdk.BuildException;
import com.github.zhengcan.apisdk.http.HttpResponse;
import com.github.zhengcan.apisdk.utils.ObjectUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class DefaultJsonExtractorFactory extends DefaultHttpExtractor implements JsonExtractor.Factory {
  private Class<?> targetClass;
  private Class<? extends JsonExtractor> implClass;
  private JsonExtractor implInstance;
  private String path;

  @Override
  public <T> void init(Class<? extends T> apiClass, Annotation annotation) throws BuildException {
    super.init(apiClass, annotation);
    if (annotation instanceof JsonExtract) {
      JsonExtract extract = (JsonExtract) annotation;
      if (!extract.as().equals(Object.class)) {
        this.targetClass = extract.as();
        JsonExtract targetAnno = this.targetClass.getAnnotation(JsonExtract.class);
        if (targetAnno != null) {
          if (!targetAnno.using().equals(JsonExtractor.class)) {
            this.implClass = targetAnno.using();
          }
          this.path = targetAnno.path();
        }
      }
      if (!extract.using().equals(JsonExtractor.class)) {
        this.implClass = extract.using();
      }
      if (this.implClass != null) {
        this.implInstance = ObjectUtils.createInstance(this.implClass);
      }
      if (this.path != null) {
        this.path = extract.path();
      }
    }
  }

  @Override
  public <T> T extract(HttpResponse response, Type returnType) throws ApiException {
    String json = response.getResponseText();
    JsonElement jsonElement = JsonParser.parseString(json);
    System.out.println("jsonElement = " + jsonElement);
    if (this.implInstance != null) {
      return this.implInstance.extract(jsonElement, this.path, returnType);
    } else {
      return new Gson().fromJson(jsonElement, returnType);
    }
  }
}
