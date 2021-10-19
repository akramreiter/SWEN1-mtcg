package org.kramreiter.mtcg.card;

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
    protected Rarity rarity;
    protected UniqueEffect effect;
    @Setter
    protected MonsterTag tag;
    @Setter
    protected String customWin;
    @Setter
    protected String ownerName;

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
            res = (int) (res * RuleManager.computeWeaknessMultiplier(getCardType(), opponent.getCardType()));
        }
        return res;
    }

    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append(getName()).append(" (");
        if (isSpell()) {
            out.append("Spell");
        } else {
            out.append("Monster");
            if (getTag() != null) {
                out.append(" - ").append(getTag());
            }
        }
        out.append(") [strength: ").
                append(getBaseStrength()).
                append("] {Rarity: ").
                append(getRarity()).
                append("; Type: ").
                append(getCardType());
        if (getEffect() != null) {
            out.append("; Effect: ").append(getEffect().getName());
        }
        out.append("}");
        return out.toString();
    }
}
