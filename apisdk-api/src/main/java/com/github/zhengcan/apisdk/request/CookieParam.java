package com.github.zhengcan.apisdk.request;

import com.github.zhengcan.apisdk.Defaults;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
@Inherited
@Documented
public @interface CookieParam {
  String name();
  String value() default Defaults.NO_VALUE;
  boolean skipNull() default true;
}
