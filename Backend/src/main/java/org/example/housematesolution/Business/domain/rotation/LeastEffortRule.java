package org.example.housematesolution.Business.domain.rotation;

import org.example.housematesolution.Business.domain.Chore;
import org.example.housematesolution.Business.domain.ChoreCompletion;
import org.example.housematesolution.Business.domain.User;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

// Variant C: the member with the lowest sum of effort points across all chores
public class LeastEffortRule implements ChoreRotationRule {

    @Override
    public String name() {
        return "C least effort";
    }

    @Override
    public User nextAssignee(List<User> members, Chore chore, List<ChoreCompletion> completions) {
        List<User> order = RotationLoads.inJoinOrder(members);
        Map<UUID, Integer> effortSum = RotationLoads.baselinedLoads(
                order, completions, completion -> true, completion -> completion.getChore().getEffort());

        Comparator<User> nextFirst = Comparator
                .comparingInt((User member) -> effortSum.get(member.getId()))
                .thenComparing(RotationLoads.tieBreaks(order, chore, completions));

        return order.stream().min(nextFirst).orElseThrow();
    }
}
