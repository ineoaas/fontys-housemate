package org.example.housematesolution.Business.domain.rotation;

import org.example.housematesolution.Business.domain.Chore;
import org.example.housematesolution.Business.domain.ChoreCompletion;
import org.example.housematesolution.Business.domain.User;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

// Domain classes have no equals(), so members and chores are matched on id
final class RotationLoads {

    private RotationLoads() {
    }

    static final Comparator<User> JOIN_ORDER =
            Comparator.comparing(User::getJoinedAt).thenComparing(User::getId);

    static List<User> inJoinOrder(List<User> members) {
        if (members == null || members.isEmpty()) {
            throw new IllegalArgumentException("A house needs at least one member to assign a chore");
        }
        return members.stream().sorted(JOIN_ORDER).toList();
    }

    static Predicate<ChoreCompletion> sameChore(Chore chore) {
        return completion -> completion.getChore().getId().equals(chore.getId());
    }

    static boolean doneBy(ChoreCompletion completion, User member) {
        return completion.getDoneBy().getId().equals(member.getId());
    }

    // Never done returns LocalDateTime.MIN, so it sorts as longest ago
    static LocalDateTime lastDone(User member, Chore chore, List<ChoreCompletion> completions) {
        return completions.stream()
                .filter(sameChore(chore))
                .filter(completion -> doneBy(completion, member))
                .map(ChoreCompletion::getCompletedAt)
                .max(Comparator.naturalOrder())
                .orElse(LocalDateTime.MIN);
    }

    // Tie-break chain shared by B and C. Reorder the lines to change the priority.
    static Comparator<User> tieBreaks(List<User> membersInJoinOrder, Chore chore, List<ChoreCompletion> completions) {
        Map<UUID, Integer> totalCount = baselinedLoads(membersInJoinOrder, completions, completion -> true, completion -> 1);
        return Comparator
                .comparingInt((User member) -> totalCount.get(member.getId()))
                .thenComparing(member -> lastDone(member, chore, completions))
                .thenComparing(User::getJoinedAt)
                .thenComparing(User::getId);
    }

    // Load per member id: own weighted completions plus a newcomer baseline.
    // counts says which completions count, weight what one is worth. For example:
    //   count of this chore: baselinedLoads(order, completions, sameChore(chore), completion -> 1)
    //   effort of all chores: baselinedLoads(order, completions, completion -> true, completion -> completion.getChore().getEffort())
    // Baseline: the lowest load among earlier joiners at the moment the member joined (founders start at 0),
    // so a late joiner is not handed the same chore many times in a row.
    // Walking in join order means the baselines of earlier members are already known.
    static Map<UUID, Integer> baselinedLoads(List<User> membersInJoinOrder,
                                             List<ChoreCompletion> completions,
                                             Predicate<ChoreCompletion> counts,
                                             ToIntFunction<ChoreCompletion> weight) {
        Map<UUID, Integer> baselines = new HashMap<>();
        for (User member : membersInJoinOrder) {
            int lowest = Integer.MAX_VALUE;
            for (User earlier : membersInJoinOrder) {
                if (!earlier.getJoinedAt().isBefore(member.getJoinedAt())) {
                    continue;
                }
                int loadWhenMemberJoined = baselines.get(earlier.getId())
                        + ownLoad(earlier, completions, counts, weight, member.getJoinedAt());
                lowest = Math.min(lowest, loadWhenMemberJoined);
            }
            baselines.put(member.getId(), lowest == Integer.MAX_VALUE ? 0 : lowest);
        }

        Map<UUID, Integer> loads = new HashMap<>();
        for (User member : membersInJoinOrder) {
            loads.put(member.getId(),
                    baselines.get(member.getId()) + ownLoad(member, completions, counts, weight, null));
        }
        return loads;
    }

    private static int ownLoad(User member, List<ChoreCompletion> completions,
                               Predicate<ChoreCompletion> counts, ToIntFunction<ChoreCompletion> weight,
                               LocalDateTime before) {
        return completions.stream()
                .filter(counts)
                .filter(completion -> doneBy(completion, member))
                .filter(completion -> before == null || completion.getCompletedAt().isBefore(before))
                .mapToInt(weight)
                .sum();
    }
}
