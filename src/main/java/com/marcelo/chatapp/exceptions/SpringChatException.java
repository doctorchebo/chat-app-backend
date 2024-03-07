package com.marcelo.chatapp.exceptions;

public class SpringChatException extends RuntimeException {
    public SpringChatException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public SpringChatException(String exMessage) {
        super(exMessage);
    }
}
