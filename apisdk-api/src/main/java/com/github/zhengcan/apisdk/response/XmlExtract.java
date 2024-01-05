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
@HttpExtract(accept = {MimeTypes.APPLICATION_XML, MimeTypes.TEXT_XML}, using = XmlExtractor.Factory.class)
public @interface XmlExtract {
  Class<? extends XmlValidator> validator() default XmlValidator.class;

  Class<? extends XmlExtractor> using() default XmlExtractor.class;

  /**
   * <a href="https://www.w3schools.com/xml/xpath_syntax.asp">XPath</a>
   */
  String path() default ".";
}
