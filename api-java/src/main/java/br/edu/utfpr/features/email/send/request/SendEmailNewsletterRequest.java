package br.edu.utfpr.features.email.send.request;

import lombok.Data;

@Data
public class SendEmailNewsletterRequest extends AbstractEmailSendRequest {

    private Long newsletterId;

}
