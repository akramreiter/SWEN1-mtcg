package org.kramreiter.mtcg.user;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;
import org.kramreiter.mtcg.card.Card;
import org.kramreiter.mtcg.card.CardFactory;
import org.kramreiter.mtcg.card.CardType;

import java.util.Objects;

@Getter
@Setter
public class CardFilter {
    @JsonAlias("card")
    String cardId;
    @JsonAlias("minvalue")
    int minStr = -1;
    @JsonAlias("matchtype")
    boolean matchType;
    @JsonAlias("type")
    String type;
    @JsonAlias("matchspell")
    boolean matchIsSpell;
    @JsonAlias("spell")
    boolean isSpell;
    @JsonAlias("matcheffect")
    boolean matchHasEffect;
    @JsonAlias("effect")
    boolean hasEffect;

    public boolean matchFilter(String card) {
        if (Objects.equals(card, cardId) && card != null) {
            return true;
        }
        Card c = CardFactory.getInstance().getCard(card);
        if (c.getBasePower() < minStr) {
            return false;
        }
        if (matchType && c.getCardType() != CardType.typeFromString(type)) {
            return false;
        }
        if (matchIsSpell && c.isSpell() != isSpell) {
            return false;
        }
        if (matchHasEffect && (c.getEffect() != null) == hasEffect) {
            return false;
        }
        return true;
    }

    public String toString() {
        String out = "";
        if (cardId != null) {
            out += "Card ID = " + cardId;
        }
        if (minStr > -1) {
            if (out.length() > 0) out += "; ";
            out += "Minimum Strength = " + minStr;
        }
        if (matchType) {
            if (out.length() > 0) out += "; ";
            out += "type = " + type;
        }
        if (matchIsSpell) {
            if (out.length() > 0) out += "; ";
            out += "Is spell: " + isSpell;
        }
        if (matchHasEffect) {
            if (out.length() > 0) out += "; ";
            out += "Has effect: " + hasEffect;
        }
        return out;
    }
}
