package org.example.housematesolution.Business.services;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

// Creates random join codes like "K7QX2M" that housemates can type in to join a house
@Component
public class JoinCodeGenerator {

    // No 0/O or 1/I, so the code is easy to read and type
    private static final String CHARACTERS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int CODE_LENGTH = 6;

    private final SecureRandom random = new SecureRandom();

    public String generate() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }
        return code.toString();
    }
}
