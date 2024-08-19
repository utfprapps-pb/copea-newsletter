package br.edu.utfpr.auth;

import br.edu.utfpr.auth.context.AuthUserContext;
import lombok.Getter;

import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;
import java.util.Objects;

@Getter
@Provider
public class AuthSecurityFilter implements ContainerRequestFilter {

    @Context
    SecurityContext securityCtx;

    @Inject
    AuthUserContext authUserContext;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        if (Objects.isNull(securityCtx.getUserPrincipal()))
            return;

        authUserContext.setUsername(Long.valueOf(securityCtx.getUserPrincipal().getName()));
    }

}
