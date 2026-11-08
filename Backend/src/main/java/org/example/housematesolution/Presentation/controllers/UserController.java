package org.example.housematesolution.Presentation.controllers;

import jakarta.validation.Valid;
import org.example.housematesolution.Business.domain.User;
import org.example.housematesolution.Business.services.HouseService;
import org.example.housematesolution.Presentation.dtos.JoinHouseRequest;
import org.example.housematesolution.Presentation.dtos.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final HouseService houseService;

    public UserController(HouseService houseService) {
        this.houseService = houseService;
    }

    // Joining a house creates a new user in that house.
    // The join code goes in the body, not the URL, so it doesn't end up in server logs.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse joinHouse(@Valid @RequestBody JoinHouseRequest request) {
        User user = houseService.joinHouse(request.joinCode(), request.name(), request.email());
        return UserResponse.fromDomain(user);
    }
}
