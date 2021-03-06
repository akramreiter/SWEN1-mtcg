package org.kramreiter.mtcg.card.rule;

import org.kramreiter.mtcg.card.*;

public class DivineResist implements SpecialRule {

    @Override
    public CombatOutcome executeRule(Card source, Card target) {
        if (!source.isSpell() && target.isSpell() && source.getTag() == MonsterTag.Divine && RuleManager.computeWeaknessMultiplier(target.getCardType(), source.getCardType()) > 1.1) {
            return new CombatOutcome("/w's divine protection saved it from the effect of /l", source, target);
        }
        return null;
    }

    @Override
    public MonsterTag[] getAffectedTags() {
        return new MonsterTag[] {
                MonsterTag.Divine
        };
    }

    @Override
    public CardType[] getAffectedSpellTypes() {
        return new CardType[0];
    }

    @Override
    public String getEffectDescription() {
        return "Divine monsters are immune to spells that would be very effective against them";
    }
}
