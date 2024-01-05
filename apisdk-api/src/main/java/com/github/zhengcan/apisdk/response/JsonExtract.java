package com.github.zhengcan.apisdk.response;

import com.github.zhengcan.apisdk.MimeTypes;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Inherited
@Documented
@HttpExtract(accept = MimeTypes.APPLICATION_JSON, using = JsonExtractor.Factory.class)
public @interface JsonExtract {

  Class<?> as() default Object.class;

  Class<? extends JsonExtractor> using() default JsonExtractor.class;

  /**
   * <a href="https://goessner.net/articles/JsonPath/">JsonPath</a>
   */
  String path() default ".";
}
