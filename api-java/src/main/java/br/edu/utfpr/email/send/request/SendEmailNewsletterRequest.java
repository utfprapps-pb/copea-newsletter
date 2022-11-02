package br.edu.utfpr.email.send.request;

import lombok.Data;

@Data
public class SendEmailNewsletterRequest extends AbstractEmailSendRequest {

    private Long newsletterId;

}
