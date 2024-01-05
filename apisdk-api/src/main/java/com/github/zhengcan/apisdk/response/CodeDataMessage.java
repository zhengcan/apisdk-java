package com.github.zhengcan.apisdk.response;

@JsonExtract(using = CodeDataMessage.Impl.class, path = "data")
public interface CodeDataMessage {
  interface Impl extends JsonExtractor {}
}
