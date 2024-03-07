package com.marcelo.chatapp.service;

import com.marcelo.chatapp.dto.AppUserDto;
import com.marcelo.chatapp.dto.ContactRequest;
import com.marcelo.chatapp.dto.DeleteContactRequest;
import com.marcelo.chatapp.dto.DeletedContactsResponse;
import com.marcelo.chatapp.exceptions.SpringChatException;
import com.marcelo.chatapp.mapper.AppUserMapper;
import com.marcelo.chatapp.mapper.UserContactMapper;
import com.marcelo.chatapp.model.AppUser;
import com.marcelo.chatapp.model.UserContact;
import com.marcelo.chatapp.repository.UserContactRepository;
import com.marcelo.chatapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class ContactService {

    private final AppUserMapper appUserMapper;
    private final UserContactRepository userContactRepository;
    private final UserRepository userRepository;

    public List<AppUserDto> getAllContacts(Long userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new SpringChatException("User with id " + userId.toString() + " not found"));

        List<UserContact> userContacts = userContactRepository.findByUser(user);
        List<AppUser> contacts = userContacts.stream()
                .map(UserContact::getContact)
                .collect(Collectors.toList());
        // We return a list of users, so we can use user id's instead of contact id's to send messages
        return appUserMapper.mapToDtoList(contacts);
    }
    public List<AppUserDto> addContacts(ContactRequest contactRequest) {
        AppUser user = userRepository.findById(contactRequest.getUserId())
                .orElseThrow(() -> new SpringChatException("User with id " + contactRequest.getUserId().toString() + " not found"));

        List<UserContact> existingContacts = user.getContacts();
        List<AppUser> newContacts = userRepository.findAllByIdIn(contactRequest.getContactIds());

        List<AppUser> newContactsToAdd = newContacts.stream()
                .filter(newContact ->
                        existingContacts.stream()
                                .noneMatch(existingContact -> existingContact.getContact().getId().equals(newContact.getId()))
                )
                .collect(Collectors.toList());

        for (AppUser newContact : newContactsToAdd) {
            // Create a new UserContact and set both sides of the relationship
            UserContact userContact = new UserContact();
            userContact.setUser(user);
            userContact.setContact(newContact);

            existingContacts.add(userContact);
        }

        user.setContacts(existingContacts);
        userRepository.save(user);
        return appUserMapper.mapToDtoList(newContactsToAdd);
    }


    public DeletedContactsResponse deleteContactsForUser(DeleteContactRequest deleteContactRequest) {

        AppUser user = userRepository.findById(deleteContactRequest.getUserId())
                .orElseThrow(() -> new SpringChatException("User with id " + deleteContactRequest.getUserId() + " not found"));

        List<UserContact> contacts = user.getContacts();

        List<Long> usersIds = contacts.stream().map(contact -> contact.getContact().getId()).collect(Collectors.toList());

        List<Long> deletedContactIds = new ArrayList<>();
        for (Long contactId : deleteContactRequest.getContactsIds()) {
            if(usersIds.contains(contactId)){
                userContactRepository.deleteByUserIdAndContactId(deleteContactRequest.getUserId(), contactId);
                deletedContactIds.add(contactId);
            }
        }
        DeletedContactsResponse deletedContactsResponse = new DeletedContactsResponse(deletedContactIds);
        return deletedContactsResponse;
    }

}
