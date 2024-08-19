package br.edu.utfpr.http;

import io.vertx.core.http.HttpServerRequest;
import lombok.Getter;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Context;

@Getter
@RequestScoped
public class HttpServerRequestFilter {

    @Context
    HttpServerRequest request;

}
