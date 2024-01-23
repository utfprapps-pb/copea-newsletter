package br.edu.utfpr.features.htmlfileswithcidinsteadbase64;

import br.edu.utfpr.features.htmlfileswithcidinsteadbase64.models.HtmlFilesWithCidInsteadBase64Model;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@Service
public class HtmlFilesWithCidInsteadBase64Service {

    @Inject
    EntityManager entityManager;

    public HtmlFilesWithCidInsteadBase64Model findHtmlFilesWithCidInsteadBase64Model(String html) throws JsonProcessingException {
        Query query = entityManager.createNativeQuery("select cast(to_json(get_html_files_with_cid_instead_base64(:html)) as varchar)");
        query.setParameter("html", html);
        String result = (String) query.getSingleResult();

        if ((result != null) && (!result.isEmpty())) {
            ObjectMapper objectMapper = new ObjectMapper();
            HtmlFilesWithCidInsteadBase64Model htmlFilesWithCidInsteadBase64DTO =
                    objectMapper.readValue(result, HtmlFilesWithCidInsteadBase64Model.class);

            return htmlFilesWithCidInsteadBase64DTO;
        }

        return null;
    }

}
