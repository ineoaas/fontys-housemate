package org.example.housematesolution.Business.exceptions;

import java.util.UUID;

public class HouseNotFoundException extends RuntimeException {

    public HouseNotFoundException(String joinCode) {
        super("No house found with join code: " + joinCode);
    }
    public HouseNotFoundException(UUID id) {
        super("No house found with id: " + id);
    }
}
