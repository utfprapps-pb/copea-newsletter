package br.edu.utfpr.login;

import br.edu.utfpr.reponses.GenericResponse;
import br.edu.utfpr.reponses.TokenResponse;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("login")
public class LoginResource {

    @Inject
    LoginService loginResource;

    @POST
    public Response logar(@Valid LoginRequest loginRequest) throws Exception {
        Optional<TokenResponse> tokenResponse = loginResource.logar(loginRequest);
        if (tokenResponse.isPresent())
            return Response.ok(tokenResponse.get()).build();
        else
            return getUnauthorizedResponse();
    }

    /** TODO: necessário implementar um fluxo de refresh token que vence em um tempo maior que o access token
     * ao invés de dar refresh toda vez no access token, pois assim nunca vai forçar o usuário a autenticar de novo
     */
//    @GET
//    @Path("token/refresh")
//    public Response refreshToken() throws Exception {
//        Optional<TokenResponse> tokenResponse = loginResource.refreshToken();
//        if (tokenResponse.isPresent())
//            return Response.ok(tokenResponse.get()).build();
//        else
//            return getUnauthorized();
//    }

    private Response getUnauthorizedResponse() {
        return Response
                .status(Response.Status.UNAUTHORIZED)
                .entity(GenericResponse.getGenericResponse("Usuário ou senha inválidos.", Response.Status.UNAUTHORIZED.getStatusCode()))
                .build();
    }

}