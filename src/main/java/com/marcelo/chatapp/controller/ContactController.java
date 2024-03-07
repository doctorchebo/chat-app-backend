package com.marcelo.chatapp.controller;

import com.marcelo.chatapp.dto.AppUserDto;
import com.marcelo.chatapp.dto.ContactRequest;
import com.marcelo.chatapp.dto.DeleteContactRequest;
import com.marcelo.chatapp.dto.DeletedContactsResponse;
import com.marcelo.chatapp.service.ContactService;
import com.marcelo.chatapp.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/contact")
@AllArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @GetMapping("/getAllContacts/{userId}")
    public List<AppUserDto> getAllContacts(@PathVariable Long userId){
        return contactService.getAllContacts(userId);
    }

    @PostMapping("/addContacts")
    public List<AppUserDto> addContact(@RequestBody ContactRequest contactRequest){
        return contactService.addContacts(contactRequest);
    }

    @DeleteMapping("/deleteContacts")
    public ResponseEntity<DeletedContactsResponse> deleteContacts (@RequestBody DeleteContactRequest deleteContactsRequest){
        DeletedContactsResponse deletedContactsResponse = contactService.deleteContactsForUser(deleteContactsRequest);
        return ResponseEntity.ok(deletedContactsResponse);
    }
}
