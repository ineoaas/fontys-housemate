package org.example.housematesolution.Persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.example.housematesolution.Business.domain.House;

import java.util.UUID;

@Entity
@Table(name = "houses")
public class HouseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String joinCode;

    // JPA needs an empty constructor to create objects from database rows
    protected HouseEntity() {
    }

    public HouseEntity(UUID id, String name, String joinCode) {
        this.id = id;
        this.name = name;
        this.joinCode = joinCode;
    }

    public static HouseEntity fromDomain(House house) {
        return new HouseEntity(house.getId(), house.getName(), house.getJoinCode());
    }

    public House toDomain() {
        return new House(id, name, joinCode);
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
