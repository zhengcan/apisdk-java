package com.github.zhengcan.apisdk.aop;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Inherited
@Documented
@Intercept(AccessTokenAuth.Factory.class)
public @interface AccessTokenAuth {
  interface Factory extends InterceptorFactory, Interceptor<Object> {
  }

  Class<? extends TokenGenerator> token() default TokenGenerator.class;

  TokenCarrier carrier() default TokenCarrier.BEARER_AUTH;

  String headerName() default "";

  String queryParamName() default "";
}
