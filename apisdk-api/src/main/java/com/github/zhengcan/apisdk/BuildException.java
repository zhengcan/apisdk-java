package com.github.zhengcan.apisdk;

public class BuildException extends Exception {
  public BuildException() {
  }

  public BuildException(String message) {
    super(message);
  }

  public BuildException(String message, Throwable cause) {
    super(message, cause);
  }

  public BuildException(Throwable cause) {
    super(cause);
  }
}
