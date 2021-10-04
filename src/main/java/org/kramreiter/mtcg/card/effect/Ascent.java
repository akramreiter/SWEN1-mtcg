package org.kramreiter.mtcg.card.effect;

import org.kramreiter.mtcg.card.*;

public class Ascent implements UniqueEffect {

    @Override
    public EffectTime getEffectTime() {
        return EffectTime.GameStart;
    }

    @Override
    public String executeEffect(Card self, Card opponent, Deck selfDeck, Deck opponentDeck, CombatOutcome prevOutcome) {
        for (Card c : selfDeck.getCards()) {
            if (c.isSpell() && c.getCardType() == CardType.Air) {
                c.setStrength(c.getStrength() + 20);
            }
        }
        return "The Archmage of Winds empowered air spells";
    }

    @Override
    public String getDescription() {
        return "Effect: Ascent\nGame start -> all air spells in your deck gain 20 Power\nThe storm becomes too strong to withstand";
    }
}
