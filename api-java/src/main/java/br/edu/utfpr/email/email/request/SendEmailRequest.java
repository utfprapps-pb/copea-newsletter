package br.edu.utfpr.email.email.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendEmailRequest extends AbstractEmailSendRequest {

    @JsonProperty("emails-list")
    private String[] emailsList;

}
