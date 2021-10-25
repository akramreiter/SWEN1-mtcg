package org.kramreiter.mtcg.card.effect;

import org.kramreiter.mtcg.card.*;

public class DriedLands implements UniqueEffect {
    private static final int REDUCE_STR = 5;

    @Override
    public EffectTime getEffectTime() {
        return EffectTime.AfterCombat;
    }

    @Override
    public String executeEffect(Card self, Card opponent, Deck selfDeck, Deck opponentDeck, CombatOutcome prevOutcome) {
        boolean selfWeakened = weakenDeck(selfDeck);
        boolean opponentWeakened = weakenDeck(opponentDeck);
        if (selfWeakened || opponentWeakened) {
            return "Water monsters were weakened by a lack of hydration";
        }
        return null;
    }

    private boolean weakenDeck(Deck deck) {
        boolean weakened = false;
        for (Card c : deck.getCards()) {
            if (!c.isSpell() && c.getCardType() == CardType.Water) {
                c.setStrength(c.getStrength() - 5);
            }
        }
        return weakened;
    }

    @Override
    public String getDescription() {
        return "Effect: Dried Lands\nAfter combat -> all water monsters in both decks lose " + REDUCE_STR + " strength\nBetter stay hydrated";
    }

    @Override
    public String getName() {
        return "Dried Lands";
    }
}
