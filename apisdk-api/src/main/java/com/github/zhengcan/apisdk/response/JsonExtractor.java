package com.github.zhengcan.apisdk.response;

import com.github.zhengcan.apisdk.ApiException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.lang.reflect.Type;

public interface JsonExtractor {
  interface Factory extends HttpExtractor {
  }

  default <T> T extract(JsonElement json, String path, Type returnType) throws ApiException {
    if (json instanceof JsonObject) {
      return this.extract((JsonObject) json, path, returnType);
    } else if (json instanceof JsonArray) {
      return this.extract((JsonArray) json, path, returnType);
    } else if (json instanceof JsonNull) {
      return this.extract((JsonNull) json, path, returnType);
    } else if (json instanceof JsonPrimitive) {
      return this.extract((JsonPrimitive) json, path, returnType);
    } else {
      throw new ApiException();
    }
  }

  default <T> T extract(JsonObject json, String path, Type returnType) throws ApiException {
    throw new ApiException();
  }

  default <T> T extract(JsonArray json, String path, Type returnType) throws ApiException {
    throw new ApiException();
  }

  default <T> T extract(JsonNull json, String path, Type returnType) throws ApiException {
    throw new ApiException();
  }

  default <T> T extract(JsonPrimitive json, String path, Type returnType) throws ApiException {
    throw new ApiException();
  }
}
