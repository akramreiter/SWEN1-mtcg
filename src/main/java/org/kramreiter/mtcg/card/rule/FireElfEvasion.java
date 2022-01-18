package org.kramreiter.mtcg.card.rule;

import org.kramreiter.mtcg.card.*;

public class FireElfEvasion implements SpecialRule {
    @Override
    public CombatOutcome executeRule(Card source, Card target) {
        if (!source.isSpell() && !target.isSpell() && source.getTag() == MonsterTag.FireElf && target.getTag() == MonsterTag.Dragon) {
            return new CombatOutcome("/w easily dodged the attacks of /l", source, target);
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
    public CardType[] getAffectedSpellTypes() {
        return new CardType[0];
    }

    @Override
    public String getEffectDescription() {
        return "Fire Elves can evade attacks from Dragons";
    }
}
