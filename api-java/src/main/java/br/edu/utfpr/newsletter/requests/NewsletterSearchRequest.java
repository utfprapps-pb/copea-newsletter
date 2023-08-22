package br.edu.utfpr.newsletter.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewsletterSearchRequest {

    private boolean newslettersSent = true;
    private boolean newslettersNotSent = true;
    private boolean newslettersTemplateMine = false;
    private boolean newslettersTemplateShared = false;
    private String description;

}
