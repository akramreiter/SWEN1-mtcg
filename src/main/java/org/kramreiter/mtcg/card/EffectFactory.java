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
            case "Gamble" -> new Gamble();
            case "MorningRoutine" -> new MorningRoutine();
            case "CollateralDamage" -> new CollateralDamage();
            case "Inconsistency" -> new Inconsistency();
            case "RevolutionarySpirit" -> new RevolutionarySpirit();
            case "OrkImpact" -> new OrkImpact();
            case "BlazingAura" -> new BlazingAura();
            case "GatherUndead" -> new GatherUndead();
            case "DriedLands" -> new DriedLands();
            case "Nullify" -> new Nullify();
            case "CleanseUndead" -> new CleanseUndead();
            default -> null;
        };
    }
}
