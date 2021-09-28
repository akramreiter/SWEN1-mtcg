package org.kramreiter.mtcg.card.rule;

import org.kramreiter.mtcg.card.*;

public class KnightDrown implements SpecialRule {
    @Override
    public CombatOutcome executeRule(Card source, Card target) {
        if (source.isSpell() && !target.isSpell() && source.getCardType() == CardType.Water && target.getTag() == MonsterTag.Knight) {
            return new CombatOutcome(target.getName() + " was unable to escape the flood created by " + source.getName() + " and drowned", source, target);
        }
        return null;
    }

    @Override
    public MonsterTag[] getAffectedTags() {
        return new MonsterTag[] {
                MonsterTag.Knight
        };
    }

    @Override
    public String getEffectDescription() {
        return "Knights drown when hit by Water Spells";
    }
}
