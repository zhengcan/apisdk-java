package com.github.zhengcan.apisdk.runtime;

import com.github.zhengcan.apisdk.ApiContext;
import com.github.zhengcan.apisdk.ApiRuntime;
import com.github.zhengcan.apisdk.DefaultApiContext;

import java.lang.reflect.Method;

public class DefaultApiRuntime implements ApiRuntime {
  @Override
  public <T> ApiContext<T> createContext(T api, Method apiMethod) {
    return new DefaultApiContext<>(api, apiMethod);
  }

  @Override
  public <T> ApiContext<T> createContext(T api, Class<? extends T> apiClass, Method apiMethod) {
    return new DefaultApiContext<>(api, apiMethod);
  }
}
