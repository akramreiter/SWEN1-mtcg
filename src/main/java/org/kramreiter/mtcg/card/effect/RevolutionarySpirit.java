package org.kramreiter.mtcg.card.effect;

import org.kramreiter.mtcg.card.*;

public class RevolutionarySpirit implements UniqueEffect {
    private static final int BUFF_STR = 5;

    @Override
    public EffectTime getEffectTime() {
        return EffectTime.AfterCombat;
    }

    @Override
    public String executeEffect(Card self, Card opponent, Deck selfDeck, Deck opponentDeck, CombatOutcome prevOutcome) {
        for (Card c : selfDeck.getCards()) {
            if (c.getTag() == MonsterTag.Goblin) {
                c.setStrength(c.getStrength() + BUFF_STR);
            }
        }
        return "Our Goblins are motivated to fight";
    }

    @Override
    public String getDescription() {
        return "Effect: Revolutionary Spirit\nAfter combat -> all Goblins in your deck gain " + BUFF_STR + " strength\nThe hero Goblinhood deserves";
    }

    @Override
    public String getName() {
        return "Revolutionary Spirit";
    }
}
