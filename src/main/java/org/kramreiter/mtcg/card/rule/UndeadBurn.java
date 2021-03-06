package org.kramreiter.mtcg.card.rule;

import org.kramreiter.mtcg.card.*;

public class UndeadBurn implements SpecialRule {
    @Override
    public CombatOutcome executeRule(Card source, Card target) {
        if (source.isSpell() && !target.isSpell() && source.getCardType() == CardType.Fire && target.getTag() == MonsterTag.Undead) {
            return new CombatOutcome("/l was instantly burned by /w", source, target);
        }
        return null;
    }

    @Override
    public MonsterTag[] getAffectedTags() {
        return new MonsterTag[] {
                MonsterTag.Undead
        };
    }

    @Override
    public CardType[] getAffectedSpellTypes() {
        return new CardType[] {
                CardType.Fire
        };
    }

    @Override
    public String getEffectDescription() {
        return "Undead get burned instantly when exposed to Fire spells";
    }
}
