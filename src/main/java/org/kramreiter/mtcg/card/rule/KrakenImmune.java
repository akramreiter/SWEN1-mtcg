package org.kramreiter.mtcg.card.rule;

import org.kramreiter.mtcg.card.*;

public class KrakenImmune implements SpecialRule {
    @Override
    public CombatOutcome executeRule(Card source, Card target) {
        if (!source.isSpell() && target.isSpell() && source.getTag() == MonsterTag.Kraken) {
            return new CombatOutcome("/w is immune to spells and /l proved useless against it", source, target);
        }
        return null;
    }

    @Override
    public MonsterTag[] getAffectedTags() {
        return new MonsterTag[] {
                MonsterTag.Kraken
        };
    }

    @Override
    public CardType[] getAffectedSpellTypes() {
        return new CardType[0];
    }

    @Override
    public String getEffectDescription() {
        return null;
    }
}
