package com.github.zhengcan.apisdk.build;

import com.github.zhengcan.apisdk.ApiRuntime;
import com.github.zhengcan.apisdk.ApiSession;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;

public class DefaultCodeGenerator<T> implements CodeGenerator<T> {
  private final ApiServiceBuilder<T> serviceBuilder;

  public DefaultCodeGenerator(ApiServiceBuilder<T> serviceBuilder) {
    this.serviceBuilder = serviceBuilder;
  }

  @Override
  public Class<? extends T> generate() throws NotFoundException, CannotCompileException {
    Class<? extends T> apiClass = this.serviceBuilder.getApiClass();
    String apiImplClassName = apiClass.getName() + "$$Impl";
    ClassPool classPool = ClassPool.getDefault();
    CtClass objectClass = classPool.get(Object.class.getName());
    CtClass apiDeclClass = classPool.get(apiClass.getName());
    CtClass apiSessClass = classPool.get(ApiSession.class.getName());
    CtClass apiImplClass = classPool.makeClass(apiImplClassName);

    // 继承抽象类或实现接口
    apiImplClass.addInterface(apiDeclClass);

    // 定义基础字段
    // ApiRuntime
    CtField runtimeField = CtField.make(String.format("private static %s %s;", ApiRuntime.class.getName(), "runtime"), apiImplClass);
    apiImplClass.addField(runtimeField);
    // Method_Xxx

    // 创建构造函数，完成初始化

    ApiModuleBuilder<T, T> module = this.serviceBuilder.getRootModule();
    Class<? extends T> clazz = module.buildClass(apiImplClass);
    return clazz;
  }

//  public void xx() {
//    DefaultApiContext<ClassName> context = this.runtime.createContext(this, this.METHOD_XXX);
//
//    // Http
//    context.setHttpMethod("mmm");
//    context.setRawPath("/path/{param}");
//    context.setPath("/path/" + param);
//
//    // Params - JSON
//    context.setContentType(MimeTypes.APPLICATION_JSON);
//    JsonObject json = new JsonObject();
//    json.add("key", this.runtime.toJsonTree(param));
//    context.setJsonBody(json);
//
//
//  }
}
