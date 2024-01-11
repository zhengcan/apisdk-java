package com.github.zhengcan.apisdk.build;

import com.github.zhengcan.apisdk.BuildException;
import com.github.zhengcan.apisdk.aop.Intercept;
import com.github.zhengcan.apisdk.aop.Intercepts;
import com.github.zhengcan.apisdk.request.CookieParam;
import com.github.zhengcan.apisdk.request.HeaderParam;
import com.github.zhengcan.apisdk.request.QueryParam;
import com.github.zhengcan.apisdk.request.RequestBuilder;
import com.github.zhengcan.apisdk.response.HttpExtract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBuilder<T> {
  protected final Logger apiLogger;
  protected final InterceptorList<T> interceptors;
  protected String contentType;
  protected RequestBuilder requestBuilder;
  protected Annotation requestBuilderImpl;
  protected HttpExtract httpExtract;
  protected Annotation entityExtract;
  protected List<HeaderParam> headerParams;
  protected List<CookieParam> cookieParams;
  protected List<QueryParam> queryParams;

  protected AbstractBuilder(Class<? extends T> apiClass, Logger apiLogger) {
    this.apiLogger = apiLogger;
    this.interceptors = new InterceptorList<>(apiClass);
    this.headerParams = new ArrayList<>();
    this.cookieParams = new ArrayList<>();
    this.queryParams = new ArrayList<>();
  }

  public void addIntercept(@NotNull Intercept intercept) throws BuildException {
    this.interceptors.add(intercept);
  }

  public void addIntercept(@NotNull Intercept intercept, @NotNull Annotation annotation) throws BuildException {
    this.interceptors.add(intercept, annotation);
  }

  public void setRequestBuilder(@NotNull RequestBuilder requestBuilder, @NotNull Annotation annotation) {
    this.requestBuilder = requestBuilder;
    this.requestBuilderImpl = annotation;
  }

  public void setExtract(@NotNull HttpExtract httpExtract, @Nullable Annotation entityExtract) {
    this.httpExtract = httpExtract;
    this.entityExtract = entityExtract;
  }

  public void processAnnotations(Annotation[] annotations) throws BuildException {
    System.out.println(this);
    for (Annotation annotation : annotations) {
      System.out.println("## " + annotation);
      Class<? extends Annotation> annotationType = annotation.annotationType();
      RequestBuilder reqBuilderAnno = annotationType.getAnnotation(RequestBuilder.class);
      if (reqBuilderAnno != null) {
        this.setRequestBuilder(reqBuilderAnno, annotation);
        continue;
      }
//      if (annotationType.equals(FormRequest.class)
//        || annotationType.equals(JsonRequest.class)
//        || annotationType.equals(XmlRequest.class)) {
//        this.setRequestBuilder(annotation);
//        continue;
//      }
      if (annotationType.equals(HttpExtract.class)) {
        this.setExtract((HttpExtract) annotation, null);
        continue;
      }
      HttpExtract httpExtract = annotationType.getAnnotation(HttpExtract.class);
      if (httpExtract != null) {
        this.setExtract(httpExtract, annotation);
        continue;
      }
//      if (annotationType.equals(HttpExtract.class)
//        || annotationType.equals(JsonExtract.class)
//        || annotationType.equals(TextExtract.class)
//        || annotationType.equals(XmlExtract.class)) {
//        this.setExtract(annotation);
//        continue;
//      }
      if (annotationType.equals(Intercept.class)) {
        this.addIntercept((Intercept) annotation);
        continue;
      }
      if (annotationType.equals(Intercepts.class)) {
        for (Intercept intercept : ((Intercepts) annotation).value()) {
          this.addIntercept(intercept);
        }
        continue;
      }
      Intercept intercept = annotationType.getAnnotation(Intercept.class);
      if (intercept != null) {
        this.addIntercept(intercept, annotation);
        continue;
      }
      if (annotationType.equals(HeaderParam.class)) {
        this.headerParams.add((HeaderParam) annotation);
        continue;
      }
      if (annotationType.equals(CookieParam.class)) {
        this.cookieParams.add((CookieParam) annotation);
        continue;
      }
      if (annotationType.equals(QueryParam.class)) {
        this.queryParams.add((QueryParam) annotation);
        continue;
      }
      this.apiLogger.debug("Skip annotation: {}", annotation);
    }
  }

  public Annotation getRequestBuilder() {
    return this.requestBuilder;
  }
}
