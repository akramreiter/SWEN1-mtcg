package org.kramreiter.mtcg.card.rule;

import org.kramreiter.mtcg.card.Card;
import org.kramreiter.mtcg.card.CombatOutcome;
import org.kramreiter.mtcg.card.MonsterTag;
import org.kramreiter.mtcg.card.SpecialRule;

public class KrakenImmune implements SpecialRule {
    @Override
    public CombatOutcome executeRule(Card source, Card target) {
        if (!source.isSpell() && target.isSpell() && source.getTag() == MonsterTag.Kraken) {
            return new CombatOutcome(source.getName() + " is immune to spells and " + source.getName() + "proved useless against it", source, target);
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
    public String getEffectDescription() {
        return null;
    }
}
