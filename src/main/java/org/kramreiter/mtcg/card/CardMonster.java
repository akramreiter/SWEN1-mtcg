package org.kramreiter.mtcg.card;

public class CardMonster extends Card {

    public CardMonster(String name, int baseStrength, int cardType, int rarity, String effect, int tag, String id) {
        this.name = name;
        this.basePower = baseStrength;
        this.power = baseStrength;
        this.spell = false;
        this.cardType = CardType.typeFromNumber(cardType);
        this.rarity = Rarity.rarityFromNumber(rarity);
        this.effect = EffectFactory.getEffectFromString(effect);
        this.tag = MonsterTag.tagFromNumber(tag);
        this.cardId = id;
    }
    public CardMonster(String name, int baseStrength, CardType cardType, Rarity rarity, UniqueEffect effect, MonsterTag tag, String id) {
        this.name = name;
        this.basePower = baseStrength;
        this.power = baseStrength;
        this.spell = false;
        this.cardType = cardType;
        this.rarity = rarity;
        this.effect = effect;
        this.tag = tag;
        this.cardId = id;
    }
}
