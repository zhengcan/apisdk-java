package com.github.zhengcan.apisdk;

import org.slf4j.Logger;

import java.lang.reflect.Method;

public interface ApiRuntime {
  <T> ApiContext<T> createContext(T api, Method apiMethod);

  <T> ApiContext<T> createContext(T api, Class<? extends T> apiClass, Method apiMethod);
}
