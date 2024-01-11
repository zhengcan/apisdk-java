package com.github.zhengcan.apisdk.build;

import com.github.zhengcan.apisdk.BuildException;
import com.github.zhengcan.apisdk.aop.Intercept;
import com.github.zhengcan.apisdk.aop.Interceptor;
import com.github.zhengcan.apisdk.aop.InterceptorFactory;
import com.github.zhengcan.apisdk.utils.ObjectUtils;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class InterceptorList<T> {
  private final Class<? extends T> apiClass;
  private final List<Interceptor<?>> inner;

  public InterceptorList(Class<? extends T> apiClass) {
    this.apiClass = apiClass;
    this.inner = new ArrayList<>();
  }

  public void add(Intercept intercept) throws BuildException {
    this.inner.add(ObjectUtils.createInstance(intercept.value()));
  }

  public void add(Intercept intercept, Annotation annotation) throws BuildException {
    if (InterceptorFactory.class.isAssignableFrom(intercept.value())) {
      @SuppressWarnings("unchecked")
      Class<? extends InterceptorFactory> factoryClass = (Class<? extends InterceptorFactory>) intercept.value();
      InterceptorFactory factory = ObjectUtils.createInstance(factoryClass);
      this.inner.add(factory.create(this.apiClass, annotation));
    } else {
      this.add(intercept);
    }
  }

  public Stream<Interceptor<?>> stream() {
    return this.inner.stream();
  }
}
