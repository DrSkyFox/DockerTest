package org.messager.dao;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.msgdb.models.Message;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Builder(toBuilder = true)
@Getter
@Setter(value = AccessLevel.PUBLIC)
public class MessageDAO {

    private Long id;

    @NotEmpty
    private String message;

    private Date created;

    public MessageDAO(Message message) {
        this.id = message.getId();
        this.message = message.getMessage();
        this.created = message.getCreated();
    }

    public MessageDAO(Long id, String message, Date created) {
        this.id = id;
        this.message = message;
        this.created = created;
    }

    public MessageDAO() {
    }



}
