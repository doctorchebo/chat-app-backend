package com.marcelo.chatapp.mapper;

import com.marcelo.chatapp.model.AppUser;
import com.marcelo.chatapp.model.UserContact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserContactMapper {
    @Mapping(target="id", ignore=true)
    @Mapping(target="user", source="user")
    @Mapping(target="contact", source="contact")
    UserContact map(AppUser contact, AppUser user);

}
