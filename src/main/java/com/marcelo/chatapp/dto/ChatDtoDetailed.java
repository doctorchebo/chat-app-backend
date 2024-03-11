package com.marcelo.chatapp.dto;

import com.marcelo.chatapp.model.ChatType;
import lombok.Data;

import java.util.List;

@Data
public class ChatDtoDetailed {
    private Long id;
    private String name;
    private ChatType type;
    private List<AppUserDto> participants;
    private Long chatOwnerId;
    private MessageDto lastMessage;
}
