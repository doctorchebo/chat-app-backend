package com.marcelo.chatapp.mapper;

import com.marcelo.chatapp.dto.AppUserDto;
import com.marcelo.chatapp.dto.ChatDto;
import com.marcelo.chatapp.dto.ChatDtoDetailed;
import com.marcelo.chatapp.dto.MessageDto;
import com.marcelo.chatapp.model.AppUser;
import com.marcelo.chatapp.model.Chat;
import com.marcelo.chatapp.model.Message;
import com.marcelo.chatapp.service.ChatService;
import com.marcelo.chatapp.service.UserService;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {AppUserMapper.class, MessageMapper.class})

public interface ChatMapper {

    @Mapping(target = "participants", expression = "java(getParticipants(chatDto, userService))")
    Chat map(ChatDto chatDto, UserService userService);

    default List<AppUser> getParticipants(ChatDto chatDto, UserService userService) {
        List<Long> participantIds = chatDto.getParticipantsIds();
        return userService.getUsersByIds(participantIds);
    }
    @Mapping(target = "participantsIds", expression="java(getParticipantsIds(chat))")
    ChatDto mapToDto(Chat chat);
    @Mapping(target = "participants", expression = "java(mapParticipantsToDtoList(chat.getParticipants(), appUserMapper))")
    ChatDtoDetailed mapToDtoDetailed(Chat chat, @Context AppUserMapper appUserMapper, @Context MessageMapper messageMapper);

    List<ChatDtoDetailed> mapToDtoListDetailed(List<Chat> chats, @Context AppUserMapper appUserMapper);

    @Mapping(target = "participantsIds", expression="java(getParticipantsIds(chat))")
    List<ChatDto> mapToDtoList(List<Chat> chats);

    default List<Long> getParticipantsIds(Chat chat) {
        return chat.getParticipants().stream().map(participant -> participant.getId()).collect(Collectors.toList());
    }

    default List<AppUserDto> mapParticipantsToDtoList(List<AppUser> participants, AppUserMapper appUserMapper) {
        return appUserMapper.mapToDtoList(participants);
    }


    @Mapping(target = "id", ignore = true) // Ignore mapping the ID during updates
    void updateChatFromDto(ChatDto dto, @MappingTarget Chat chat);
}