package org.kramreiter.mtcg.card.effect;

import org.kramreiter.mtcg.card.*;

public class CollateralDamage implements UniqueEffect {
    private static final int DAMAGE = 30;

    @Override
    public EffectTime getEffectTime() {
        return EffectTime.GameStart;
    }

    @Override
    public String executeEffect(Card self, Card opponent, Deck selfDeck, Deck opponentDeck, CombatOutcome prevOutcome) {
        Card c = selfDeck.getCards()[(int) (Math.random() * (selfDeck.getCards().length - 1))];
        c.setPower(c.getPower() - DAMAGE);
        return c.getName() + " has been severely weakened";
    }

    @Override
    public String getDescription() {
        return "Effect: Collateral Damage\nGame start -> a random card in your deck (including this one) loses " + DAMAGE + "strength\nTake cover";
    }

    @Override
    public String getName() {
        return "Collateral Damage";
    }
}
