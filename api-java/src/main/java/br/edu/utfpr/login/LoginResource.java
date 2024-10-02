package br.edu.utfpr.login;

import br.edu.utfpr.reponses.GenericErrorResponse;
import br.edu.utfpr.reponses.TokenResponse;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

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
                .entity(GenericErrorResponse.getGenericResponse("Usuário ou senha inválidos.", Response.Status.UNAUTHORIZED.getStatusCode()))
                .build();
    }

}