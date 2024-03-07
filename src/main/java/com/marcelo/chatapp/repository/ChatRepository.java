package com.marcelo.chatapp.repository;

import com.marcelo.chatapp.model.Chat;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    @EntityGraph(attributePaths = "participants")
    Optional<Chat> findById(Long Id);
    Optional<Chat> findByName(String name);
    @EntityGraph(attributePaths = "participants")
    @Query("SELECT c FROM Chat c JOIN c.participants p WHERE p.id = :userId")
    List<Chat> findAllChatsByUserId(@Param("userId") Long userId);
}
