package org.kramreiter.mtcg.card.effect;

import org.kramreiter.mtcg.card.*;

public class Uprising implements UniqueEffect {
    private static final int UPRISESTR = 20;

    @Override
    public EffectTime getEffectTime() {
        return EffectTime.AfterCombat;
    }

    @Override
    public String executeEffect(Card self, Card opponent, Deck selfDeck, Deck opponentDeck, CombatOutcome prevOutcome) {
        if (prevOutcome.getWinner() == self) {
            strengthenDeck(selfDeck);
            strengthenDeck(opponentDeck);
            return "Goblins rise up!";
        }
        return null;
    }

    private void strengthenDeck(Deck deck) {
        for (Card c : deck.getCards()) {
            if (c.getTag() == MonsterTag.Goblin) {
                c.setPower(c.getPower() + UPRISESTR);
            }
        }
    }

    @Override
    public String getDescription() {
        return "Effect: Uprising\nAfter combat -> if this card wins, all Goblins in both decks gain +" + UPRISESTR + " strength\nNo longer just low-level monsters";
    }

    @Override
    public String getName() {
        return "Uprising";
    }
}
