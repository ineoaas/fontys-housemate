package org.example.housematesolution.Business.domain;

import java.util.UUID;

public class User {

    private UUID id;
    private String name;
    private String email;
    private House house;

    public User(UUID id, String name, String email, House house) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.house = house;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public House getHouse() {
        return house;
    }
}
