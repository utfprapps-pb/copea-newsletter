package br.edu.utfpr.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.security.jpa.Password;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@Entity(name = "users")
public class User implements Serializable {

    @Id
    @SequenceGenerator(name = "users_id_sequence", sequenceName = "users_id_sequence", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "users_id_sequence")
    private Long id;

    @Column
    @JsonProperty("fullname")
    @NotNull
    private String fullName;

    @Column(unique = true)
    @NotNull
    private String username;

    @Column(unique = true)
    @NotNull
    private String email;

    @Column
    @NotNull
    @Password
    private String password;

}
