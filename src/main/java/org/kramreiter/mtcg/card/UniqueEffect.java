package org.kramreiter.mtcg.card;

public interface UniqueEffect {
    public abstract EffectTime getEffectTime();
    public abstract String executeEffect(Card self, Card opponent, Deck selfDeck, Deck opponentDeck, CombatOutcome prevOutcome);
    public abstract String getDescription();
}
