package org.kramreiter.mtcg.card.effect;

import org.kramreiter.mtcg.card.*;

public class Blessing implements UniqueEffect {
    @Override
    public EffectTime getEffectTime() {
        return EffectTime.AfterCombat;
    }

    @Override
    public String executeEffect(Card self, Card opponent, Deck selfDeck, Deck opponentDeck, CombatOutcome prevOutcome) {
        for (Card c : selfDeck.getCards()) {
            if (!c.isSpell()) {
                c.setStrength(c.getStrength() + 10);
            }
        }
        return "The blessing granted monsters additional strength";
    }

    @Override
    public String getDescription() {
        return "Effect: Blessing\nAfter combat -> all monsters in your deck gain +10 strength\n";
    }
}
