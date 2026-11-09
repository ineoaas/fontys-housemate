package org.example.housematesolution.Business.exceptions;

public class ChoreAlreadyExistsException extends RuntimeException {
    public ChoreAlreadyExistsException(String Chore) {
        super("Chore already exists: " + Chore);
    }
}
