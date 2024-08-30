package org.user.user.exception;

public class AuthorizationException extends RuntimeException{
    public AuthorizationException(String message){
        super(message);
    }
}
