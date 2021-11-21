package org.messager.implementations;


import org.messager.exceptions.MessageNotExists;
import org.messager.dao.MessageDAO;
import org.msgdb.models.Message;
import org.msgdb.repositories.MessageRepository;
import org.messager.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageService implements IMessageService {

    private final MessageRepository repository;

    @Autowired
    public MessageService(MessageRepository repository) {
        this.repository = repository;
    }

    @Override
    public void createNewMessage(MessageDAO messageDAO) {
        repository.save(Message.builder()
                        .message(messageDAO.getMessage())
                        .id(messageDAO.getId())
                        .created(Calendar.getInstance().getTime())
                .build());
    }

    @Override
    public List<MessageDAO> getListOfMessagesByPage(Integer page, Integer size, Optional<String> sortDirection, Optional<String> sort) {
        PageRequest pageRequest;
        if(sortDirection.isPresent() && sort.isPresent()) {
            pageRequest = PageRequest.of(page, size, Sort.Direction.fromString(sortDirection.get()), sort.get());
        } else {
            pageRequest = PageRequest.of(page, size);
        }
        return repository.findAll(pageRequest).stream().map(MessageDAO::new).collect(Collectors.toList());
    }

    @Override
    public List<MessageDAO> getListOfMessagesSorted(String sortDirection, String sort) {
        return null;
    }

    @Override
    public List<MessageDAO> getListOfMessages() {
        return repository.findAll().stream().map(MessageDAO::new).collect(Collectors.toList());
    }

    @Override
    public void deleteMessage(Long id) {
        if(repository.findById(id).isPresent()) {
            repository.deleteById(id);
        } else throw new MessageNotExists("Message with id: " + id + " not exists");
    }

    @Override
    public MessageDAO getMessageById(Long id) {
        return repository.findById(id).map(MessageDAO::new).orElse(null);
    }

    @Override
    public MessageDAO setMessage(MessageDAO message) {
        var messageDB = repository.findById(message.getId());
        if(messageDB.isPresent()) {
            throw new MessageNotExists("Message with id: " +  message.getId() + " - not found");
        }
        messageDB.get().setMessage(message.getMessage());
        return new MessageDAO(repository.save(messageDB.get()));
    }





}
