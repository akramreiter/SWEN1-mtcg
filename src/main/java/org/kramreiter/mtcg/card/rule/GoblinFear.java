package org.kramreiter.mtcg.card.rule;

import org.kramreiter.mtcg.card.*;

public class GoblinFear implements SpecialRule {
    @Override
    public CombatOutcome executeRule(Card source, Card target) {
        if (!source.isSpell() && !target.isSpell() && source.getTag() == MonsterTag.Dragon && target.getTag() == MonsterTag.Goblin) {
            return new CombatOutcome("/l was too afraid to fight /w", source, target);
        }
        return null;
    }

    @Override
    public MonsterTag[] getAffectedTags() {
        return new MonsterTag[] {
                MonsterTag.Dragon, MonsterTag.Goblin
        };
    }

    @Override
    public CardType[] getAffectedSpellTypes() {
        return new CardType[0];
    }

    @Override
    public String getEffectDescription() {
        return "Goblins are too afraid of Dragons to fight them";
    }
}
