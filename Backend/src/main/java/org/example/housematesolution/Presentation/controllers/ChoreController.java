package org.example.housematesolution.Presentation.controllers;

import jakarta.validation.Valid;
import org.example.housematesolution.Business.domain.Chore;
import org.example.housematesolution.Business.services.ChoreService;
import org.example.housematesolution.Presentation.dtos.ChoreResponse;
import org.example.housematesolution.Presentation.dtos.CreateChoreRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/houses/{houseId}/chores")
public class ChoreController {

    private final ChoreService choreService;

    public ChoreController(ChoreService choreService) {
        this.choreService = choreService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ChoreResponse createChore(@PathVariable UUID houseId,
                                     @Valid @RequestBody CreateChoreRequest request) {
        Chore chore = choreService.createChore(
                request.name(), request.intervalDays(), request.effort(), houseId);
        return ChoreResponse.fromDomain(chore);
    }

    @GetMapping
    public List<ChoreResponse> getChores(@PathVariable UUID houseId) {
        List<Chore> chores = choreService.getChores(houseId);

        List<ChoreResponse> response = new ArrayList<>();
        for (Chore chore : chores) {
            response.add(ChoreResponse.fromDomain(chore));
        }
        return response;
    }
}