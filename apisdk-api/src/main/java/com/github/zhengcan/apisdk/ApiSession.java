package com.github.zhengcan.apisdk;

import org.jetbrains.annotations.NotNull;

public interface ApiSession<T> {
  @NotNull
  T getApi();
  @NotNull
  T withRequestId(@NotNull String requestId);
  @NotNull
  T withTraceId(@NotNull String traceId);
  @NotNull
  T withTraceId(@NotNull String traceId, @NotNull String spanId);
}
