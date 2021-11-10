package org.kramreiter.mtcg.card.effect;

import org.kramreiter.mtcg.card.*;

public class Nullify implements UniqueEffect {
    @Override
    public EffectTime getEffectTime() {
        return EffectTime.AfterCombat;
    }

    @Override
    public String executeEffect(Card self, Card opponent, Deck selfDeck, Deck opponentDeck, CombatOutcome prevOutcome) {
        for (Card c : selfDeck.getCards()) {
            c.setPower(c.getBasePower());
        }
        return "Cards returned to their state before the game";
    }

    @Override
    public String getDescription() {
        return "Effect: Nullify\nafter combat -> reset the strengths of cards your deck to their base strength\nNot all changes can last";
    }

    @Override
    public String getName() {
        return "Nullify";
    }
}