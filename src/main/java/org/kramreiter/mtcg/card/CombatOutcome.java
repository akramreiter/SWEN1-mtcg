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
        this.message = this.message.replaceAll("/w", getMessageStringForCard(winner, loser));
        this.message = this.message.replaceAll("/l", getMessageStringForCard(loser, winner));
    }

    private String getMessageStringForCard(Card self, Card opponent) {
        int selfStr = self.computeStrengthAgainst(opponent);
        int cachedStr = self.getPower();
        self.setPower(selfStr);
        String output = self.toString();
        self.setPower(cachedStr);
        return output;
    }
}
