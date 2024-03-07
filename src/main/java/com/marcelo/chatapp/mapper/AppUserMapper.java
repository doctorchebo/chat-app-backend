package com.marcelo.chatapp.mapper;

import com.marcelo.chatapp.dto.AppUserDto;
import com.marcelo.chatapp.model.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AppUserMapper {

    AppUserDto 
    mapToDto(AppUser appUser);

    List<AppUserDto> mapToDtoList(List<AppUser> appUsers);
}
