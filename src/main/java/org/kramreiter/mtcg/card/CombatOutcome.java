package org.kramreiter.mtcg.card;

import lombok.Getter;

@Getter
public class CombatOutcome {
    protected String message;
    protected Card winner;
    protected Card loser;
    public CombatOutcome(String message, Card winner, Card loser) {
        this.message = message;
        this.winner = winner;
        this.loser = loser;
    }
}
