package com.github.zhengcan.apisdk.response;

import com.github.zhengcan.apisdk.MimeTypes;

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
@HttpExtract(accept = MimeTypes.TEXT_PLAIN, using = TextExtractor.Factory.class)
public @interface TextExtract {
  Class<? extends TextExtractor> using() default TextExtractor.class;

}
