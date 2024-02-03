package br.edu.utfpr.frontend_config;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@RequestScoped
@Path("frontend-config")
public class FrontEndConfigResource {

    @POST
    public Response update(FrontEndConfig frontEndConfig) {
        FrontEndConfigService.frontEndConfig = frontEndConfig;
        return Response.ok().build();
    }

}
