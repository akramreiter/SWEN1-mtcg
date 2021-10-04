package org.kramreiter.mtcg.card.effect;

import org.kramreiter.mtcg.card.*;

public class SpellThief implements UniqueEffect {

    @Override
    public EffectTime getEffectTime() {
        return EffectTime.AfterCombat;
    }

    @Override
    public String executeEffect(Card self, Card opponent, Deck selfDeck, Deck opponentDeck, CombatOutcome prevOutcome) {
        if (prevOutcome.getWinner() == self && opponent.isSpell()) {
            selfDeck.addCard(opponent.clone());
            return opponent.getName() + " has been absorbed";
        }
        return null;
    }

    @Override
    public String getDescription() {
        return "Effect: Spell-Thief\nAfter Combat -> if this card wins against a spell, add a copy of it to your deck\nWe do a little plagiarism...";
    }
}
