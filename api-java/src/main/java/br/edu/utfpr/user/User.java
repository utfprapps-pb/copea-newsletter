package br.edu.utfpr.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.security.jpa.Password;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.persistence.*;
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
    private String fullName;

    @Column
    private String username;

    @Column
    @Password
    private String password;

}
