package br.edu.utfpr.features.newsletter.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LastSentEmailNewsletter {

    private LocalDateTime logDate;

}
