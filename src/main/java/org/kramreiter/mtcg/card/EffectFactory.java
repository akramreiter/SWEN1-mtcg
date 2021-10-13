package org.kramreiter.mtcg.card;

import org.kramreiter.mtcg.card.effect.*;

public class EffectFactory {
    public static UniqueEffect getEffectFromString(String effectname) {
        if (effectname == null) return null;
        return switch (effectname) {
            case "Ascent" -> new Ascent();
            case "Blessing" -> new Blessing();
            case "Curse" -> new Curse();
            case "Extinction" -> new Extinction();
            case "GearUp" -> new GearUp();
            case "GreatWave" -> new GreatWave();
            case "Incinerate" -> new Incinerate();
            case "Scorch" -> new Scorch();
            case "SelfSeal" -> new SelfSeal();
            case "Sharpen" -> new Sharpen();
            case "SpellThief" -> new SpellThief();
            case "Swap" -> new Swap();
            case "Uprising" -> new Uprising();
            case "WornOut" -> new WornOut();
            default -> null;
        };
    }
}
