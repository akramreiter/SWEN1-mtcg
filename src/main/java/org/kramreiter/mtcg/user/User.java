package org.kramreiter.mtcg.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.kramreiter.mtcg.card.Card;

@Getter
@Setter
public class User {
    protected String username;
    @Setter(AccessLevel.NONE)
    protected String[] deckCardNames;
    @Setter(AccessLevel.NONE)
    protected String[] ownedCards;
    protected int elo;
    protected int payToWinCoins;

    public Card[] openPack() {
        if (payToWinCoins > 4) {
            payToWinCoins -= 5;
            double random = Math.random();
            if (random > 0.95) {

            } else if (random > 0.75) {

            } else {

            }
        }
        return new Card[0];
    }
}
