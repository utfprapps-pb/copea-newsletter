package br.edu.utfpr.htmlfileswithcidinsteadbase64.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HtmlFilesWithCidInsteadBase64Model {
    private List<HtmlFileModel> html_files;
    private String html_with_content_id_instead_base64;
}
