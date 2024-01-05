package com.github.zhengcan.apisdk;

import com.github.zhengcan.apisdk.build.ApiServiceBuildContext;

public interface ApiFactory {
  <T> ApiServiceBuildContext<T> createApiServiceBuildContext(Class<T> apiClass) throws BuildException;

  <T> T createApi(Class<T> apiClass) throws BuildException;
}