package com.github.zhengcan.apisdk.build;

import com.github.zhengcan.apisdk.BuildException;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;

public class MethodBody {
  private final CtClass ctCLass;
  private final CtMethod ctMethod;
  private final StringBuilder src;

  public MethodBody(CtClass ctClass, CtMethod ctMethod) {
    this.ctCLass = ctClass;
    this.ctMethod = ctMethod;
    this.src = new StringBuilder();
  }

  public void add(String format, Object... args) {
    this.src.append(String.format(format, args));
    this.src.append('\n');
  }

  public void freeze() throws BuildException {
    try {
      String body = "{\n" + this.src + "}";
      System.out.println(body);
      this.ctMethod.setBody(body);
      this.ctCLass.addMethod(this.ctMethod);
    } catch (CannotCompileException e) {
      throw new BuildException(e);
    }
  }

  public String escape(String text) {
    return text;
  }
}
