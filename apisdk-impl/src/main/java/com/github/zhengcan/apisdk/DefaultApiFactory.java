package com.github.zhengcan.apisdk;

import com.github.zhengcan.apisdk.build.ApiServiceBuilder;
import com.github.zhengcan.apisdk.build.DefaultApiServiceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultApiFactory implements ApiFactory {
  @Override
  public <T> ApiServiceBuilder<T> createApiServiceBuilder(Class<T> apiClass) throws BuildException {
    ApiService apiService = apiClass.getAnnotation(ApiService.class);
    if (apiService == null) {
      throw new BuildException("No @ApiService");
    }

    Logger apiLogger = LoggerFactory.getLogger(apiClass);
    DefaultApiServiceBuilder<T> serviceBuilder = new DefaultApiServiceBuilder<>(apiClass, apiService, apiLogger);

    serviceBuilder.load();
    return serviceBuilder;
  }

  @Override
  public <T> T createApi(Class<T> apiClass) throws BuildException {
    ApiServiceBuilder<T> serviceBuilder = this.createApiServiceBuilder(apiClass);
    return serviceBuilder.build();
  }
}
