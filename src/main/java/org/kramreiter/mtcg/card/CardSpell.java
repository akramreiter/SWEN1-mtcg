package org.kramreiter.mtcg.card;

public class CardSpell extends Card {

    public CardSpell(String name, int baseStrength, int cardType, int rarity, String effect) {
        this.name = name;
        this.basePower = baseStrength;
        this.power = baseStrength;
        this.spell = true;
        this.cardType = CardType.typeFromNumber(cardType);
        this.rarity = Rarity.rarityFromNumber(rarity);
        this.effect = EffectFactory.getEffectFromString(effect);
    }
    public CardSpell(String name, int baseStrength, CardType cardType, Rarity rarity, UniqueEffect effect) {
        this.name = name;
        this.basePower = baseStrength;
        this.power = baseStrength;
        this.spell = true;
        this.cardType = cardType;
        this.rarity = rarity;
        this.effect = effect;
    }

}
