package org.example.housematesolution.Presentation.dtos;

import org.example.housematesolution.Business.domain.User;
import java.util.UUID;

public record HousemateResponse(
        UUID id,
        String name
){
    public static HousemateResponse fromDomain(User user) {
        return new HousemateResponse(user.getId(), user.getName());
    }
}