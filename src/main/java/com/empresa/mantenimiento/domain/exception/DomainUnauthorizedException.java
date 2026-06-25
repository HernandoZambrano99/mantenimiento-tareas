package com.empresa.mantenimiento.domain.exception;

public class DomainUnauthorizedException extends RuntimeException {
    public DomainUnauthorizedException(String message) {
        super(message);
    }
}
