package br.edu.utfpr.reponses;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {

    private String token;
    private Long issuedAt;
    private Long expiresAt;

}
