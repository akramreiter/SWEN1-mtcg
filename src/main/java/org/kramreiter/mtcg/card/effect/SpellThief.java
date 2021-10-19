package org.kramreiter.mtcg.card.effect;

import org.kramreiter.mtcg.card.*;

public class SpellThief implements UniqueEffect {
    private int uses = 2;

    @Override
    public EffectTime getEffectTime() {
        return EffectTime.AfterCombat;
    }

    @Override
    public String executeEffect(Card self, Card opponent, Deck selfDeck, Deck opponentDeck, CombatOutcome prevOutcome) {
        if (prevOutcome.getWinner() == self && opponent.isSpell() && uses > 0) {
            Card copy = opponent.clone();
            copy.setOwnerName(self.getOwnerName());
            selfDeck.addCard(copy);
            uses--;
            return opponent.getName() + " has been absorbed";
        }
        return null;
    }

    @Override
    public String getDescription() {
        return "Effect: Spell-Thief\nAfter Combat -> if this card wins against a spell, add a copy of it to your deck (up to 2 times per game)\nWe do a little plagiarism...";
    }

    @Override
    public String getName() {
        return "Spell-Thief";
    }
}
