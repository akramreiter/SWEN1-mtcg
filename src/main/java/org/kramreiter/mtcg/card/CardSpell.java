package org.kramreiter.mtcg.card;

public class CardSpell extends Card {

    public CardSpell(String name, int baseStrength, int cardType, boolean spell, int rarity, String effect) {
        this.name = name;
        this.baseStrength = baseStrength;
        this.strength = baseStrength;
        this.spell = spell;
        this.cardType = CardType.typeFromNumber(cardType);
        this.rarity = Rarity.rarityFromNumber(rarity);
        this.effect = null;
    }
    public CardSpell(String name, int baseStrength, CardType cardType, boolean spell, Rarity rarity, UniqueEffect effect) {
        this.name = name;
        this.baseStrength = baseStrength;
        this.strength = baseStrength;
        this.spell = spell;
        this.cardType = cardType;
        this.rarity = rarity;
        this.effect = effect;
    }

}
