package org.kramreiter.mtcg.card.effect;

import org.kramreiter.mtcg.card.*;

public class BlazingAura implements UniqueEffect {
    private static final int BUFF_STR = 10;

    @Override
    public EffectTime getEffectTime() {
        return EffectTime.GameStart;
    }

    @Override
    public String executeEffect(Card self, Card opponent, Deck selfDeck, Deck opponentDeck, CombatOutcome prevOutcome) {
        buffDeck(selfDeck);
        buffDeck(opponentDeck);
        return "The rising heat empowered fire spells";
    }

    public void buffDeck(Deck deck) {
        for (Card c : deck.getCards()) {
            if (c.isSpell() && c.getCardType() == CardType.Fire) {
                c.setStrength(c.getStrength() + BUFF_STR);
            }
        }
    }

    @Override
    public String getDescription() {
        return "Effect: Blazing Aura\nGame start -> all fire spells in both decks gain " + BUFF_STR + "strength\nIt's getting hot in here";
    }

    @Override
    public String getName() {
        return "Blazing Aura";
    }
}
