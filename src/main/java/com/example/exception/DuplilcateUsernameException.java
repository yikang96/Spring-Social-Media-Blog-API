package com.example.exception;

public class DuplilcateUsernameException extends RuntimeException{
    public DuplilcateUsernameException(String message){
        super(message);
    }
}
