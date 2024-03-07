package com.marcelo.chatapp.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "user_contacts")
public class UserContact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "contact_id")
    private AppUser contact;

    @Override
    public String toString() {
        return "UserContact{" +
                "id=" + id +
                ", user=" + user + // or contact, depending on your implementation
                ", contact=" + contact + // or user, depending on your implementation
                '}';
    }
}