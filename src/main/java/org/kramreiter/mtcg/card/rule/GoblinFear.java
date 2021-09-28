package org.kramreiter.mtcg.card.rule;

import org.kramreiter.mtcg.card.Card;
import org.kramreiter.mtcg.card.CombatOutcome;
import org.kramreiter.mtcg.card.MonsterTag;
import org.kramreiter.mtcg.card.SpecialRule;

public class GoblinFear implements SpecialRule {
    @Override
    public CombatOutcome executeRule(Card source, Card target) {
        if (!source.isSpell() && !target.isSpell() && source.getTag() == MonsterTag.Dragon && target.getTag() == MonsterTag.Goblin) {
            return new CombatOutcome(target.getName() + " was too afraid to fight " + source.getName(), source, target);
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
    public String getEffectDescription() {
        return "Goblins are too afraid of Dragons to fight them";
    }
}
