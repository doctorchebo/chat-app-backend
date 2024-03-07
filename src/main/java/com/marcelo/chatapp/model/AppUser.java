package com.marcelo.chatapp.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;
import java.util.List;

@Entity
@Data
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "username is required")
    private String username;
    @NotBlank(message = "Password is required")
    private String password;
    @Email
    @NotEmpty(message = "Email is required")
    private String email;
    private Instant created;
    private boolean enabled;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserContact> contacts;
    @ManyToMany(mappedBy = "participants", fetch = FetchType.LAZY)
    private List<Chat> chats;

    @Override
    public String toString() {
        return "AppUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                // Other fields and their values
                '}';
    }
}
