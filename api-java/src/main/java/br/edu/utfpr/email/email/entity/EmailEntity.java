package br.edu.utfpr.email.email.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "email")
public class EmailEntity {

    @Id
    @SequenceGenerator(name = "email_id_sequence", sequenceName = "email_id_sequence", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "email_id_sequence")
    private Long id;
    @Column(nullable = false)
    private String email;

}
