package com.github.zhengcan.apisdk;

import com.github.zhengcan.apisdk.build.ApiServiceBuilder;

public interface ApiFactory {
  <T> ApiServiceBuilder<T> createApiServiceBuilder(Class<T> apiClass) throws BuildException;

  <T> T createApi(Class<T> apiClass) throws BuildException;
}