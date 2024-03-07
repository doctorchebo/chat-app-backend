package com.marcelo.chatapp.service;

import com.marcelo.chatapp.dto.AppUserDto;
import com.marcelo.chatapp.dto.ContactRequest;
import com.marcelo.chatapp.dto.DeleteContactRequest;
import com.marcelo.chatapp.dto.DeleteUsersRequest;
import com.marcelo.chatapp.exceptions.SpringChatException;
import com.marcelo.chatapp.mapper.AppUserMapper;
import com.marcelo.chatapp.mapper.UserContactMapper;
import com.marcelo.chatapp.model.AppUser;
import com.marcelo.chatapp.model.UserContact;
import com.marcelo.chatapp.repository.UserContactRepository;
import com.marcelo.chatapp.repository.UserRepository;
import com.marcelo.chatapp.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final AppUserMapper appUserMapper;
    private final AuthService authService;

    public List<AppUser> getUsersByIds(List<Long> userIds){
        return userRepository.findAllByIdIn(userIds);
    }

    public List<AppUserDto> deleteUsers (DeleteUsersRequest deleteUsersRequest){
        List<AppUser> users = userRepository.findAllByIdIn(deleteUsersRequest.getUserIds());
        for(AppUser user : users){
            verificationTokenRepository.deleteById(user.getId());
        }
        return appUserMapper.mapToDtoList(users);
    }

    public List<AppUserDto> getAllUsers(){
        return appUserMapper.mapToDtoList(userRepository.findAll());
    }

    public AppUserDto getCurrentUser (){
        AppUser currentUser = authService.getCurrentUser();
        return appUserMapper.mapToDto(currentUser);
    }

}
