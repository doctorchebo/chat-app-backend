package com.marcelo.chatapp.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class AppUserDto {
    private Long id;
    private String username;
    private String email;
    private Instant created; // Use String or specific Date/Time format
    private boolean enabled;
}