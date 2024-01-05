package com.github.zhengcan.apisdk;

public interface ApiFactoryBuilder {
  public static ApiFactoryBuilder newInstance() {
    return new DefaultApiFactoryBuilder();
  }

  ApiFactory build();
}