package com.github.zhengcan.apisdk.response;

import com.github.zhengcan.apisdk.ApiException;
import com.github.zhengcan.apisdk.BuildException;
import com.github.zhengcan.apisdk.http.HttpResponse;
import com.github.zhengcan.apisdk.utils.ObjectUtils;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class DefaultXmlExtractorFactory extends DefaultHttpExtractor implements XmlExtractor.Factory {
  private Class<?> targetClass;
  private Class<? extends XmlExtractor> implClass;
  private XmlExtractor implInstance;
  private String path;

  @Override
  public <T> void init(Class<? extends T> apiClass, Annotation annotation) throws BuildException {
    super.init(apiClass, annotation);
    if (annotation instanceof XmlExtract) {
      XmlExtract extract = (XmlExtract) annotation;
      if (!extract.using().equals(XmlExtractor.class)) {
        this.implClass = extract.using();
      }
      if (this.implClass != null) {
        this.implInstance = ObjectUtils.createInstance(this.implClass);
      }
      if (this.path != null) {
        this.path = extract.path();
      }
    }
  }

  @Override
  public <T> T extract(HttpResponse response, Type returnType) throws ApiException {
    String xml = response.getResponseText();

    Document document;
    try {
      document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xml);
      System.out.println("document = " + document);
    } catch (Exception e) {
      throw new ApiException(e);
    }

    if (this.implInstance != null) {
      return this.implInstance.extract(document, this.path, returnType);
    } else {
      // FIXME:
      throw new ApiException();
    }
  }
}
