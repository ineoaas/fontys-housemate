package org.example.housematesolution.Presentation.dtos;

import org.example.housematesolution.Business.domain.Chore;
import java.time.LocalDateTime;
import java.util.UUID;

public record ChoreResponse(UUID id, String name, int intervalDays, int effort, LocalDateTime createdAt) {
    public static ChoreResponse fromDomain(Chore chore) {
        return new ChoreResponse(chore.getId(), chore.getName(),
                chore.getIntervalDays(), chore.getEffort(), chore.getCreatedAt());
    }
}