package com.marcelo.chatapp.dto;

import lombok.Data;

import java.util.List;

@Data
public class ContactRequest {
    Long userId;
    List<Long> contactIds;
}
