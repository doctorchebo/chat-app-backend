package com.marcelo.chatapp.repository;

import com.marcelo.chatapp.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findById (Long id);
    Optional<AppUser> findByUsername(String username);

    List<AppUser> findAllByIdIn(List<Long> id);
}
