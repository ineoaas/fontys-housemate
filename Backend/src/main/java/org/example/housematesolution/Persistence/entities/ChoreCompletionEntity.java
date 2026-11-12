package org.example.housematesolution.Persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import org.example.housematesolution.Business.domain.ChoreCompletion;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
// The index makes "all completions of a chore, oldest first" fast
@Table(name = "chore_completions",
        indexes = @Index(name = "idx_chore_completions_chore_completed_at", columnList = "chore_id, completed_at"))
public class ChoreCompletionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private LocalDateTime completedAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "chore_id")
    private ChoreEntity chore;

    @ManyToOne(optional = false)
    @JoinColumn(name = "house_id")
    private HouseEntity house;

    // Nullable: chores aren't assigned to anyone yet
    @ManyToOne
    @JoinColumn(name = "assigned_to_id")
    private UserEntity assignedTo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "done_by_id")
    private UserEntity doneBy;

    // JPA needs an empty constructor to create objects from database rows
    protected ChoreCompletionEntity() {
    }

    public ChoreCompletionEntity(UUID id, LocalDateTime completedAt, ChoreEntity chore, HouseEntity house,
                                 UserEntity assignedTo, UserEntity doneBy) {
        this.id = id;
        this.completedAt = completedAt;
        this.chore = chore;
        this.house = house;
        this.assignedTo = assignedTo;
        this.doneBy = doneBy;
    }

    public static ChoreCompletionEntity fromDomain(ChoreCompletion completion) {
        UserEntity assignedTo = completion.getAssignedTo() == null
                ? null
                : UserEntity.fromDomain(completion.getAssignedTo());
        return new ChoreCompletionEntity(
                completion.getId(),
                completion.getCompletedAt(),
                ChoreEntity.fromDomain(completion.getChore()),
                HouseEntity.fromDomain(completion.getHouse()),
                assignedTo,
                UserEntity.fromDomain(completion.getDoneBy()));
    }

    public ChoreCompletion toDomain() {
        return new ChoreCompletion(
                id,
                completedAt,
                chore.toDomain(),
                house.toDomain(),
                assignedTo == null ? null : assignedTo.toDomain(),
                doneBy.toDomain());
    }

}
