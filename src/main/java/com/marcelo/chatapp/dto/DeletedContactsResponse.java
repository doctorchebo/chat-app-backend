package com.marcelo.chatapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DeletedContactsResponse {
    private List<Long> contactIds;
}
