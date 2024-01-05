package com.github.zhengcan.apisdk.demo.def;

import com.github.zhengcan.apisdk.ApiSession;
import com.github.zhengcan.apisdk.ApiModule;
import com.github.zhengcan.apisdk.aop.Intercept;
import com.github.zhengcan.apisdk.request.ApiMethod;
import com.github.zhengcan.apisdk.request.PathParam;
import com.github.zhengcan.apisdk.response.JsonExtract;

@ApiModule(path = "users")
@Intercept(UserModuleInterceptor.class)
public interface UserModule extends ApiSession<DemoApi> {
//  @GET("{userId}")
  @ApiMethod(method = "GET", path = "{userId}")
  @JsonExtract
  @Intercept(UserMethodInterceptor.class)
  @Intercept(UserMethod2Interceptor.class)
  User getUser(@PathParam(name = "userId") long userId);
}
