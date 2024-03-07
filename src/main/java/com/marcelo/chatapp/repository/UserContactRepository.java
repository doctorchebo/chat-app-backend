package com.marcelo.chatapp.repository;

import com.marcelo.chatapp.model.AppUser;
import com.marcelo.chatapp.model.UserContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserContactRepository extends JpaRepository<UserContact, Long> {
    List<UserContact> findByUser(AppUser user);
    @Query("SELECT uc FROM UserContact uc WHERE uc.user.id = :userId")
    List<UserContact> findByUserId(@Param("userId") Long userId);

    List<UserContact> findAllByIdIn(List<Long> id);

    List<UserContact> findAllByUser(AppUser appuser);

    // Method to delete contacts by user ID and contact ID
    @Modifying
    @Query("DELETE FROM UserContact uc WHERE uc.user.id = :userId AND uc.contact.id = :contactId")
    void deleteByUserIdAndContactId(@Param("userId") Long userId, @Param("contactId") Long contactId);

}
