package com.github.zhengcan.apisdk.request;

import com.github.zhengcan.apisdk.MimeTypes;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
@Documented
@RequestBuilder(contentType = MimeTypes.APPLICATION_X_WWW_FORM_URLENCODED)
public @interface FormRequest {
}
