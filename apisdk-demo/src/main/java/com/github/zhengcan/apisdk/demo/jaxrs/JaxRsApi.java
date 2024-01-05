package com.github.zhengcan.apisdk.demo.jaxrs;


import com.github.zhengcan.apisdk.ApiContext;
import com.github.zhengcan.apisdk.ApiService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.MatrixParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;

@ApiService("http://host/api")
public interface JaxRsApi {
  @GET
  @Path("/path/endpoint/{path}")
  @Consumes("application/json")
  @Produces("application/json")
  String sayHello(
    @PathParam("path") String path,
    @QueryParam("query") String query,
    @MatrixParam("matrix") String matrix,
    @CookieParam("cookie") String cookie,
    @HeaderParam("header") String header,
    @Context ApiContext context
  );
}
