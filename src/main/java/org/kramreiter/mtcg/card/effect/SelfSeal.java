package org.kramreiter.mtcg.card.effect;

import org.kramreiter.mtcg.card.*;

public class SelfSeal implements UniqueEffect {
    @Override
    public EffectTime getEffectTime() {
        return EffectTime.AfterCombat;
    }

    @Override
    public String executeEffect(Card self, Card opponent, Deck selfDeck, Deck opponentDeck, CombatOutcome prevOutcome) {
        self.setStrength(0);
        return self.getName() + "'s powers have been sealed";
    }

    @Override
    public String getDescription() {
        return "Effect: Self-Seal\nAfter combat -> sets own strength to 0\nSometimes you only need one shot...";
    }

    @Override
    public String getName() {
        return "Self-Seal";
    }
}
