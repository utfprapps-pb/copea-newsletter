package br.edu.utfpr.features.email.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailSelfRegistrationRequest {

    private String email;
    private String groupUuid;

}
