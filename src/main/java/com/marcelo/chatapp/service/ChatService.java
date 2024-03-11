package com.marcelo.chatapp.service;

import com.marcelo.chatapp.dto.ChatDto;
import com.marcelo.chatapp.dto.ChatDtoDetailed;
import com.marcelo.chatapp.exceptions.SpringChatException;
import com.marcelo.chatapp.mapper.AppUserMapper;
import com.marcelo.chatapp.mapper.ChatMapper;
import com.marcelo.chatapp.mapper.MessageMapper;
import com.marcelo.chatapp.model.AppUser;
import com.marcelo.chatapp.model.Chat;
import com.marcelo.chatapp.model.Message;
import com.marcelo.chatapp.repository.ChatRepository;
import com.marcelo.chatapp.repository.MessageRepository;
import com.marcelo.chatapp.repository.UserRepository;
import com.nimbusds.jose.crypto.opts.UserAuthenticationRequired;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final UserService userService;
    private final MessageRepository messageRepository;
    private final AuthService authService;
    private final ChatMapper chatMapper;

    private final AppUserMapper appUserMapper;

    private final MessageMapper messageMapper;
    public void createChat(ChatDto chatDto){
        chatRepository.save(chatMapper.map(chatDto, userService));
    }

    public ChatDto editChatName(ChatDto chatDto) {
        Long chatId = chatDto.getId();
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new SpringChatException("Chat with id " + chatId + " not found"));

        chat.setName(chatDto.getName());
        chatRepository.save(chat);
        return chatMapper.mapToDto(chat);
    }

    public ChatDto editChatType(ChatDto chatDto) {
        Long chatId = chatDto.getId();
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new SpringChatException("Chat with id " + chatId + " not found"));

        chat.setType(chatDto.getType());
        chatRepository.save(chat);
        return chatMapper.mapToDto(chat);
    }

    public ChatDto deleteChat(Long chatId){
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new SpringChatException("Chat with id " + chatId.toString() + " not found"));

        List<Message> messages = messageRepository.findAllByChatIdOrderByCreatedAsc(chatId);
        messageRepository.deleteAll(messages);

        chatRepository.deleteById(chat.getId());

        return chatMapper.mapToDto(chat);
    }

    public List<ChatDto> getAllChatsByUserId(Long userId){
        List<Chat> chats = chatRepository.findAllByUserId(userId);
        return chatMapper.mapToDtoList(chats);
    }

    public List<ChatDto> getAllChatsForCurrentUser(){
        AppUser currentUser = authService.getCurrentUser();
        List<Chat> chats = chatRepository.findAllByUserId(currentUser.getId());
        return chatMapper.mapToDtoList(chats);
    }

    public List<ChatDtoDetailed> getAllChatsForCurrentUserDetailed(){
        AppUser currentUser = authService.getCurrentUser();
        List<Chat> chats = chatRepository.findAllByUserId(currentUser.getId());
        return chatMapper.mapToDtoListDetailed(chats, appUserMapper);
    }

    public ChatDto getChatDetails(Long chatId){
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new SpringChatException("Chat with id " + chatId.toString() + " not found"));
        return chatMapper.mapToDto(chat);
    }

    public ChatDtoDetailed getChatDetailsWithParticipantsData(Long chatId){
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new SpringChatException("Chat with id " + chatId.toString() + " not found"));
        return chatMapper.mapToDtoDetailed(chat, appUserMapper, messageMapper);
    }
}
