package org.event.event.exceptions;

public class EventRegistrationNotFoundException extends RuntimeException{
    public EventRegistrationNotFoundException(String message){
        super(message);
    }
}
