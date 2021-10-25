package org.kramreiter.mtcg.card.effect;

import org.kramreiter.mtcg.card.*;

public class CleanseUndead implements UniqueEffect {
    @Override
    public EffectTime getEffectTime() {
        return EffectTime.AfterCombat;
    }

    @Override
    public String executeEffect(Card self, Card opponent, Deck selfDeck, Deck opponentDeck, CombatOutcome prevOutcome) {
        boolean selfRemoved = clearUndeadFromDeck(selfDeck);
        boolean opponentRemoved = clearUndeadFromDeck(opponentDeck);
        if (selfRemoved || opponentRemoved) {
            return "The rising sun burned away all undead creatures";
        }
        return null;
    }

    private boolean clearUndeadFromDeck(Deck deck) {
        boolean removed = false;
        for (Card c : deck.getCards()) {
            if (c.getTag() == MonsterTag.Undead) {
                removed = true;
                deck.removeCard(c);
            }
        }
        return removed;
    }

    @Override
    public String getDescription() {
        return "Effect: Cleanse Undead\nAfter combat -> remove all undead monsters in both decks from play\nRise and shine";
    }

    @Override
    public String getName() {
        return "Cleanse Undead";
    }
}
