package com.github.zhengcan.apisdk.response;

import com.github.zhengcan.apisdk.ApiException;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.lang.reflect.Type;

public class CodeDataMessageExtractor implements CodeDataMessage.Impl {
  @Override
  public <T> T extract(JsonObject json, String path, Type returnType) throws ApiException {
    JsonPrimitive code = json.getAsJsonPrimitive("code");
    if (code == null) {
      throw new ApiException("No code");
    } else if (code.getAsInt() != 0) {
      JsonPrimitive message = json.getAsJsonPrimitive("message");
      if (message == null) {
        message = json.getAsJsonPrimitive("msg");
      }
      if (message == null) {
        throw new ApiException(code.getAsInt(), "Error");
      } else {
        throw new ApiException(code.getAsInt(), message.getAsString());
      }
    } else {
      // Evaluate path
      JsonElement result;
      if (path.equals(".")) {
        result = json;
      } else if (path.equals("data")) {
        result = json.get("data");
      } else {
        // Unsupported
        return null;
      }

      if (result instanceof JsonNull) {
        return null;
      } else {
        return new Gson().fromJson(result, returnType);
      }
    }
  }
}
