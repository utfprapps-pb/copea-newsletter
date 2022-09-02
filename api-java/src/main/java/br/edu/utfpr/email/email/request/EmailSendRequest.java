package br.edu.utfpr.email.email.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailSendRequest extends AbstractEmailSendRequest {

    @JsonProperty("emails-list")
    private String[] emailsList;

}
