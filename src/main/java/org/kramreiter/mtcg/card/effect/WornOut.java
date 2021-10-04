package org.kramreiter.mtcg.card.effect;

import org.kramreiter.mtcg.card.*;

public class WornOut implements UniqueEffect {
    @Override
    public EffectTime getEffectTime() {
        return EffectTime.AfterCombat;
    }

    @Override
    public String executeEffect(Card self, Card opponent, Deck selfDeck, Deck opponentDeck, CombatOutcome prevOutcome) {
        self.setStrength(self.getStrength() - 20);
        return "The Champion lost some strength";
    }

    @Override
    public String getDescription() {
        return "Effect: Worn Out\nAfter combat -> this card loses 20 strength\nThe Champion is past their peak";
    }
}
