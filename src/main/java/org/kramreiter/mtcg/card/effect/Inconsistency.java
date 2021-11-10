package org.kramreiter.mtcg.card.effect;

import org.kramreiter.mtcg.card.*;

public class Inconsistency implements UniqueEffect {
    private static final int MAX_STRENGTH = 100;

    @Override
    public EffectTime getEffectTime() {
        return EffectTime.BeforeCombat;
    }

    @Override
    public String executeEffect(Card self, Card opponent, Deck selfDeck, Deck opponentDeck, CombatOutcome prevOutcome) {
        self.setPower((int) (Math.random() * MAX_STRENGTH + 1));
        self.setCardType(CardType.typeFromNumber((int) (Math.random() * 5)));
        StringBuilder glitch = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            glitch.append((char) (33 + (int) (Math.random() * 93)));
        }
        return glitch.toString();
    }

    @Override
    public String getDescription() {
        return "Effect: Inconsistency\nBefore combat -> this card gains a random type and damage in the range of 1 to " + MAX_STRENGTH + "\nMore random than pack openings";
    }

    @Override
    public String getName() {
        return "Inconsistency";
    }
}
