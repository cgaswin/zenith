package org.event.event.exceptions;

public class EventItemNotFoundException extends RuntimeException{
    public EventItemNotFoundException(String message){
        super(message);
    }
}
