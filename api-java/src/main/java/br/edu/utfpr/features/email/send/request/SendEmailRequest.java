package br.edu.utfpr.features.email.send.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendEmailRequest extends AbstractEmailSendRequest {

    @JsonProperty("emails-list")
    private String[] emailsList;

}
