package com.research.exception;

public class NotFoundException extends RuntimeException {
  public NotFoundException(String entityName, int id) {
    super(String.format("%s with ID %d not found", entityName, id));
  }

  public NotFoundException(String entityName, String identifier) {
    super(String.format("%s with identifier '%s' not found", entityName, identifier));
  }
}
