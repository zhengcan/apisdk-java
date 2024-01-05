package com.github.zhengcan.apisdk;

import com.github.zhengcan.apisdk.build.ApiServiceBuildContext;
import com.github.zhengcan.apisdk.build.DefaultApiServiceBuildContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultApiFactory implements ApiFactory {
  @Override
  public <T> ApiServiceBuildContext<T> createApiServiceBuildContext(Class<T> apiClass) throws BuildException {
    ApiService apiService = apiClass.getAnnotation(ApiService.class);
    if (apiService == null) {
      throw new BuildException("No @ApiService");
    }

    Logger apiLogger = LoggerFactory.getLogger(apiClass);
    DefaultApiServiceBuildContext<T> buildContext = new DefaultApiServiceBuildContext<>(apiClass, apiService, apiLogger);

    buildContext.load();
    return buildContext;
  }

  @Override
  public <T> T createApi(Class<T> apiClass) throws BuildException {
    ApiServiceBuildContext<T> apiServiceBuildContext = this.createApiServiceBuildContext(apiClass);
    return apiServiceBuildContext.build();
  }
}
