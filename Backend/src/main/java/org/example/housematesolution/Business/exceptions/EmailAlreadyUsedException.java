package org.example.housematesolution.Business.exceptions;

public class EmailAlreadyUsedException extends RuntimeException {

    public EmailAlreadyUsedException(String email) {
        super("Email is already in use: " + email);
    }
}
