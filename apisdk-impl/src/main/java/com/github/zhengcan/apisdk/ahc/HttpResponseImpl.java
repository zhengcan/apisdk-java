package com.github.zhengcan.apisdk.ahc;

import com.github.zhengcan.apisdk.ApiException;
import com.github.zhengcan.apisdk.http.HttpResponse;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Response;

import java.util.concurrent.ExecutionException;

public class HttpResponseImpl implements HttpResponse {
  private final ListenableFuture<Response> future;

  public HttpResponseImpl(ListenableFuture<Response> future) {
    this.future = future;
  }

  @Override
  public String getStatusText() {
    try {
      return this.future.get().getStatusText();
    } catch (Exception e) {
      return e.getMessage();
    }
  }

  @Override
  public String getResponseText() throws ApiException {
    try {
      return this.future.get().getResponseBody();
    } catch (Exception e) {
      throw new ApiException(e);
    }
  }
}
