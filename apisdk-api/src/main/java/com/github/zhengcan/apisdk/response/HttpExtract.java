package com.github.zhengcan.apisdk.response;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
@Inherited
@Documented
public @interface HttpExtract {
  String[] accept() default "*/*";
  Class<? extends HttpExtractor> using() default HttpExtractor.class;
}
