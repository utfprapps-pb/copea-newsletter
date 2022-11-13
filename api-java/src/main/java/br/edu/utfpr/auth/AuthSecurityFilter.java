package br.edu.utfpr.auth;

import br.edu.utfpr.auth.context.AuthUserContext;
import lombok.Getter;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
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
