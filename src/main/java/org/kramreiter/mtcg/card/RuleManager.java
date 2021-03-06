package org.kramreiter.mtcg.card;

import lombok.AccessLevel;
import lombok.Getter;
import org.kramreiter.mtcg.card.rule.*;

import java.util.Arrays;

public class RuleManager {
    @Getter()
    private static SpecialRule[] ruleset = initiateRuleset();

    private static SpecialRule[] initiateRuleset() {
        return new SpecialRule[] {
                new GoblinFear(),
                new KnightDrown(),
                new KrakenImmune(),
                new WizardControl(),
                new FireElfEvasion(),
                new UndeadBurn(),
                new DivineResist()
        };
    }

    public static double computeWeaknessMultiplier(CardType spellType, CardType monsterType) {
        return switch (spellType) {
            case Fire -> getWeakness(monsterType, CardType.Normal, CardType.Air, CardType.Water, CardType.Earth);
            case Air -> getWeakness(monsterType, CardType.Water, CardType.Earth, CardType.Fire, CardType.Normal);
            case Water -> getWeakness(monsterType, CardType.Fire, CardType.Earth, CardType.Air, CardType.Normal);
            case Earth -> getWeakness(monsterType, CardType.Normal, CardType.Fire, CardType.Water, CardType.Air);
            case Normal -> getWeakness(monsterType, CardType.Water, CardType.Air, CardType.Fire, CardType.Earth);
        };
    }

    private static double getWeakness(CardType monsterType, CardType str1, CardType str2, CardType weak1, CardType weak2) {
        if (monsterType == str1 || monsterType == str2) {
            return 2.0;
        } else if (monsterType == weak1 || monsterType == weak2) {
            return 0.5;
        } else {
            return 1.0;
        }
    }

    public static String getRelevantRules(Card card) {
        StringBuilder out = new StringBuilder();
        if (card.isSpell()) {
            CardType type = card.getCardType();
            for (SpecialRule rule : RuleManager.getRuleset()) {
                if (Arrays.asList(rule.getAffectedSpellTypes()).contains(type)) {
                    if (out.length() > 0) out.append("\n");
                    out.append(rule.getEffectDescription());
                }
            }
        } else {
            MonsterTag tag = card.getTag();
            if (tag == null) return null;
            for (SpecialRule rule : RuleManager.getRuleset()) {
                if (Arrays.asList(rule.getAffectedTags()).contains(tag)) {
                    if (out.length() > 0) out.append("\n");
                    out.append(rule.getEffectDescription());
                }
            }
        }
        return out.toString();
    }
}
