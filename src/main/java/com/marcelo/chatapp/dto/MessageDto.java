package com.marcelo.chatapp.dto;

import com.marcelo.chatapp.model.ContentType;
import com.marcelo.chatapp.model.MessageType;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class MessageDto {
    private Long id;
    private Long chatId;
    private List<Long> recipientIds;
    private MessageType messageType;
    private String content;
    private Instant created;
}
