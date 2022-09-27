package br.edu.utfpr.utils;

import br.edu.utfpr.configmapping.NewsletterConfigMapping;
import br.edu.utfpr.configmapping.JwtNewsletterConfigMapping;
import br.edu.utfpr.reponses.TokenResponse;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtClaimsBuilder;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.SecurityContext;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

@RequestScoped
public class TokenUtils {

    @Inject
    JwtNewsletterConfigMapping jwtNewsletterConfigMapping;

    @Inject
    NewsletterConfigMapping newsletterConfigMapping;

    public TokenResponse generateToken(String username, String... roles) throws Exception {
        PrivateKey privateKey = readPrivateKey(jwtNewsletterConfigMapping.privatekey_location());

        JwtClaimsBuilder claimsBuilder = Jwt.claims();
        long currentTimeInSecs = currentTimeInSecs();
        long expiresAt = currentTimeInSecs + jwtNewsletterConfigMapping.duration();

        Set<String> groups = new HashSet<>();
        for (String role : roles) {
            groups.add(role);
        }

        claimsBuilder.issuer(newsletterConfigMapping.dns())
                .subject(username)
                .issuedAt(currentTimeInSecs)
                .expiresAt(expiresAt)
                .groups(groups);

        return new TokenResponse().builder()
                    .token((claimsBuilder.jws().sign(privateKey)))
                    .issuedAt(currentTimeInSecs)
                    .expiresAt(expiresAt).build();
    }

    public String getUserPrincipalFromToken(SecurityContext securityContext) {
        if (securityContext == null)
            return "";

        return securityContext.getUserPrincipal().getName();
    }

    private static PrivateKey readPrivateKey(final String pemResName) throws Exception {
        try (InputStream contentIS = TokenUtils.class.getResourceAsStream(pemResName)) {
            byte[] tmp = new byte[4096];
            int length = contentIS.read(tmp);
            return decodePrivateKey(new String(tmp, 0, length, StandardCharsets.UTF_8));
        }
    }

    private static PrivateKey decodePrivateKey(final String pemEncoded) throws Exception {
        byte[] encodedBytes = toEncodedBytes(pemEncoded);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(keySpec);
    }

    private static byte[] toEncodedBytes(final String pemEncoded) {
        final String normalizedPem = removeBeginEnd(pemEncoded);
        return Base64.getDecoder().decode(normalizedPem);
    }

    private static String removeBeginEnd(String pem) {
        pem = pem.replaceAll("-----BEGIN (.*)-----", "");
        pem = pem.replaceAll("-----END (.*)----", "");
        pem = pem.replaceAll("\r\n", "");
        pem = pem.replaceAll("\n", "");
        return pem.trim();
    }

    private static int currentTimeInSecs() {
        long currentTimeMS = System.currentTimeMillis();
        return (int) (currentTimeMS / 1000);
    }

}