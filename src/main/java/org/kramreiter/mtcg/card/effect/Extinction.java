package org.kramreiter.mtcg.card.effect;

import org.kramreiter.mtcg.card.*;

public class Extinction implements UniqueEffect {
    @Override
    public EffectTime getEffectTime() {
        return EffectTime.AfterCombat;
    }

    @Override
    public String executeEffect(Card self, Card opponent, Deck selfDeck, Deck opponentDeck, CombatOutcome prevOutcome) {
        if (!opponent.isSpell() && opponent.getTag() != null) {
            MonsterTag tag = opponent.getTag();
            int count = 0;
            for (Card c : opponentDeck.getCards().clone()) {
                if (c.getTag() == tag) {
                    count++;
                    selfDeck.removeCard(c);
                }
            }
            for (Card c : opponentDeck.getCards().clone()) {
                if (c.getTag() == tag) {
                    count++;
                    opponentDeck.removeCard(c);
                }
            }
            return count + " monsters were eradicated by the World-ending Collapse";
        }
        return null;
    }

    @Override
    public String getDescription() {
        return "Effect: Extinction\nAfter combat -> if used against a monster with a tag, all monsters with the same tag in both decks are destroyed\nAnother day, another world-ending event";
    }
}
