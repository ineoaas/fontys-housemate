package org.example.housematesolution.Business.domain.rotation;

import org.example.housematesolution.Business.domain.Chore;
import org.example.housematesolution.Business.domain.ChoreCompletion;
import org.example.housematesolution.Business.domain.User;

import java.util.List;

// Computes who does a chore next from the completion log, so the turn is never stored
public interface ChoreRotationRule {

    String name();

    // A completion counts for doneBy, so covering for someone counts for the one who covered
    User nextAssignee(List<User> members, Chore chore, List<ChoreCompletion> completions);
}
