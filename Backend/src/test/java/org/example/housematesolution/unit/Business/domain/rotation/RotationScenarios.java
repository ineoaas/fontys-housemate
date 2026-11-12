package org.example.housematesolution.unit.Business.domain.rotation;

import org.example.housematesolution.Business.domain.Chore;
import org.example.housematesolution.Business.domain.ChoreCompletion;
import org.example.housematesolution.Business.domain.House;
import org.example.housematesolution.Business.domain.User;
import org.example.housematesolution.Business.domain.rotation.ChoreRotationRule;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.ToIntFunction;

// Shared data and scenarios, so the tests and the printed table use the same numbers
final class RotationScenarios {

    private RotationScenarios() {
    }

    static final LocalDateTime START = LocalDateTime.of(2026, 1, 1, 9, 0);

    static final House HOUSE = new House(UUID.fromString("00000000-0000-0000-0000-000000000100"), "Student House", "ABC123");

    // Ids are chosen so id order differs from join order
    static final User ANNA = member("00000000-0000-0000-0000-000000000003", "Anna", START);
    static final User BEN = member("00000000-0000-0000-0000-000000000002", "Ben", START.plusDays(1));
    static final User CEM = member("00000000-0000-0000-0000-000000000001", "Cem", START.plusDays(2));
    static final User DANA = member("00000000-0000-0000-0000-000000000004", "Dana", START.plusDays(3));

    static final List<User> FOUNDERS = List.of(ANNA, BEN, CEM);

    static final Chore DISHES = chore("00000000-0000-0000-0000-000000000201", "dishes", 2);
    static final Chore VACUUM = chore("00000000-0000-0000-0000-000000000202", "vacuum", 3);
    static final Chore TRASH = chore("00000000-0000-0000-0000-000000000203", "trash", 1);
    static final List<Chore> CHORES = List.of(DISHES, VACUUM, TRASH);

    static final ToIntFunction<ChoreCompletion> COUNT = completion -> 1;
    static final ToIntFunction<ChoreCompletion> EFFORT = completion -> completion.getChore().getEffort();

    // summary is only for the printed table, tests assert on the other fields
    record ScenarioResult(String summary, String picked, int newcomerTurns, int newcomerStreak,
                          int maxCountGap, int maxEffortGap) {
    }

    // ---------- Scenarios ----------

    static final List<String> SCENARIOS =
            List.of("tie on count", "cold start", "newcomer joins halfway", "cover for a housemate", "fairness over time");

    static ScenarioResult run(String scenario, ChoreRotationRule rule) {
        return switch (scenario) {
            case "tie on count" -> tieOnCount(rule);
            case "cold start" -> coldStart(rule);
            case "newcomer joins halfway" -> newcomerJoinsHalfway(rule);
            case "cover for a housemate" -> coverForHousemate(rule);
            case "fairness over time" -> fairnessOverTime(rule);
            default -> throw new IllegalArgumentException(scenario);
        };
    }

    static ScenarioResult tieOnCount(ChoreRotationRule rule) {
        Simulation sim = new Simulation(rule);
        sim.record(DISHES, ANNA);
        sim.record(DISHES, BEN);
        sim.record(DISHES, CEM);
        sim.record(VACUUM, ANNA);
        sim.record(VACUUM, CEM);

        User picked = sim.assign(FOUNDERS, DISHES);
        return new ScenarioResult(picked.getName() + " is next", picked.getName(), 0, 0,
                gap(FOUNDERS, sim.log, COUNT, null), gap(FOUNDERS, sim.log, EFFORT, null));
    }

    static ScenarioResult coldStart(ChoreRotationRule rule) {
        Simulation sim = new Simulation(rule);
        User picked = sim.assign(FOUNDERS, DISHES);
        return new ScenarioResult(picked.getName() + " is next", picked.getName(), 0, 0,
                gap(FOUNDERS, sim.log, COUNT, null), gap(FOUNDERS, sim.log, EFFORT, null));
    }

