package org.example.housematesolution.Presentation.dtos;

import org.example.housematesolution.Business.domain.User;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        UUID houseId,
        String houseName
) {

    public static UserResponse fromDomain(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getHouse().getId(),
                user.getHouse().getName()
        );
    }
}
