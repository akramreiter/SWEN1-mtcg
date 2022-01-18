package org.kramreiter.mtcg.card;

public interface SpecialRule {
    CombatOutcome executeRule(Card source, Card target);
    MonsterTag[] getAffectedTags();
    CardType[] getAffectedSpellTypes();
    String getEffectDescription();
}
