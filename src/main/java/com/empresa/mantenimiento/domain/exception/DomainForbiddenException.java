package com.empresa.mantenimiento.domain.exception;

public class DomainForbiddenException extends RuntimeException {
    public DomainForbiddenException(String message) {
        super(message);
    }
}
