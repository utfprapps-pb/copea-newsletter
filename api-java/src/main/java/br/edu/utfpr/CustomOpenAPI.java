package br.edu.utfpr;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;

import javax.ws.rs.core.Application;

@OpenAPIDefinition(
        info = @Info(
                title = "API newsletter",
                description = "Métodos para o sistema newsletter",
                version = "")
)
public class CustomOpenAPI extends Application {
}

