package org.kramreiter.mtcg.card;

import java.util.Locale;

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

    public static CardType typeFromString(String type) {
        return switch (type.toLowerCase()) {
            case "water" -> Water;
            case "fire" -> Fire;
            case "earth" -> Earth;
            case "air" -> Air;
            default -> Normal;
        };
    }

    public String toString() {
        return switch (this) {
            case Normal -> "Normal";
            case Water -> "Water";
            case Fire -> "Fire";
            case Earth -> "Earth";
            case Air -> "Air";
        };
    }
}
