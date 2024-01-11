package com.github.zhengcan.apisdk.build;

import com.github.zhengcan.apisdk.BuildException;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.NotFoundException;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;

public class DefaultClassGenerator implements ClassGenerator {
  private final String className;
  private final CtClass ctClass;

  public DefaultClassGenerator(ClassPool classPool, String className) {
    System.out.println("class " + className);
    this.className = className;
    this.ctClass = classPool.makeClass(className);
  }

  @Override
  public String getClassName() {
    return this.className;
  }

  @Override
  public <T> void addInterface(Class<? extends T> apiClass) throws BuildException {
    try {
      System.out.println("implements " + apiClass.getName());
      this.ctClass.addInterface(this.ctClass.getClassPool().getCtClass(apiClass.getName()));
    } catch (NotFoundException e) {
      throw new BuildException(e);
    }
  }

  @Override
  public void addStaticField(Type fieldType, String fieldName) throws BuildException {
    try {
      String src = String.format("public static %s %s;", fieldType.getTypeName(), fieldName);
      System.out.println(src);
      this.ctClass.addField(CtField.make(src, this.ctClass));
    } catch (CannotCompileException e) {
      throw new BuildException(e);
    }
  }

  @Override
  public <T> Class<? extends T> generate() throws BuildException {
    try {
      @SuppressWarnings("unchecked")
      Class<? extends T> clazz = (Class<? extends T>) this.ctClass.toClass();
      return clazz;
    } catch (CannotCompileException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public MethodBody addMethod(Method apiMethod) throws BuildException {
    try {
      System.out.printf(
        "%s %s(%s)%n",
        apiMethod.getReturnType().getName(),
        apiMethod.getName(),
        Arrays.toString(apiMethod.getParameterTypes())
      );
      CtMethod ctMethod = this.findCtMethod(this.ctClass, apiMethod);
      CtMethod newMethod = CtNewMethod.copy(ctMethod, this.ctClass, null);
      return new MethodBody(this.ctClass, newMethod);
    } catch (Exception e) {
      throw new BuildException(e);
    }
  }

  /**
   * 查找与给定 Method 相匹配的 CtMethod
   *
   * @param ctClass
   * @param method
   * @return
   * @throws NotFoundException
   */
  private CtMethod findCtMethod(CtClass ctClass, Method method) throws NotFoundException {
    for (CtMethod ctMethod : ctClass.getMethods()) {
      if (!ctMethod.getName().equals(method.getName())) {
        continue;
      }
      CtClass[] ctParamTypes = ctMethod.getParameterTypes();
      Class<?>[] paramTypes = method.getParameterTypes();
      if (ctParamTypes.length != paramTypes.length) {
        continue;
      }
      boolean sameParamType = true;
      for (int i = 0; i < paramTypes.length; i++) {
        CtClass ctParamType = ctParamTypes[i];
        Class<?> paramType = paramTypes[i];
        CtClass desiredParamType = this.ctClass.getClassPool().getCtClass(paramType.getName());
        if (!ctParamType.equals(desiredParamType)) {
          sameParamType = false;
          break;
        }
      }
      if (sameParamType) {
        return ctMethod;
      }
    }
    return null;
  }
}
