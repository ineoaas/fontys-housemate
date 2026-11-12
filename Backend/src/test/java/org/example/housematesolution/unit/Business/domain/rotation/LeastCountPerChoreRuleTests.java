package org.example.housematesolution.unit.Business.domain.rotation;

import org.example.housematesolution.Business.domain.rotation.ChoreRotationRule;
import org.example.housematesolution.Business.domain.rotation.LeastCountPerChoreRule;
import org.junit.jupiter.api.Test;

import static org.example.housematesolution.unit.Business.domain.rotation.RotationScenarios.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LeastCountPerChoreRuleTests extends ChoreRotationRuleContract {

    private final LeastCountPerChoreRule rule = new LeastCountPerChoreRule();

    @Override
    protected ChoreRotationRule rule() {
        return rule;
    }

    @Test
    void tieOnCountGoesToFewestCompletionsAcrossAllChores() {
        ScenarioResult result = tieOnCount(rule);

        assertEquals("Ben", result.picked());
        assertEquals(0, result.maxCountGap());
    }

    @Test
    void newcomerStartsAtHouseMinimumAndIsNotGivenTheSameChoreInARow() {
        ScenarioResult result = newcomerJoinsHalfway(rule);

        assertEquals(8, result.newcomerTurns());
        assertEquals(1, result.newcomerStreak());
        assertEquals(1, result.maxCountGap());
    }

    @Test
    void memberWhoWasCoveredForIsNext() {
        ScenarioResult result = coverForHousemate(rule);

        assertEquals("Cem", result.picked());
        assertEquals(1, result.maxCountGap());
    }

    @Test
    void keepsCompletionGapAtMostOneOverTime() {
        assertEquals(1, fairnessOverTime(rule).maxCountGap());
    }
}
