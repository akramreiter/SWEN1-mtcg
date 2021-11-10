package org.kramreiter.mtcg.card.effect;

import org.kramreiter.mtcg.card.*;

public class WornOut implements UniqueEffect {
    private static final int WORNNERF = 20;

    @Override
    public EffectTime getEffectTime() {
        return EffectTime.AfterCombat;
    }

    @Override
    public String executeEffect(Card self, Card opponent, Deck selfDeck, Deck opponentDeck, CombatOutcome prevOutcome) {
        self.setPower(self.getPower() - WORNNERF);
        return "The Champion lost some strength";
    }

    @Override
    public String getDescription() {
        return "Effect: Worn Out\nAfter combat -> this card loses " + WORNNERF + " strength\nThe Champion is past their peak";
    }

    @Override
    public String getName() {
        return "Worn Out";
    }
}
