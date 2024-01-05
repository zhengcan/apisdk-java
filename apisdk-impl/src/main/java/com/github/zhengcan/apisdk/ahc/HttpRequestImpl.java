package com.github.zhengcan.apisdk.ahc;

import com.github.zhengcan.apisdk.http.HttpRequest;
import com.github.zhengcan.apisdk.http.HttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import org.asynchttpclient.BoundRequestBuilder;

public class HttpRequestImpl implements HttpRequest {
  private final BoundRequestBuilder builder;

  public HttpRequestImpl(BoundRequestBuilder builder) {
    this.builder = builder;
  }

  @Override
  public void setHeader(String name, String value) {
    this.builder.setHeader(name, value);
  }

  @Override
  public void addQueryParam(String name, String value) {
    this.builder.addQueryParam(name, value);
  }

  public HttpResponseImpl execute() {
    return new HttpResponseImpl(this.builder.execute());
  }

  public void setContentType(String contentType) {
    this.builder.setHeader(HttpHeaderNames.CONTENT_TYPE.toString(), contentType);
  }

  public void setBody(String data) {
    System.out.println("Data = " + data);
    this.builder.setBody(data);
  }
}
