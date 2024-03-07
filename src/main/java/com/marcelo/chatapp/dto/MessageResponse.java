package com.marcelo.chatapp.dto;

import com.marcelo.chatapp.model.MessageType;

import java.time.Instant;
import java.util.List;

public class MessageResponse {
    private Long chatId;
    private List<Long> recipientIds;
    private MessageType type;
    private String content;
    private Instant created;
}
