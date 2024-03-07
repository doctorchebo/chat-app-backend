package com.marcelo.chatapp.dto;

import com.marcelo.chatapp.model.ChatType;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class ChatDto {
    private Long id;
    private String name;
    private ChatType type;
    private List<Long> participantsIds;
}
