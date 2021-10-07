package org.kramreiter.mtcg.card;

import lombok.Getter;

@Getter
public class CombatOutcome {
    protected String message;
    protected Card winner;
    protected Card loser;
    public CombatOutcome(String message, Card winner, Card loser) {
        if (winner != null && winner.getCustomWin() != null) {
            this.message = winner.getCustomWin();
        } else if (message != null) {
            this.message = message;
        } else if (winner != null) {
            this.message = "/w won the fight against /l";
        }
        parseMessage(winner, loser);
        this.winner = winner;
        this.loser = loser;
    }

    private void parseMessage(Card winner, Card loser) {
        if (winner == null || loser == null) return;
        if (this.message.contains("/w")) {
            int winnerStr = winner.getStrength();
            if (winner.isSpell()) {
                winnerStr *= RuleManager.computeWeaknessMultiplier(winner.getCardType(), loser.getCardType());
            }
            String winnerString = winner.getName() + "(strength: " + winnerStr + "; type: " + winner.getCardType().toString() + ")";
            this.message = this.message.replaceAll("/w", winnerString);
        }
        if (this.message.contains("/l")) {
            int loserStr = winner.getStrength();
            if (loser.isSpell()) {
                loserStr *= RuleManager.computeWeaknessMultiplier(loser.getCardType(), winner.getCardType());
            }
            String loserString = loser.getName() + "(strength: " + loserStr + "; type: " + loser.getCardType().toString() + ")";
            this.message = this.message.replaceAll("/l", loserString);
        }
    }
}
