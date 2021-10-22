package org.kramreiter.mtcg.card.effect;

import org.kramreiter.mtcg.card.*;

public class Gamble implements UniqueEffect {
    @Override
    public EffectTime getEffectTime() {
        return EffectTime.BeforeCombat;
    }

    @Override
    public String executeEffect(Card self, Card opponent, Deck selfDeck, Deck opponentDeck, CombatOutcome prevOutcome) {
        if ((int) (Math.random() * 2) > 1) {
            self.setStrength(self.getStrength() - 10);
            return "You lose some...";
        } else {
            self.setStrength(self.getStrength() + 10);
            return "You win some...";
        }
    }

    @Override
    public String getDescription() {
        return "Effect: Gamble\nBefore combat -> either gain or lose 10 strength randomly\nBelieve in the heart of the cards";
    }

    @Override
    public String getName() {
        return "Gamble";
    }
}
