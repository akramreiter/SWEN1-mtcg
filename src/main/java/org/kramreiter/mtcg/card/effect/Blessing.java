package org.kramreiter.mtcg.card.effect;

import org.kramreiter.mtcg.card.*;

public class Blessing implements UniqueEffect {
    private static final int BLESSSTR = 10;

    @Override
    public EffectTime getEffectTime() {
        return EffectTime.AfterCombat;
    }

    @Override
    public String executeEffect(Card self, Card opponent, Deck selfDeck, Deck opponentDeck, CombatOutcome prevOutcome) {
        for (Card c : selfDeck.getCards()) {
            if (!c.isSpell()) {
                c.setPower(c.getPower() + BLESSSTR);
            }
        }
        return "The blessing granted monsters additional strength";
    }

    @Override
    public String getDescription() {
        return "Effect: Blessing\nAfter combat -> all monsters in your deck gain +" + BLESSSTR + " strength\n";
    }

    @Override
    public String getName() {
        return "Blessing";
    }
}
