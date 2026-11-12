package org.example.housematesolution.unit.Business.domain.rotation;

import org.example.housematesolution.Business.domain.rotation.ChoreRotationRule;
import org.example.housematesolution.Business.domain.rotation.LeastCountPerChoreRule;
import org.example.housematesolution.Business.domain.rotation.LeastEffortRule;
import org.example.housematesolution.Business.domain.rotation.StrictRoundRobinRule;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.example.housematesolution.unit.Business.domain.rotation.RotationScenarios.*;

// Prints a markdown table of every scenario for every variant, for the research document
class ChoreRotationComparisonTests {

    private final List<ChoreRotationRule> rules =
            List.of(new StrictRoundRobinRule(), new LeastCountPerChoreRule(), new LeastEffortRule());

    @Test
    void printsScenarioTable() {
        StringBuilder table = new StringBuilder(
                "\n| Scenario | Variant | Result | Max gap (count) | Max gap (effort) |\n|---|---|---|---|---|\n");
        for (String scenario : SCENARIOS) {
            for (ChoreRotationRule rule : rules) {
                ScenarioResult result = run(scenario, rule);
                table.append("| ").append(scenario)
                        .append(" | ").append(rule.name())
                        .append(" | ").append(result.summary())
                        .append(" | ").append(result.maxCountGap())
                        .append(" | ").append(result.maxEffortGap())
                        .append(" |\n");
            }
        }
        System.out.println(table);
    }
}
