package org.kramreiter.mtcg.card.effect;

import org.kramreiter.mtcg.card.*;

public class Scorch implements UniqueEffect {
    @Override
    public EffectTime getEffectTime() {
        return EffectTime.BeforeCombat;
    }

    @Override
    public String executeEffect(Card self, Card opponent, Deck selfDeck, Deck opponentDeck, CombatOutcome prevOutcome) {
        for (Card c : opponentDeck.getCards()) {
            c.setStrength(c.getStrength() - 5);
        }
        return "The flames of hell scorched the world";
    }

    @Override
    public String getDescription() {
        return "Effect: Hellfire\nBefore combat -> all enemy cards lose 5 strength\nSome just want to see the world burn";
    }
}
