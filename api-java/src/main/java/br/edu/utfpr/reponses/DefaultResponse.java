package br.edu.utfpr.reponses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jboss.resteasy.reactive.RestResponse;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DefaultResponse {

    @JsonProperty("http-status")
    private Integer httpStatus;
    private String message;

}
