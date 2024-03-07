package com.marcelo.chatapp.model;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Chat chat;

    @ManyToOne
    private AppUser sender;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "message_recipients",
            joinColumns = @JoinColumn(name = "message_id"),
            inverseJoinColumns = @JoinColumn(name = "recipient_id")
    )
    private List<AppUser> recipients;

    @Enumerated(EnumType.ORDINAL)
    private MessageType messageType;

    private String content;

    private Instant created;
}
