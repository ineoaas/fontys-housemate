package org.example.housematesolution.unit.Business.domain.rotation;

import org.example.housematesolution.Business.domain.rotation.ChoreRotationRule;
import org.example.housematesolution.Business.domain.rotation.LeastEffortRule;
import org.junit.jupiter.api.Test;

import static org.example.housematesolution.unit.Business.domain.rotation.RotationScenarios.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LeastEffortRuleTests extends ChoreRotationRuleContract {

    private final LeastEffortRule rule = new LeastEffortRule();

    @Override
    protected ChoreRotationRule rule() {
        return rule;
    }

    @Test
    void tieOnCountGoesToLowestEffort() {
        ScenarioResult result = tieOnCount(rule);

        assertEquals("Ben", result.picked());
        assertEquals(1, result.maxEffortGap());
    }

    @Test
    void newcomerStartsAtHouseMinimumAndIsNotGivenTheSameChoreInARow() {
        ScenarioResult result = newcomerJoinsHalfway(rule);

        assertEquals(8, result.newcomerTurns());
        assertEquals(1, result.newcomerStreak());
        assertEquals(4, result.maxEffortGap());
    }

    @Test
    void memberWhoWasCoveredForIsNext() {
        ScenarioResult result = coverForHousemate(rule);

        assertEquals("Cem", result.picked());
        assertEquals(2, result.maxEffortGap());
    }

    @Test
    void keepsEffortGapWithinOneChoreOverTime() {
        // 3 is the effort of the heaviest chore
        assertEquals(3, fairnessOverTime(rule).maxEffortGap());
    }
}
