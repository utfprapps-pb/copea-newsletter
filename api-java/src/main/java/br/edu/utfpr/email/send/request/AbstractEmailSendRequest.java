package br.edu.utfpr.email.send.request;

import lombok.Data;

@Data
public abstract class AbstractEmailSendRequest {

    private String title = "";
    private String body = "";

}
