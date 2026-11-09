package org.example.housematesolution.Business.domain;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Chore {

    private UUID id;
    private String name;
    private int intervalDays; // 7 = weekly
    private int effort; // 1 to 3
    private LocalDateTime createdAt;
    private House house;

    public Chore(UUID id, String name, int intervalDays, int effort, LocalDateTime createdAt, House house) {
        this.id = id;
        this.name = name;
        this.intervalDays = intervalDays;
        this.effort = effort;
        this.createdAt = createdAt;
        this.house = house;
    }
}
