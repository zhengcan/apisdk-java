package com.github.zhengcan.apisdk.utils;

import jakarta.validation.constraints.NotNull;

import java.net.URI;
import java.net.URISyntaxException;

public class UrlUtils {

  /**
   * 连接两段 URL 地址
   *
   * @param uri  基础 URI
   * @param path URL 路径
   * @return 新 URL 地址
   */
  @NotNull
  public static String join(@NotNull URI uri, @NotNull String path) {
    char first = path.charAt(0);
    if (first == '/') {
      // 绝对路径
      return uri.resolve(path).toString();
    } else {
      // 相对路径
      String url = uri.toString();
      if (url.endsWith("/") || first == '?' || first == '&' || first == '#') {
        return url + path;
      } else {
        return url + "/" + path;
      }
    }
  }

  /**
   * 连接两段 URL 地址
   *
   * @param base 基础路径
   * @param path URL 路径
   * @return 新 URL 地址
   */
  @NotNull
  public static String join(@NotNull String base, @NotNull String path) {
    char first = path.charAt(0);
    if (first == '/') {
      // 绝对路径
      if (base.contains(":")) {
        try {
          return new URI(base).resolve(path).toString();
        } catch (URISyntaxException e) {
          throw new RuntimeException(e);
        }
      }
      return path;
    } else {
      // 相对路径
      if (base.endsWith("/") || first == '?' || first == '&' || first == '#') {
        return base + path;
      } else {
        return base + "/" + path;
      }
    }
  }
}
