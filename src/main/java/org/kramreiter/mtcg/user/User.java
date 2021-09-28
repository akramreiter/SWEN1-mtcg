package org.kramreiter.mtcg.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.kramreiter.mtcg.card.Card;
import org.kramreiter.mtcg.card.CardFactory;
import org.kramreiter.mtcg.card.Rarity;

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
                return new Card[] {
                        CardFactory.getRandomCardForRarity(Rarity.Legendary),
                        CardFactory.getRandomCardForRarity(Rarity.Epic),
                        CardFactory.getRandomCardForRarity(Rarity.Rare),
                        CardFactory.getRandomCardForRarity(Rarity.Common),
                        CardFactory.getRandomCardForRarity(Rarity.Common)
                };
            } else if (random > 0.75) {
                return new Card[] {
                        CardFactory.getRandomCardForRarity(Rarity.Epic),
                        CardFactory.getRandomCardForRarity(Rarity.Rare),
                        CardFactory.getRandomCardForRarity(Rarity.Common),
                        CardFactory.getRandomCardForRarity(Rarity.Common),
                        CardFactory.getRandomCardForRarity(Rarity.Common)
                };
            } else {
                return new Card[] {
                        CardFactory.getRandomCardForRarity(Rarity.Rare),
                        CardFactory.getRandomCardForRarity(Rarity.Common),
                        CardFactory.getRandomCardForRarity(Rarity.Common),
                        CardFactory.getRandomCardForRarity(Rarity.Common),
                        CardFactory.getRandomCardForRarity(Rarity.Common)
                };
            }
        }
        return new Card[0];
    }
}
