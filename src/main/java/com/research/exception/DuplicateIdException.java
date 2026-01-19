package com.research.exception;

public class DuplicateIdException extends RuntimeException {
    public DuplicateIdException(String message, int id) {
        super(message);
    }
}
