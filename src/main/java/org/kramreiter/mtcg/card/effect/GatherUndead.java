package org.kramreiter.mtcg.card.effect;

import org.kramreiter.mtcg.card.*;

public class GatherUndead implements UniqueEffect {
    private static final int UNDEAD_BUFF = 3;

    @Override
    public EffectTime getEffectTime() {
        return EffectTime.BeforeCombat;
    }

    @Override
    public String executeEffect(Card self, Card opponent, Deck selfDeck, Deck opponentDeck, CombatOutcome prevOutcome) {
        int undeadCount = countUndead(selfDeck) + countUndead(opponentDeck);
        self.setStrength(self.getBaseStrength() + undeadCount * UNDEAD_BUFF);
        if (undeadCount > 0) {
            return "Nearby Undead strengthened Shifting Grounds";
        }
        return null;
    }

    private int countUndead(Deck deck) {
        int undeadCount = 0;
        for (Card c : deck.getCards()) {
            if (c.getTag() == MonsterTag.Undead) {
                undeadCount++;
            }
        }
        return undeadCount;
    }

    @Override
    public String getDescription() {
        return "Effect: Gather Undead\nBefore combat -> set this cards strength to its base strength + " + UNDEAD_BUFF + " * the amount of Undead monsters in both decks\nSome things should never be unearthed";
    }

    @Override
    public String getName() {
        return "Gather Undead";
    }
}
