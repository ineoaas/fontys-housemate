package org.example.housematesolution.Business.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class ChoreCompletion {

    private UUID id;
    private LocalDateTime completedAt;
    private Chore chore;
    private House house;
    // Usually the same person; they differ when someone covers for the assignee
    private User assignedTo;
    private User doneBy;

    public ChoreCompletion(UUID id, LocalDateTime completedAt, Chore chore, House house, User assignedTo, User doneBy) {
        this.id = id;
        this.completedAt = completedAt;
        this.chore = chore;
        this.house = house;
        this.assignedTo = assignedTo;
        this.doneBy = doneBy;
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public Chore getChore() {
        return chore;
    }

    public House getHouse() {
        return house;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public User getDoneBy() {
        return doneBy;
    }
}
