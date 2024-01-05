package com.github.zhengcan.apisdk;

public class DefaultApiFactoryBuilder implements ApiFactoryBuilder {
  @Override
  public ApiFactory build() {
    return new DefaultApiFactory();
  }
}
