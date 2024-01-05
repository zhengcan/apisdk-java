package com.github.zhengcan.apisdk.demo.def;

import com.github.zhengcan.apisdk.ApiModule;
import com.github.zhengcan.apisdk.ApiService;
import com.github.zhengcan.apisdk.ApiSession;
import com.github.zhengcan.apisdk.aop.AccessTokenAuth;
import com.github.zhengcan.apisdk.aop.RequestTraceId;
import com.github.zhengcan.apisdk.request.ApiMethod;
import com.github.zhengcan.apisdk.request.JsonRequest;
import com.github.zhengcan.apisdk.request.POST;
import com.github.zhengcan.apisdk.request.Param;
import com.github.zhengcan.apisdk.request.PathParam;
import com.github.zhengcan.apisdk.response.CodeDataMessage;
import com.github.zhengcan.apisdk.response.JsonExtract;

@ApiService("https://jsonplaceholder.typicode.com/")
@AccessTokenAuth
@RequestTraceId
public interface DemoApi extends ApiSession<DemoApi> {
  @ApiMethod(method = "GET", path = "todos/{postId}")
  @JsonExtract(as = CodeDataMessage.class)
  Post getPost(@PathParam(name = "postId") Long postId);

  @POST("posts")
  @JsonRequest
  @JsonExtract
  Post createPost(
    @Param(name = "userId") long userId,
    @Param(name = "title") String title,
    @Param(name = "body") String body
  );

  @ApiModule
  UserModule getModule1();

  UserModule getModule2();
}
