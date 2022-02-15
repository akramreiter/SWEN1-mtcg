package org.kramreiter.mtcg.card;

import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class Card implements Cloneable {
    protected int basePower;
    protected int power;
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
    protected String cardId;

    public Card clone() {
        try {
            return (Card) super.clone();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setPower(int str) {
        this.power = str;
        if (this.power < 0) this.power = 0;
    }

    public int computeStrengthAgainst(Card opponent) {
        int res = this.getPower();
        if (isSpell()) {
            res = (int) (res * RuleManager.computeWeaknessMultiplier(getCardType(), opponent.getCardType()));
        }
        return res;
    }

    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append(getCardId()).append(": ").append(getName()).append(" (");
        if (isSpell()) {
            out.append("Spell");
        } else {
            out.append("Monster");
            if (getTag() != null) {
                out.append(" - ").append(getTag());
            }
        }
        out.append(")[base power: ").
                append(this.getBasePower());
        if (this.getBasePower() != this.getPower()) {
            out.append("; current power: ").append(this.getPower());
        }
        out.append("]{Rarity: ").
                append(getRarity()).
                append("; Type: ").
                append(getCardType());
        if (getEffect() != null) {
            out.append("; Effect: ").append(getEffect().getName());
        }
        out.append("}");
        return out.toString();
    }

    public String cardInfo() {
        String output = "" + toString();
        if (getEffect() != null) {
            output += "\n" + getEffect().getDescription();
        }
        String rules = RuleManager.getRelevantRules(this);
        if (rules != null) {
            output += "\nRules:\n" + rules;
        }
        return output;
    }
}
