package com.uca.parcialfinalncapas.exceptions;

public class BadTicketRequestException extends RuntimeException {
    public BadTicketRequestException(String message) {
        super(message);
    }
}
