package br.edu.utfpr.features.htmlfileswithcidinsteadbase64.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HtmlFileModel {
    private String content_id;
    private String type_file;
    private String justbase64;
}
