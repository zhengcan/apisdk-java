package com.github.zhengcan.apisdk.utils;

import com.github.zhengcan.apisdk.BuildException;

import java.util.ServiceLoader;

public class ObjectUtils {
  public static <T> T createInstance(Class<? extends T> clazz) throws BuildException {
    if (clazz.isInterface()) {
      return ServiceLoader.load(clazz).findFirst().orElseThrow(BuildException::new);
    }
    try {
      return clazz.getConstructor().newInstance();
    } catch (Exception e) {
      //
      throw new BuildException(e);
    }
  }
}
