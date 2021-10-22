package org.kramreiter.mtcg.card.effect;

import org.kramreiter.mtcg.card.*;

public class Sunrise implements UniqueEffect {

    @Override
    public EffectTime getEffectTime() {
        return EffectTime.AfterCombat;
    }

    @Override
    public String executeEffect(Card self, Card opponent, Deck selfDeck, Deck opponentDeck, CombatOutcome prevOutcome) {
        boolean selfBurned = undeadBurned(selfDeck);
        boolean opponentBurned = undeadBurned(opponentDeck);
        if (selfBurned || opponentBurned) {
            return "Undead monsters died from exposure to daylight";
        }
        return null;
    }

    private boolean undeadBurned(Deck deck) {
        boolean burned = false;
        for (Card c : deck.getCards()) {
            if (c.getTag() == MonsterTag.Undead) {
                deck.removeCard(c);
                burned = true;
            }
        }
        return burned;
    }

    @Override
    public String getDescription() {
        return "Effect: Sunrise\nAfter combat -> destroy all undead monsters in both decks\nRise and shine";
    }

    @Override
    public String getName() {
        return "Sunrise";
    }
}
