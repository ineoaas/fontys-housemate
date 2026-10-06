package org.example.housematesolution.Presentation.controllers;

import jakarta.validation.Valid;
import org.example.housematesolution.Business.domain.User;
import org.example.housematesolution.Business.services.HouseService;
import org.example.housematesolution.Presentation.dtos.JoinHouseRequest;
import org.example.housematesolution.Presentation.dtos.UserResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/houses")
public class HouseController {

    private final HouseService houseService;

    public HouseController(HouseService houseService) {
        this.houseService = houseService;
    }

    @PostMapping("/join")
    public UserResponse joinHouse(@Valid @RequestBody JoinHouseRequest request) {
        User user = houseService.joinHouse(request.joinCode(), request.name(), request.email());
        return UserResponse.fromDomain(user);
    }
}
