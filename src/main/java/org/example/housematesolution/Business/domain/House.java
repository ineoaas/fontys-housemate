package org.example.housematesolution.Business.domain;

import java.util.UUID;

public class House {

    private UUID id;
    private String name;
    private String joinCode;

    public House(UUID id, String name, String joinCode) {
        this.id = id;
        this.name = name;
        this.joinCode = joinCode;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getJoinCode() {
        return joinCode;
    }
}
