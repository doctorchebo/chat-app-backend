package com.marcelo.chatapp.controller;

import com.marcelo.chatapp.dto.ChatDto;
import com.marcelo.chatapp.model.Chat;
import com.marcelo.chatapp.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@AllArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @PatchMapping("/editName")
    public ResponseEntity<ChatDto> editChatName(@RequestBody ChatDto chatDto) {
        ChatDto newChatDto = chatService.editChatName(chatDto);
        return ResponseEntity.ok(newChatDto);
    }

    @PatchMapping("/editType")
    public ResponseEntity<ChatDto> editChatType(@RequestBody ChatDto chatDto){
        ChatDto newChatDto = chatService.editChatType(chatDto);
        return ResponseEntity.ok(newChatDto);
    }

    @DeleteMapping("/delete/{chatId}")
    public ResponseEntity<ChatDto> deleteChat(@PathVariable Long chatId){
        ChatDto chatDto = chatService.deleteChat(chatId);
        return ResponseEntity.ok(chatDto);
    }

    @GetMapping("/getAllForUser/{userId}")
    public ResponseEntity<List<ChatDto>> getAllChatsByUserId(@PathVariable Long userId){
        List<ChatDto> chatDtos = chatService.getAllChatsByUserId(userId);
        return ResponseEntity.ok(chatDtos);
    }

    @GetMapping("/getCurrentUserChats")
    public ResponseEntity<List<ChatDto>>getAllChatsForCurrentUser(){
        return ResponseEntity.ok(chatService.getAllChatsForCurrentUser());
    }
}
