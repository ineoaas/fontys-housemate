package org.example.housematesolution.Persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import org.example.housematesolution.Business.domain.Chore;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "chores")
public class ChoreEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int intervalDays;

    @Column(nullable = false)
    private int effort;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "house_id")
    private HouseEntity house;

    public ChoreEntity(UUID id, String name, int intervalDays, int effort,
                       LocalDateTime createdAt, HouseEntity house) {
        this.id = id;
        this.name = name;
        this.intervalDays = intervalDays;
        this.effort = effort;
        this.createdAt = createdAt;
        this.house = house;
    }

    // JPA needs an empty constructor to create objects from database rows
    protected ChoreEntity() {
    }

    public static ChoreEntity fromDomain(Chore chore) {
        HouseEntity houseEntity = HouseEntity.fromDomain(chore.getHouse());
        return new ChoreEntity(chore.getId(), chore.getName(), chore.getIntervalDays(),
                chore.getEffort(), chore.getCreatedAt(), houseEntity);
    }

    public Chore toDomain() {
        return new Chore(id, name, intervalDays, effort, createdAt, house.toDomain());
    }

}