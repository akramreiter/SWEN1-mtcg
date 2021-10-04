package org.kramreiter.mtcg.card.effect;

import org.kramreiter.mtcg.card.*;

public class Sharpen implements UniqueEffect {
    @Override
    public EffectTime getEffectTime() {
        return EffectTime.BeforeCombat;
    }

    @Override
    public String executeEffect(Card self, Card opponent, Deck selfDeck, Deck opponentDeck, CombatOutcome prevOutcome) {
        self.setStrength(self.getStrength() + 25);
        return "Ow the edge";
    }

    @Override
    public String getDescription() {
        return "Effect: Sharpen\nBefore combat -> gain 25 strength\nDon't cut yourself on that edge";
    }
}
