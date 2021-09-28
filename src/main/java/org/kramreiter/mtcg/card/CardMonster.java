package org.kramreiter.mtcg.card;

public class CardMonster extends Card {

    public CardMonster(String name, int baseStrength, int cardType, boolean spell, int rarity, String effect, int tag) {
        this.name = name;
        this.baseStrength = baseStrength;
        this.strength = baseStrength;
        this.spell = spell;
        this.cardType = CardType.typeFromNumber(cardType);
        this.rarity = Rarity.rarityFromNumber(rarity);
        this.effect = null;
        this.tag = MonsterTag.tagFromNumber(tag);
    }
    public CardMonster(String name, int baseStrength, CardType cardType, boolean spell, Rarity rarity, UniqueEffect effect, MonsterTag tag) {
        this.name = name;
        this.baseStrength = baseStrength;
        this.strength = baseStrength;
        this.spell = spell;
        this.cardType = cardType;
        this.rarity = rarity;
        this.effect = effect;
        this.tag = tag;
    }
}
