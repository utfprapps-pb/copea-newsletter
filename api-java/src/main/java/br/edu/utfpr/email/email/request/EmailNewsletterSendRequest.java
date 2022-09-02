package br.edu.utfpr.email.email.request;

import lombok.Data;

@Data
public class EmailNewsletterSendRequest extends AbstractEmailSendRequest {

    private Long newsletterId;

}
