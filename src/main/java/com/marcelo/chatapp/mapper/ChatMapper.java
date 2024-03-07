package com.marcelo.chatapp.mapper;

import com.marcelo.chatapp.dto.ChatDto;
import com.marcelo.chatapp.model.AppUser;
import com.marcelo.chatapp.model.Chat;
import com.marcelo.chatapp.service.ChatService;
import com.marcelo.chatapp.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    @Mapping(target = "participants", expression = "java(getParticipants(chatDto, userService))")
    Chat map(ChatDto chatDto, UserService userService);

    default List<AppUser> getParticipants(ChatDto chatDto, UserService userService) {
        List<Long> participantIds = chatDto.getParticipantsIds();
        return userService.getUsersByIds(participantIds);
    }
    @Mapping(target = "participantsIds", expression="java(getParticipantsIds(chat))")
    ChatDto mapToDto(Chat chat);

    @Mapping(target = "participantsIds", expression="java(getParticipantsIds(chat))")
    List<ChatDto> mapToDtoList(List<Chat> chats);

    default List<Long> getParticipantsIds(Chat chat) {
        return chat.getParticipants().stream().map(participant -> participant.getId()).collect(Collectors.toList());
    }

    @Mapping(target = "id", ignore = true) // Ignore mapping the ID during updates
    void updateChatFromDto(ChatDto dto, @MappingTarget Chat chat);
}