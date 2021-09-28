package org.kramreiter.mtcg.card.rule;

import org.kramreiter.mtcg.card.Card;
import org.kramreiter.mtcg.card.CombatOutcome;
import org.kramreiter.mtcg.card.MonsterTag;
import org.kramreiter.mtcg.card.SpecialRule;

public class FireElfEvasion implements SpecialRule {
    @Override
    public CombatOutcome executeRule(Card source, Card target) {
        if (!source.isSpell() && !target.isSpell() && source.getTag() == MonsterTag.FireElf && target.getTag() == MonsterTag.Dragon) {
            return new CombatOutcome(source.getName() + " easily dodged the attacks of " + target.getName(), source, target);
        }
        return null;
    }

    @Override
    public MonsterTag[] getAffectedTags() {
        return new MonsterTag[] {
                MonsterTag.FireElf, MonsterTag.Dragon
        };
    }

    @Override
    public String getEffectDescription() {
        return "Fire Elves can evade attacks from Dragons";
    }
}
