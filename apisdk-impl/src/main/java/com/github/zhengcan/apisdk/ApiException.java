package com.github.zhengcan.apisdk;

public class ApiException extends RuntimeException {

  private final int code;

  public ApiException(int code, String message) {
    super(message);
    this.code = code;
  }

  public ApiException() {
    this.code = -1;
  }

  public ApiException(String message) {
    super(message);
    this.code = -1;
  }

  public ApiException(String message, Throwable cause) {
    super(message, cause);
    this.code = -1;
  }

  public ApiException(Throwable cause) {
    super(cause);
    this.code = -1;
  }
}
