package br.edu.utfpr.configmapping;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "newsletter")
public interface NewsletterConfigMapping {

    String dns();

}
