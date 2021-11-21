package org.msgdb.repositories;

import org.msgdb.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAll();

    Optional<Message> findById(Long id);

    void deleteById(Long id);



}
