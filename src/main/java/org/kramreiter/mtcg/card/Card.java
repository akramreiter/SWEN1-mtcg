package org.kramreiter.mtcg.card;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class Card implements Cloneable {
    protected int baseStrength;
    protected int strength;
    @Setter
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
    @Setter
    protected MonsterTag tag;
    @Setter
    protected String customWin;

    public Card clone() {
        try {
            return (Card) super.clone();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setStrength(int str) {
        this.strength = str;
        if (this.strength < 0) this.strength = 0;
    }

    public int computeStrengthAgainst(Card opponent) {
        int res = getStrength();
        if (isSpell()) {
            res *= RuleManager.computeWeaknessMultiplier(getCardType(), opponent.getCardType());
        }
        return res;
    }
}
