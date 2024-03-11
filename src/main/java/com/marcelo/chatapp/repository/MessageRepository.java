package com.marcelo.chatapp.repository;

import com.marcelo.chatapp.model.AppUser;
import com.marcelo.chatapp.model.Message;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @EntityGraph(attributePaths = "recipients")
    Optional<Message> findById(Long id);
    List<Message> findBySender(AppUser sender);
    @EntityGraph(attributePaths = "recipients")
    List<Message> findAllByChatIdOrderByCreatedAsc(Long id);
    @EntityGraph(attributePaths = "recipients")
    Optional<Message> findTopByChatIdOrderByCreatedDesc(Long chatId);
}
