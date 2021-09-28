package org.kramreiter.mtcg.card;

public enum Rarity {
    Common,
    Rare,
    Epic,
    Legendary;

    public static Rarity rarityFromNumber(int rarity) {
        return switch (rarity) {
            case 1 -> Rare;
            case 2 -> Epic;
            case 3 -> Legendary;
            default -> Common;
        };
    }
}
