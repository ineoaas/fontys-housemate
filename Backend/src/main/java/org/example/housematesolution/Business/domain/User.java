package org.example.housematesolution.Business.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class User {

    private UUID id;
    private String name;
    private String email;
    private House house;
    private LocalDateTime joinedAt;
    private LocalDateTime movedOutAt; // null while they still live in the house

    public User(UUID id, String name, String email, House house, LocalDateTime joinedAt, LocalDateTime movedOutAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.house = house;
        this.joinedAt = joinedAt;
        this.movedOutAt = movedOutAt;
    }

    // Kept until the database stores joinedAt and movedOutAt
    public User(UUID id, String name, String email, House house) {
        this(id, name, email, house, null, null);
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

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public LocalDateTime getMovedOutAt() {
        return movedOutAt;
    }
}
