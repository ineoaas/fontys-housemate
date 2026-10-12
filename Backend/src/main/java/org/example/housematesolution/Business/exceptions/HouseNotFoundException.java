package org.example.housematesolution.Business.exceptions;

public class HouseNotFoundException extends RuntimeException {

    public HouseNotFoundException(String joinCode) {
        super("No house found with join code: " + joinCode);
    }
}
