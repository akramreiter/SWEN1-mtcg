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
        self.setStrength((int) (Math.random() * MAX_STRENGTH + 1));
        self.setCardType(CardType.typeFromNumber((int) (Math.random() * 5)));
        return "2362#+-.,-.3567*'=))&%9?";
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
