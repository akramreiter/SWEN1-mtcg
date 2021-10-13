package org.kramreiter.mtcg.card;

public interface UniqueEffect {
    EffectTime getEffectTime();
    String executeEffect(Card self, Card opponent, Deck selfDeck, Deck opponentDeck, CombatOutcome prevOutcome);
    String getDescription();
    String getName();
}
