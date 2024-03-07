package com.marcelo.chatapp.controller;

import com.marcelo.chatapp.dto.AppUserDto;
import com.marcelo.chatapp.dto.ContactRequest;
import com.marcelo.chatapp.dto.DeleteUsersRequest;
import com.marcelo.chatapp.model.AppUser;
import com.marcelo.chatapp.model.UserContact;
import com.marcelo.chatapp.model.VerificationToken;
import com.marcelo.chatapp.repository.VerificationTokenRepository;
import com.marcelo.chatapp.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @DeleteMapping("/deleteUsers")
    public ResponseEntity<List<AppUserDto>> deleteUsers (@RequestBody DeleteUsersRequest deleteUsersRequest){
        List<AppUserDto> deletedUsersDto = userService.deleteUsers(deleteUsersRequest);
        return ResponseEntity.ok(deletedUsersDto);
    }

    @GetMapping("getAllUsers")
    public List<AppUserDto> getAllUsers (){
        return userService.getAllUsers();
    }

    @GetMapping("getCurrentUser")
    public AppUserDto getCurrentUser(){
        return userService.getCurrentUser();
    }
}
