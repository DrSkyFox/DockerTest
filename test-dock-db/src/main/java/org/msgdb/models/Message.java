package org.msgdb.models;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Builder(toBuilder = true)
@Getter
@Setter(value = AccessLevel.PUBLIC)
@Entity
@Table(name = "proposal")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message", nullable = false, length = 128)
    private String message;

    @Column(name = "created", nullable = false)
    private Date created;

    public Message(Long id, String message, Date created) {
        this.id = id;
        this.message = message;
        this.created = created;
    }

    public Message() {
    }
}
