package org.kramreiter.mtcg.card.rule;

import org.kramreiter.mtcg.card.*;

public class KnightDrown implements SpecialRule {
    @Override
    public CombatOutcome executeRule(Card source, Card target) {
        if (source.isSpell() && !target.isSpell() && source.getCardType() == CardType.Water && target.getTag() == MonsterTag.Knight) {
            return new CombatOutcome("/l was unable to escape the flood created by /w and drowned", source, target);
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
