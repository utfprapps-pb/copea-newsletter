package br.edu.utfpr.user.recover_password;

import br.edu.utfpr.utils.DateTimeUtils;
import io.quarkus.scheduler.Scheduled;
import lombok.Getter;
import lombok.Setter;

import javax.enterprise.context.ApplicationScoped;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
@Getter
@Setter
public class RecoverPasswordService {

    private static final String EVERY_MID_NIGHT = "0 0 0 * * ?";
    private static final Integer MAX_MINUTES_VALID_CODE = 30;

    private Map<String, RecoverPassword> codeSentByEmail = new HashMap<>();

    public void addCode(String username, RecoverPassword recoverPassword) {
        this.codeSentByEmail.put(username, recoverPassword);
    }

    @Scheduled(cron = EVERY_MID_NIGHT)
    public void clearExpiredCodesSentByEmail() {
        for (RecoverPassword recoverPassword : codeSentByEmail.values()) {
            if (codeExpired(recoverPassword))
                codeSentByEmail.remove(recoverPassword.getUsername());
        }
    }

    public Boolean codeExpired(RecoverPassword recoverPassword) {
        return (Duration.between(recoverPassword.getDateTime(), DateTimeUtils.getCurrentDateTime()).toMinutes() >= MAX_MINUTES_VALID_CODE);
    }

}
