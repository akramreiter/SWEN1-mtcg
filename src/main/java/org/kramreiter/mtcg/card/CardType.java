package org.kramreiter.mtcg.card;

public enum CardType {
    Normal,
    Water,
    Fire,
    Earth,
    Air;

    public static CardType typeFromNumber(int type) {
        return switch (type) {
            case 1 -> Water;
            case 2 -> Fire;
            case 3 -> Earth;
            case 4 -> Air;
            default -> Normal;
        };
    }
}
