package org.messager.service;


import org.messager.dao.MessageDAO;

import java.util.List;
import java.util.Optional;

public interface IMessageService {

    void createNewMessage(MessageDAO messageDAO);

    List<MessageDAO> getListOfMessages();

    List<MessageDAO> getListOfMessagesByPage(Integer page, Integer size, Optional<String> sortDirection, Optional<String> sort);

    List<MessageDAO> getListOfMessagesSorted(String sortDirection, String sort);

    void deleteMessage(Long id);

    MessageDAO getMessageById(Long id);

    MessageDAO setMessage(MessageDAO message);



}
