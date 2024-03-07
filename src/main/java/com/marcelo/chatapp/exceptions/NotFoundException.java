package com.marcelo.chatapp.exceptions;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String exMessage, Exception exception){
        super(exMessage, exception);
    }
    public NotFoundException(String exMessage){
        super(exMessage);
    }
}
