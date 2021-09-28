package org.kramreiter.mtcg.card;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class Card implements Cloneable {
    protected int baseStrength;
    @Setter
    protected int strength;
    protected CardType cardType;
    protected boolean spell;
    protected String name;
    /*
    0: Common
    1: Rare
    2: Epic
    3: Legendary
     */
    protected Rarity rarity;
    protected UniqueEffect effect;
    protected MonsterTag tag;
    @Setter
    protected String customWin;

    public Card clone() {
        try {
            return (Card) super.clone();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
