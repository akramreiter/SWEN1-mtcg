package org.kramreiter.mtcg.card;

public interface SpecialRule {
    public abstract CombatOutcome executeRule(Card source, Card target);
    public MonsterTag[] getAffectedTags();
    public String getEffectDescription();
}
