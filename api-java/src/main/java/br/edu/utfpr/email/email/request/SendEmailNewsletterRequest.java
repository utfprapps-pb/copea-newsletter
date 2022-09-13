package br.edu.utfpr.email.email.request;

import lombok.Data;

@Data
public class SendEmailNewsletterRequest extends AbstractEmailSendRequest {

    private Long newsletterId;

}