    // Gap counts only completions after Dana joined
    static ScenarioResult newcomerJoinsHalfway(ChoreRotationRule rule) {
        Simulation sim = new Simulation(rule);
        for (int i = 0; i < 30; i++) {
            sim.assign(FOUNDERS, CHORES.get(i % CHORES.size()));
        }

        LocalDateTime joined = sim.now.plusMinutes(1);
        User newcomer = member("00000000-0000-0000-0000-000000000005", "Dana", joined);
        List<User> house = List.of(ANNA, BEN, CEM, newcomer);

        int maxCountGap = 0;
        int maxEffortGap = 0;
        int newcomerTurns = 0;
        for (int i = 0; i < 30; i++) {
            if (sim.assign(house, CHORES.get(i % CHORES.size())) == newcomer) {
                newcomerTurns++;
            }
            maxCountGap = Math.max(maxCountGap, gap(house, sim.log, COUNT, joined));
            maxEffortGap = Math.max(maxEffortGap, gap(house, sim.log, EFFORT, joined));
        }

        int streak = longestSameChoreStreak(sim.log, newcomer);
        return new ScenarioResult("Dana did " + newcomerTurns + "/30, same chore in a row max " + streak,
                null, newcomerTurns, streak, maxCountGap, maxEffortGap);
    }

    // Gap is measured after the rule-driven picks that follow the cover
    static ScenarioResult coverForHousemate(ChoreRotationRule rule) {
        Simulation sim = new Simulation(rule);
        sim.record(DISHES, ANNA);
        sim.record(DISHES, BEN);
        sim.record(DISHES, CEM, ANNA); // assigned to Cem, done by Anna

        User next = null;
        int maxCountGap = 0;
        int maxEffortGap = 0;
        for (int i = 0; i < 10; i++) {
            User picked = sim.assign(FOUNDERS, DISHES);
            if (next == null) {
                next = picked;
            }
            maxCountGap = Math.max(maxCountGap, gap(FOUNDERS, sim.log, COUNT, null));
            maxEffortGap = Math.max(maxEffortGap, gap(FOUNDERS, sim.log, EFFORT, null));
        }
        return new ScenarioResult(next.getName() + " is next", next.getName(), 0, 0, maxCountGap, maxEffortGap);
    }

    static ScenarioResult fairnessOverTime(ChoreRotationRule rule) {
        List<User> house = List.of(ANNA, BEN, CEM, DANA);
        Simulation sim = new Simulation(rule);
        int maxCountGap = 0;
        int maxEffortGap = 0;
        for (int i = 0; i < 30; i++) {
            sim.assign(house, CHORES.get(i % CHORES.size()));
            maxCountGap = Math.max(maxCountGap, gap(house, sim.log, COUNT, null));
            maxEffortGap = Math.max(maxEffortGap, gap(house, sim.log, EFFORT, null));
        }
        return new ScenarioResult("30 completions", null, 0, 0, maxCountGap, maxEffortGap);
    }

    // ---------- Helpers ----------

    static final class Simulation {
        final ChoreRotationRule rule;
        final List<ChoreCompletion> log = new ArrayList<>();
        LocalDateTime now = START.plusDays(10);

        Simulation(ChoreRotationRule rule) {
            this.rule = rule;
        }

        User assign(List<User> members, Chore chore) {
            User picked = rule.nextAssignee(members, chore, log);
            record(chore, picked);
            return picked;
        }

        void record(Chore chore, User doneBy) {
            record(chore, doneBy, doneBy);
        }

        void record(Chore chore, User assignedTo, User doneBy) {
            now = now.plusHours(1);
            log.add(new ChoreCompletion(UUID.randomUUID(), now, chore, HOUSE, assignedTo, doneBy));
        }
    }

    // Most loaded minus least loaded member, only counting completions after 'since' if it is set
    static int gap(List<User> members, List<ChoreCompletion> log, ToIntFunction<ChoreCompletion> weight, LocalDateTime since) {
        List<Integer> loads = members.stream()
                .map(member -> log.stream()
                        .filter(completion -> completion.getDoneBy().getId().equals(member.getId()))
                        .filter(completion -> since == null || completion.getCompletedAt().isAfter(since))
                        .mapToInt(weight)
                        .sum())
                .toList();
        return loads.stream().max(Integer::compare).orElseThrow() - loads.stream().min(Integer::compare).orElseThrow();
    }

    static int longestSameChoreStreak(List<ChoreCompletion> log, User member) {
        int longest = 0;
        for (Chore chore : CHORES) {
            int run = 0;
            for (ChoreCompletion completion : log) {
                if (!completion.getChore().getId().equals(chore.getId())) {
                    continue;
                }
                run = completion.getDoneBy().getId().equals(member.getId()) ? run + 1 : 0;
                longest = Math.max(longest, run);
            }
        }
        return longest;
    }

    static User member(String id, String name, LocalDateTime joinedAt) {
        return new User(UUID.fromString(id), name, name.toLowerCase() + "@example.com", HOUSE, joinedAt, null);
    }

    static Chore chore(String id, String name, int effort) {
        return new Chore(UUID.fromString(id), name, 7, effort, START, HOUSE);
    }
}
