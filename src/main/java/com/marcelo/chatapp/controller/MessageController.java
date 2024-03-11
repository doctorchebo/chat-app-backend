package com.marcelo.chatapp.controller;

import com.marcelo.chatapp.dto.MessageDto;
import com.marcelo.chatapp.dto.MessageResponse;
import com.marcelo.chatapp.model.Message;
import com.marcelo.chatapp.repository.MessageRepository;
import com.marcelo.chatapp.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/message")
@AllArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @PostMapping("/send")
    ResponseEntity<MessageDto> sendMessage(@RequestBody MessageDto messageDto){
        MessageDto messageResponseDto = messageService.sendMessage(messageDto);
        return ResponseEntity.ok(messageResponseDto);
    }

    @DeleteMapping("/delete/{messageId}")
    ResponseEntity<MessageDto> deleteMessage(@PathVariable Long messageId){
        MessageDto messageDto = messageService.deleteMessage(messageId);
        return ResponseEntity.ok(messageDto);
    }

    @GetMapping("/getAllMessagesByChatId/{chatId}")
    List<MessageDto> getAllMessagesByChatId(@PathVariable Long chatId){
        List<MessageDto> messages = messageService.getAllMessagesByChatId(chatId);
        return messages;
    }

    @PatchMapping("/edit")
    ResponseEntity<MessageDto> editMessage(@RequestBody MessageDto messageDto){
        MessageDto messageResponseDto = messageService.editMessage(messageDto);
        return ResponseEntity.ok(messageResponseDto);
    }

    @GetMapping("/getLastMessagesForChats")
    public ResponseEntity<List<MessageDto>> getLastMessagesForChats(){
        return ResponseEntity.ok(messageService.getLastMessagesForChats());
    }



}
