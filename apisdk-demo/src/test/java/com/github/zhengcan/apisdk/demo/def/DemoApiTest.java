package com.github.zhengcan.apisdk.demo.def;

import com.github.zhengcan.apisdk.ApiFactory;
import com.github.zhengcan.apisdk.ApiFactoryBuilder;
import com.github.zhengcan.apisdk.build.ApiServiceBuildContext;
import com.github.zhengcan.apisdk.build.DefaultCodeGenerator;
import com.github.zhengcan.apisdk.demo.def.DemoApi;
import com.github.zhengcan.apisdk.demo.def.Post;
import com.github.zhengcan.apisdk.demo.def.UserModule;
import com.google.gson.Gson;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class DemoApiTest {

  @Test
  public void testGenerate() throws Exception {
    ApiFactoryBuilder builder = ApiFactoryBuilder.newInstance();
    ApiFactory factory = builder.build();
    ApiServiceBuildContext<DemoApi> buildContext = factory.createApiServiceBuildContext(DemoApi.class);
    DefaultCodeGenerator<DemoApi> generator = new DefaultCodeGenerator<>(buildContext);
    System.out.println("!!!!!!!!!!!!");
    Class<? extends DemoApi> apiImplClass = generator.generate();
    System.out.println("############");
    System.out.println(apiImplClass);
    DemoApi demoApi = apiImplClass.getConstructor().newInstance();
    System.out.println(demoApi);
    Post post = demoApi.getPost(1L);
    System.out.println(post);
  }

  @Test
  public void testSimple() throws Exception {
    ApiFactoryBuilder builder = ApiFactoryBuilder.newInstance();
    ApiFactory factory = builder.build();
    DemoApi api = factory.createApi(DemoApi.class);

    Object post = api.withRequestId("requestId").getPost(1L);
    System.out.println(post);
    System.out.println(new Gson().toJson(post));
  }

  @Test
  public void testCreatePost() throws Exception {
    ApiFactoryBuilder builder = ApiFactoryBuilder.newInstance();
    ApiFactory factory = builder.build();
    DemoApi api = factory.createApi(DemoApi.class);

    Object post = api.createPost(66, "title", "body");
    System.out.println(post);
    System.out.println(new Gson().toJson(post));
  }

  @Test
  public void testViaModule() throws Exception {
    ApiFactoryBuilder builder = ApiFactoryBuilder.newInstance();
    ApiFactory factory = builder.build();
    DemoApi api = factory.createApi(DemoApi.class);

    UserModule module = api.getModule1();
    assertNotNull(module);
    Object user = module.getUser(1L);
    System.out.println(user);
    System.out.println(new Gson().toJson(user));
  }
}
