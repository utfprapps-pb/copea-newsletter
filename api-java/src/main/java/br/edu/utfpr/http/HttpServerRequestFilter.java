package br.edu.utfpr.http;

import io.vertx.core.http.HttpServerRequest;
import lombok.Getter;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Context;

@Getter
@RequestScoped
public class HttpServerRequestFilter {

    @Context
    HttpServerRequest request;

}
