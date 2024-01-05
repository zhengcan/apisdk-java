package com.github.zhengcan.apisdk.response;

import com.github.zhengcan.apisdk.ApiException;
import org.w3c.dom.Document;

import java.lang.reflect.Type;

public interface XmlExtractor {
  interface Factory extends HttpExtractor {
  }

  <T> T extract(Document document, String path, Type returnType) throws ApiException;
}
