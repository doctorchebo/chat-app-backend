package com.marcelo.chatapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteContactRequest {
    private Long userId;
    private List<Long> contactsIds;
}
