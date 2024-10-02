package br.edu.utfpr.features.email;

import br.edu.utfpr.features.email.group.relation.EmailGroupRelation;
import br.edu.utfpr.generic.crud.EntityId;
import br.edu.utfpr.shared.enums.NoYesEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class EmailDTO implements EntityId<Long> {

    private Long id;
    @NotBlank(message = "Parameter email is required.")
    private String email;
    @Enumerated(EnumType.STRING)
    private NoYesEnum subscribed;
    private String unsubscribeReason;
    private LocalDateTime lastEmailUnsubscribedDate;
    private List<EmailGroupRelation> emailGroupRelations;

}
