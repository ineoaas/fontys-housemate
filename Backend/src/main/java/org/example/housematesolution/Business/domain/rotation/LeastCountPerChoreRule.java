package org.example.housematesolution.Business.domain.rotation;

import org.example.housematesolution.Business.domain.Chore;
import org.example.housematesolution.Business.domain.ChoreCompletion;
import org.example.housematesolution.Business.domain.User;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

// Variant B: the member with the fewest completions of THIS chore
public class LeastCountPerChoreRule implements ChoreRotationRule {

    @Override
    public String name() {
        return "B least count per chore";
    }

    @Override
    public User nextAssignee(List<User> members, Chore chore, List<ChoreCompletion> completions) {
        List<User> order = RotationLoads.inJoinOrder(members);
        Map<UUID, Integer> choreCount = RotationLoads.baselinedLoads(
                order, completions, RotationLoads.sameChore(chore), completion -> 1);

        Comparator<User> nextFirst = Comparator
                .comparingInt((User member) -> choreCount.get(member.getId()))
                .thenComparing(RotationLoads.tieBreaks(order, chore, completions));

        return order.stream().min(nextFirst).orElseThrow();
    }
}
