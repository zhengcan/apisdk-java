package com.github.zhengcan.apisdk.build;

import javassist.CannotCompileException;
import javassist.NotFoundException;

public interface CodeGenerator<T> {
  Class<? extends T> generate() throws NotFoundException, CannotCompileException;
}
