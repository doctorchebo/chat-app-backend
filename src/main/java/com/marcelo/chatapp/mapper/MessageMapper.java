package com.marcelo.chatapp.mapper;

import com.marcelo.chatapp.dto.MessageDto;
import com.marcelo.chatapp.dto.MessageResponse;
import com.marcelo.chatapp.model.AppUser;
import com.marcelo.chatapp.model.Chat;
import com.marcelo.chatapp.model.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class MessageMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "chat", source = "chat")
    @Mapping(target = "sender", source = "sender")
    @Mapping(target = "created", expression = "java(java.time.Instant.now())")
    public abstract Message map(MessageDto messageDto, Chat chat, AppUser sender);

    @Mapping(target = "chatId", source = "chat.id")
    @Mapping(target = "recipientIds", expression = "java(getRecipientsIds(message))")
    public abstract MessageDto mapToDto(Message message);



    public abstract MessageResponse mapToMessageResponse(MessageDto messageDto, Chat chat, AppUser sender, List<AppUser> recipients);

    List<Long> getRecipientsIds (Message message){
        return message.getRecipients().stream().map(user -> user.getId()).collect(Collectors.toList());
    }

    public abstract List<MessageDto> mapToDtoList(List<Message> messages);
}
