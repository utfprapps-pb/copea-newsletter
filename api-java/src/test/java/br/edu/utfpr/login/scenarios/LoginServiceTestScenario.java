package br.edu.utfpr.login.scenarios;

import br.edu.utfpr.features.user.User;
import br.edu.utfpr.login.LoginRequest;
import br.edu.utfpr.reponses.TokenResponse;

public class LoginServiceTestScenario {

    public User getUser() {
        User user = new User();
        user.setId(1L);
        user.setPassword("teste");
        return user;
    }

    public TokenResponse getTokenResponse() {
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setToken("3141432423432");
        return tokenResponse;
    }

    public LoginRequest getLoginRequest() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("user");
        loginRequest.setPassword("senhaErrada");
        return loginRequest;
    }

}
