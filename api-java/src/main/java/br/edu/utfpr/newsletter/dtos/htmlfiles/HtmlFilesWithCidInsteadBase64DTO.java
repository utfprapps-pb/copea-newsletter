package br.edu.utfpr.newsletter.dtos.htmlfiles;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HtmlFilesWithCidInsteadBase64DTO {
    private List<HtmlFileDTO> html_files;
    private String html_with_content_id_instead_base64;
}
