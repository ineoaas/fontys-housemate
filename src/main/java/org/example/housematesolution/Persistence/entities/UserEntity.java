package org.example.housematesolution.Persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.example.housematesolution.Business.domain.User;

import java.util.UUID;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    // Many users can live in one house
    @ManyToOne(optional = false)
    @JoinColumn(name = "house_id")
    private HouseEntity house;

    // JPA needs an empty constructor to create objects from database rows
    protected UserEntity() {
    }

    public UserEntity(UUID id, String name, String email, HouseEntity house) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.house = house;
    }

    public static UserEntity fromDomain(User user) {
        HouseEntity houseEntity = HouseEntity.fromDomain(user.getHouse());
        return new UserEntity(user.getId(), user.getName(), user.getEmail(), houseEntity);
    }

    public User toDomain() {
        return new User(id, name, email, house.toDomain());
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

    public HouseEntity getHouse() {
        return house;
    }
}
