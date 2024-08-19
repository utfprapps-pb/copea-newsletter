package br.edu.utfpr.features.email.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailUnsubscribeRequest {

    private String uuid;
    private String reason;

}
