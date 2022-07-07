package br.edu.utfpr.email.email.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailSendRequest {

    private String title;
    private String body;
    @JsonProperty("emails-list")
    private String[] emailsList;

}
