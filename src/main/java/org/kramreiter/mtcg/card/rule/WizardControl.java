package org.kramreiter.mtcg.card.rule;

import org.kramreiter.mtcg.card.*;

public class WizardControl implements SpecialRule {
    @Override
    public CombatOutcome executeRule(Card source, Card target) {
        if (!source.isSpell() && !target.isSpell() && source.getTag() == MonsterTag.Wizard && target.getTag() == MonsterTag.Ork) {
            return new CombatOutcome(target.getName() + "'s every move can be controlled by "
                    + source.getName() + "\nTheir \"fight\" was very one-sided", source, target);
        }
        return null;
    }

    @Override
    public MonsterTag[] getAffectedTags() {
        return new MonsterTag[] {
                MonsterTag.Wizard, MonsterTag.Ork
        };
    }

    @Override
    public String getEffectDescription() {
        return "Wizards can control Orks, allowing them those fights instantly";
    }
}
