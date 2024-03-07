package com.marcelo.chatapp.dto;

import lombok.Data;

import java.util.List;

@Data
public class DeleteUsersRequest {
    List<Long> userIds;
}
