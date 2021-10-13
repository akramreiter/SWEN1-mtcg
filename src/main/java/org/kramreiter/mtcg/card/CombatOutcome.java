package org.kramreiter.mtcg.card;

import lombok.Getter;

@Getter
public class CombatOutcome {
    private static final String DEFAULT_MSG = "/w won the fight against /l";

    protected String message;
    protected boolean wasDraw;
    protected Card winner;
    protected Card loser;
    public CombatOutcome(String message, Card winner, Card loser) {
        if (message != null) {
            this.message = message;
        } else if (winner.getCustomWin() != null) {
            this.message = winner.getCustomWin();
        } else {
            this.message = DEFAULT_MSG;
        }
        parseMessage(winner, loser);
        this.winner = winner;
        this.loser = loser;
    }

    public CombatOutcome(String message, Card winner, Card loser, boolean wasDraw) {
        if (message != null) {
            this.message = message;
        } else if (winner.getCustomWin() != null) {
            this.message = winner.getCustomWin();
        } else {
            this.message = "/w won the fight against /l";
        }
        parseMessage(winner, loser);
        this.winner = winner;
        this.loser = loser;
        this.wasDraw = wasDraw;
    }

    private void parseMessage(Card winner, Card loser) {
        if (winner == null || loser == null) return;
        if (this.message.contains("/w")) {
            int winnerStr = winner.computeStrengthAgainst(loser);
            String winnerString = winner.getName() + "(strength: " + winnerStr + "; type: " + winner.getCardType().toString() + ")";
            this.message = this.message.replaceAll("/w", winnerString);
        }
        if (this.message.contains("/l")) {
            int loserStr = loser.computeStrengthAgainst(winner);
            String loserString = loser.getName() + "(strength: " + loserStr + "; type: " + loser.getCardType().toString() + ")";
            this.message = this.message.replaceAll("/l", loserString);
        }
    }
}
