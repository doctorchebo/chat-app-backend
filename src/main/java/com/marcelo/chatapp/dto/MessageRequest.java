package com.marcelo.chatapp.dto;

import com.marcelo.chatapp.model.MessageType;

import java.util.List;

public class MessageRequest {
    private List<Long> recipientIds;
    private MessageType messageType;
    private String content;
}
