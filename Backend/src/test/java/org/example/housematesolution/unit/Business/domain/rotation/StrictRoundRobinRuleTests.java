package org.example.housematesolution.unit.Business.domain.rotation;

import org.example.housematesolution.Business.domain.rotation.ChoreRotationRule;
import org.example.housematesolution.Business.domain.rotation.StrictRoundRobinRule;
import org.junit.jupiter.api.Test;

import static org.example.housematesolution.unit.Business.domain.rotation.RotationScenarios.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

// Variant A is expected to be unfair: these tests document the measured weaknesses
class StrictRoundRobinRuleTests extends ChoreRotationRuleContract {

    private final StrictRoundRobinRule rule = new StrictRoundRobinRule();

    @Override
    protected ChoreRotationRule rule() {
        return rule;
    }

    @Test
    void ignoresOtherChoresOnATie() {
        ScenarioResult result = tieOnCount(rule);

        assertEquals("Anna", result.picked());
        assertEquals(2, result.maxCountGap());
    }

    @Test
    void leavesAGapOfThreeAfterNewcomerJoins() {
        ScenarioResult result = newcomerJoinsHalfway(rule);

        assertEquals(6, result.newcomerTurns());
        assertEquals(1, result.newcomerStreak());
        assertEquals(3, result.maxCountGap());
    }

    @Test
    void skipsTheMemberWhoWasCoveredFor() {
        ScenarioResult result = coverForHousemate(rule);

        assertEquals("Ben", result.picked());
        assertEquals(2, result.maxCountGap());
    }

    @Test
    void letsCompletionGapGrowToThreeOverTime() {
        ScenarioResult result = fairnessOverTime(rule);

        assertEquals(3, result.maxCountGap());
        assertEquals(6, result.maxEffortGap());
    }
}
