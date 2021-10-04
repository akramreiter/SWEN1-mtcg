package org.kramreiter.mtcg.card.effect;

import org.kramreiter.mtcg.card.*;

public class Curse implements UniqueEffect {
    @Override
    public EffectTime getEffectTime() {
        return EffectTime.BeforeCombat;
    }

    @Override
    public String executeEffect(Card self, Card opponent, Deck selfDeck, Deck opponentDeck, CombatOutcome prevOutcome) {
        if (!opponent.isSpell()) {
            opponent.setTag(MonsterTag.Undead);
            return opponent.getName() + " was turned undead";
        }
        return null;
    }

    @Override
    public String getDescription() {
        return "Effect: Curse\nBefore combat -> monsters this card fights against become undead\n";
    }
}
