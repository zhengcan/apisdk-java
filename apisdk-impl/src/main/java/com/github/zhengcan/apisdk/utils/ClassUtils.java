package com.github.zhengcan.apisdk.utils;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import java.lang.reflect.Method;

public class ClassUtils {
  public static CtMethod findCtMethod(CtClass ctClass, Method method) throws NotFoundException {
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
        CtClass desiredParamType = ctClass.getClassPool().getCtClass(paramType.getName());
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
