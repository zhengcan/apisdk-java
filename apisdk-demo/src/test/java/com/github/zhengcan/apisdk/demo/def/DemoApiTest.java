package com.github.zhengcan.apisdk.demo.def;

import com.github.zhengcan.apisdk.ApiFactory;
import com.github.zhengcan.apisdk.ApiFactoryBuilder;
import com.github.zhengcan.apisdk.build.ApiServiceBuilder;
import com.github.zhengcan.apisdk.build.ClassGenerator;
import com.github.zhengcan.apisdk.build.DefaultClassGenerator;
import com.google.gson.Gson;
import javassist.ClassPool;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertNotNull;

public class DemoApiTest {

  @Test
  public void testGenerate() throws Exception {
    ApiFactoryBuilder builder = ApiFactoryBuilder.newInstance();
    ApiFactory factory = builder.build();
    ApiServiceBuilder<DemoApi> serviceBuilder = factory.createApiServiceBuilder(DemoApi.class);

    ClassPool classPool = ClassPool.getDefault();
    Class<? extends DemoApi> apiImplClass = serviceBuilder.buildClass(classPool);
    System.out.println("############");
    System.out.println(apiImplClass);

    DemoApi demoApi = apiImplClass.getConstructor().newInstance();
    System.out.println("\t" + demoApi);

    for (Field field : apiImplClass.getFields()) {
      System.out.println(field);
      System.out.println("\t" + field.get(demoApi));
    }

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
