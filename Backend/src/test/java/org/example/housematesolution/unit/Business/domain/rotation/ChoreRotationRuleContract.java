package org.example.housematesolution.unit.Business.domain.rotation;

import org.example.housematesolution.Business.domain.Chore;
import org.example.housematesolution.Business.domain.ChoreCompletion;
import org.example.housematesolution.Business.domain.User;
import org.example.housematesolution.Business.domain.rotation.ChoreRotationRule;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.example.housematesolution.unit.Business.domain.rotation.RotationScenarios.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Every variant's test class extends this, so all variants must pass these tests
abstract class ChoreRotationRuleContract {

    protected abstract ChoreRotationRule rule();

    private List<ChoreCompletion> sampleLog() {
        Simulation sim = new Simulation(rule());
        for (int i = 0; i < 7; i++) {
            sim.assign(FOUNDERS, CHORES.get(i % CHORES.size()));
        }
        return sim.log;
    }

    @Test
    void sameInputGivesSameOutput() {
        List<ChoreCompletion> log = sampleLog();

        User first = rule().nextAssignee(FOUNDERS, DISHES, log);
        User second = rule().nextAssignee(FOUNDERS, DISHES, log);

        assertEquals(first.getId(), second.getId());
    }

    @Test
    void orderOfInputListsDoesNotMatter() {
        List<ChoreCompletion> log = sampleLog();
        List<User> shuffledMembers = new ArrayList<>(FOUNDERS);
        List<ChoreCompletion> shuffledLog = new ArrayList<>(log);
        Collections.shuffle(shuffledMembers, new Random(42));
        Collections.shuffle(shuffledLog, new Random(42));

        User ordered = rule().nextAssignee(FOUNDERS, DISHES, log);
        User shuffled = rule().nextAssignee(shuffledMembers, DISHES, shuffledLog);

        assertEquals(ordered.getId(), shuffled.getId());
    }

    @Test
    void alwaysReturnsAMemberOfTheHouse() {
        List<ChoreCompletion> log = sampleLog();

        for (Chore chore : CHORES) {
            User picked = rule().nextAssignee(FOUNDERS, chore, log);

            assertTrue(FOUNDERS.stream().anyMatch(member -> member.getId().equals(picked.getId())));
        }
    }

    @Test
    void throwsForAHouseWithoutMembers() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> rule().nextAssignee(List.of(), DISHES, List.of()));

        assertEquals("A house needs at least one member to assign a chore", error.getMessage());
    }

    @Test
    void choreNeverDoneGoesToFirstMemberInJoinOrder() {
        assertEquals("Anna", coldStart(rule()).picked());
    }
}
