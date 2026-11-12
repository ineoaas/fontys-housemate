package org.example.housematesolution.Business.domain.rotation;

import org.example.housematesolution.Business.domain.Chore;
import org.example.housematesolution.Business.domain.ChoreCompletion;
import org.example.housematesolution.Business.domain.User;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

// Variant A: the member after the last person who did this chore, in join order
public class StrictRoundRobinRule implements ChoreRotationRule {

    @Override
    public String name() {
        return "A strict round robin";
    }

    @Override
    public User nextAssignee(List<User> members, Chore chore, List<ChoreCompletion> completions) {
        List<User> order = RotationLoads.inJoinOrder(members);

        Optional<ChoreCompletion> last = completions.stream()
                .filter(RotationLoads.sameChore(chore))
                .filter(completion -> order.stream().anyMatch(member -> RotationLoads.doneBy(completion, member)))
                .max(Comparator.comparing(ChoreCompletion::getCompletedAt)
                        .thenComparing(completion -> completion.getDoneBy().getId()));

        if (last.isEmpty()) {
            return order.get(0);
        }

        for (int i = 0; i < order.size(); i++) {
            if (RotationLoads.doneBy(last.get(), order.get(i))) {
                return order.get((i + 1) % order.size());
            }
        }
        throw new IllegalStateException("Last doer is not a member");
    }
}
