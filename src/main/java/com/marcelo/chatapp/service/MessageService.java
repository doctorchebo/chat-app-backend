package com.marcelo.chatapp.service;

import com.marcelo.chatapp.dto.MessageDto;
import com.marcelo.chatapp.exceptions.SpringChatException;
import com.marcelo.chatapp.mapper.ChatMapper;
import com.marcelo.chatapp.mapper.MessageMapper;
import com.marcelo.chatapp.model.*;
import com.marcelo.chatapp.repository.ChatRepository;
import com.marcelo.chatapp.repository.MessageRepository;
import com.marcelo.chatapp.repository.UserContactRepository;
import com.marcelo.chatapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MessageService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final AuthService authService;
    private final UserRepository userRepository;

    @Transactional
    public MessageDto sendMessage(MessageDto messageDto) {
        List<AppUser> recipients = userRepository.findAllByIdIn(messageDto.getRecipientIds());
        AppUser currentUser = authService.getCurrentUser();
        Chat chat;

        if (messageDto.getChatId() != null) {
            chat = chatRepository.findById(messageDto.getChatId())
                    .orElseThrow(() -> new SpringChatException("Chat not found"));
        } else {
            chat = new Chat();

            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String formattedDate = dateFormat.format(currentDate);

            chat.setName(currentUser.getUsername() + " - " + formattedDate);
            chat.setType(ChatType.PRIVATE);
            chat.setCreated(new Date());
            List<AppUser> participants = new ArrayList<>(recipients);
            participants.add(currentUser);
            System.out.println("participants" + participants);
            chat.setParticipants(participants);
            chat.setChatOwnerId(currentUser.getId());
            chat = chatRepository.save(chat);

            for (AppUser participant : participants) {
                participant.getChats().add(chat); // Update the user's list of chats
            }
        }

        // Create a new message associated with the chat and current user
        Message savedMessage = messageRepository.save(
                messageMapper.map(messageDto, chat, currentUser)
        );
        savedMessage.setRecipients(recipients);
        messageRepository.save(savedMessage);
        return messageMapper.mapToDto(savedMessage);
    }

    public MessageDto deleteMessage(Long messageId){
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new SpringChatException("Message with id " + messageId + " not found"));
        messageRepository.deleteById(message.getId());
        return messageMapper.mapToDto(message);
    }

    public List<MessageDto> getAllMessagesByChatId(Long chatId){
        List<Message> messages = messageRepository.findAllByChatIdOrderByCreatedAsc(chatId);
        return messageMapper.mapToDtoList(messages);
    }
    public MessageDto editMessage(MessageDto messageDto){
        Message message = messageRepository.findById(messageDto.getId())
                .orElseThrow(() -> new SpringChatException("Message with id " + messageDto.getId() + " not found"));
        message.setContent(messageDto.getContent());
        messageRepository.save(message);
        return messageMapper.mapToDto(message);
    }

    public List<MessageDto> getLastMessagesForChats(){
        List<Chat> chats = chatRepository.findAllByUserId(authService.getCurrentUser().getId());
        List<Message> messages = new ArrayList<>();
        for(Chat chat : chats){
            Message message = messageRepository.findTopByChatIdOrderByCreatedDesc(chat.getId())
                    .orElseThrow(() -> new SpringChatException("Last message for chat with id " + chat.getId() + " not found"));
            messages.add(message);
        }
        return messageMapper.mapToDtoList(messages);
    }
}
