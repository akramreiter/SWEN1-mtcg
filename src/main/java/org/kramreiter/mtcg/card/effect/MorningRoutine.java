package org.kramreiter.mtcg.card.effect;

import org.kramreiter.mtcg.card.*;

public class MorningRoutine implements UniqueEffect {
    private static final int ROUTINE_BUFF = 10;

    @Override
    public EffectTime getEffectTime() {
        return EffectTime.GameStart;
    }

    @Override
    public String executeEffect(Card self, Card opponent, Deck selfDeck, Deck opponentDeck, CombatOutcome prevOutcome) {
        for (Card c : selfDeck.getCards()) {
            if (c.getTag() == MonsterTag.Knight) {
                c.setStrength(c.getStrength() + ROUTINE_BUFF);
            }
        }
        return "The knights are armed and ready!";
    }

    @Override
    public String getDescription() {
        return "Effect: Morning Routine\nGame start -> all knights gain " + ROUTINE_BUFF + " strength\nNever skip leg day";
    }

    @Override
    public String getName() {
        return "Morning Routine";
    }
}
