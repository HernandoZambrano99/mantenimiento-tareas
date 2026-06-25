package com.empresa.mantenimiento.domain.exception;

public class DomainConflictException extends RuntimeException {
    public DomainConflictException(String message) {
        super(message);
    }
}
