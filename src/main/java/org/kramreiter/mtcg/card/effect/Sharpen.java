package org.kramreiter.mtcg.card.effect;

import org.kramreiter.mtcg.card.*;

public class Sharpen implements UniqueEffect {
    private static final int SHARPENSTR = 25;

    @Override
    public EffectTime getEffectTime() {
        return EffectTime.BeforeCombat;
    }

    @Override
    public String executeEffect(Card self, Card opponent, Deck selfDeck, Deck opponentDeck, CombatOutcome prevOutcome) {
        self.setStrength(self.getStrength() + SHARPENSTR);
        return "Ow the edge";
    }

    @Override
    public String getDescription() {
        return "Effect: Sharpen\nBefore combat -> gain " + SHARPENSTR + " strength\nDon't cut yourself on that edge";
    }
}
