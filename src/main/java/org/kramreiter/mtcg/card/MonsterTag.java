package org.kramreiter.mtcg.card;

import java.util.Arrays;

public enum MonsterTag {
    Goblin, Dragon, Knight, Kraken, Ork, Wizard, Undead, Divine, FireElf;

    public static MonsterTag tagFromNumber(int tag) {
        return switch (tag) {
            case 1 -> Dragon;
            case 2 -> Knight;
            case 3 -> Kraken;
            case 4 -> Ork;
            case 5 -> Wizard;
            case 6 -> Undead;
            case 7 -> Divine;
            case 8 -> FireElf;
            case 9 -> Goblin;
            default -> null;
        };
    }
}
