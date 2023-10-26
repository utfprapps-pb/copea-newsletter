package br.edu.utfpr.login;

import br.edu.utfpr.features.user.User;
import br.edu.utfpr.features.user.UserRepository;
import br.edu.utfpr.login.scenarios.LoginServiceTestScenario;
import br.edu.utfpr.reponses.TokenResponse;
import br.edu.utfpr.utils.TokenUtils;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@QuarkusTest
@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    private LoginServiceTestScenario loginServiceTestScenario = new LoginServiceTestScenario();

    /**
     * O @Spy deve estar por primeiro para funcionar
     */
    @Spy
    @InjectMocks
    LoginService loginService;

    @Mock
    UserRepository userRepository;

    @Mock
    TokenUtils tokenUtils;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void test_Return_Empty_When_LoginWithUser_NotExists() throws Exception {
        Mockito.when(userRepository.findByUsernameOrEmail(Mockito.any(), Mockito.any())).thenReturn(null);

        Optional<TokenResponse> tokenResponse = loginService.logar(new LoginRequest());
        Assertions.assertTrue(
                tokenResponse.isEmpty(),
                "Quando não encontrar o usuário passado na request, deve retornar um Optional vazio (Empty)"
        );
    }

    @Test
    void test_Return_TokenResponse_When_LoginWithUser_Exists() throws Exception {
        User user = loginServiceTestScenario.getUser();
        Mockito.when(userRepository.findByUsernameOrEmail(Mockito.any(), Mockito.any())).thenReturn(user);

        TokenResponse tokenResponseMock = loginServiceTestScenario.getTokenResponse();
        Mockito.when(tokenUtils.generateToken(Mockito.any(), Mockito.any())).thenReturn(tokenResponseMock);

        /**
         * Quando mockado um método da classe alvo (InjectMocks) deve ser usado o
         * @Spy em cima do @InjectMocks e feito dessa forma o mock, com o doReturn...
         */
        Mockito.doReturn(true).when(loginService).verifyPassword(Mockito.any(), Mockito.any());

        LoginRequest loginRequest = loginServiceTestScenario.getLoginRequest();
        Optional<TokenResponse> tokenResponseOptional = loginService.logar(loginRequest);
        Assertions.assertEquals(
                tokenResponseMock.getToken(),
                tokenResponseOptional.get().getToken(),
                "Quando encontrar o usuário passado na request e senha passada " +
                        "na request for igual a do usuário encontrado, deve retornar " +
                        "o token permitindo o login"
        );
    }

}
