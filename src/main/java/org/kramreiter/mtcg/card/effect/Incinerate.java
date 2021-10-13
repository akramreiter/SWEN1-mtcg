package org.kramreiter.mtcg.card.effect;

import org.kramreiter.mtcg.card.*;

public class Incinerate implements UniqueEffect {
    private static final int INCINERATENERF = 15;

    @Override
    public EffectTime getEffectTime() {
        return EffectTime.AfterCombat;
    }

    @Override
    public String executeEffect(Card self, Card opponent, Deck selfDeck, Deck opponentDeck, CombatOutcome prevOutcome) {
        int index = (int) (opponentDeck.getCards().length * Math.random());
        opponentDeck.getCards()[index].setStrength(opponentDeck.getCards()[index].getStrength() - INCINERATENERF);
        return "A random card has been burned, reducing its strength";
    }

    @Override
    public String getDescription() {
        return "Effect: Incinerate\nAfter combat -> a random card in your opponent's deck loses " + INCINERATENERF + " strength\nThe dragon's flame burn more than just their target";
    }

    @Override
    public String getName() {
        return "Incinerate";
    }
}
