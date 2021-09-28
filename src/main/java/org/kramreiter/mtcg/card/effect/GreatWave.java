package org.kramreiter.mtcg.card.effect;

import org.kramreiter.mtcg.card.*;

public class GreatWave implements UniqueEffect {
    @Override
    public EffectTime getEffectTime() {
        return EffectTime.BeforeCombat;
    }

    @Override
    public String executeEffect(Card self, Card opponent, Deck selfDeck, Deck opponentDeck, CombatOutcome prevOutcome) {
        for (Card c : selfDeck.getCards()) {
            if (!c.isSpell() && c.getCardType() == CardType.Water) {
                c.setStrength(c.getStrength() + 10);
            }
        }
        return "The Grand Flood strengthened water monsters";
    }

    @Override
    public String getDescription() {
        return "Effect: Great Wave\nBefore combat -> all Water monsters in your deck gain +10\nThe flood can't be held back";
    }
}
