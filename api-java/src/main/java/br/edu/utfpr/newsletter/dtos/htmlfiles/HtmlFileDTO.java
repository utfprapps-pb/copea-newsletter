package br.edu.utfpr.newsletter.dtos.htmlfiles;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HtmlFileDTO {
    private String content_id;
    private String type_file;
    private String justbase64;
}
