package org.messager.exceptions;

public class MessageNotExists extends RuntimeException{

    public MessageNotExists() {
    }

    public MessageNotExists(String message) {
        super(message);
    }


}
