package org.kramreiter.mtcg.card;

import java.util.Arrays;

public enum MonsterTag {
    Goblin, Dragon, Knight, Kraken, Ork, Wizard, Undead, Divine, FireElf;

    public static MonsterTag tagFromNumber(int tag) {
        return switch (tag) {
            case 0 -> Goblin;
            case 1 -> Dragon;
            case 2 -> Knight;
            case 3 -> Kraken;
            case 4 -> Ork;
            case 5 -> Wizard;
            case 6 -> Undead;
            case 7 -> Divine;
            case 8 -> FireElf;
            default -> null;
        };
    }

    public static String getRuleEffects(MonsterTag tag) {
        if (tag == null) return "Card has no tag, no special rules apply";
        String out = "";
        for (SpecialRule rule : CardManager.getRuleset()) {
            if (Arrays.asList(rule.getAffectedTags()).contains(tag)) {
                if (out.length() < 1) out += "\n";
                out += rule.getEffectDescription();
            }
        }
        return out;
    }
}
