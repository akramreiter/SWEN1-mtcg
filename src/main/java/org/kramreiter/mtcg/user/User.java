package org.kramreiter.mtcg.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.kramreiter.mtcg.card.CardFactory;
import org.kramreiter.mtcg.card.Rarity;

@Getter
@Setter
public class User {
    private static int BASE_ELO_GAIN = 20;

    protected String username;
    protected String[] deckCards;
    @Setter(AccessLevel.NONE)
    protected String[] ownedCards;
    protected int elo;
    protected int payToWinCoins = 20;
    protected int freeRolls;
    protected boolean legendRoll;

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.NONE)
    private CardFactory cardFactory = CardFactory.getInstance();

    public User(String username) {
        this.username = username;
        this.elo = 1000;
        this.legendRoll = true;
    }

    public String[] openPack() {
        if (payToWinCoins > 4) {
            payToWinCoins -= 5;
            double random = Math.random();
            if (random > 0.95 || legendRoll) {
                if (legendRoll) {
                    legendRoll = false;
                }
                return new String[] {
                        getCardFactory().getRandomCardIdForRarity(Rarity.Legendary),
                        getCardFactory().getRandomCardIdForRarity(Rarity.Epic),
                        getCardFactory().getRandomCardIdForRarity(Rarity.Rare),
                        getCardFactory().getRandomCardIdForRarity(Rarity.Common),
                        getCardFactory().getRandomCardIdForRarity(Rarity.Common)
                };
            } else if (random > 0.75) {
                return new String[] {
                        getCardFactory().getRandomCardIdForRarity(Rarity.Epic),
                        getCardFactory().getRandomCardIdForRarity(Rarity.Rare),
                        getCardFactory().getRandomCardIdForRarity(Rarity.Common),
                        getCardFactory().getRandomCardIdForRarity(Rarity.Common),
                        getCardFactory().getRandomCardIdForRarity(Rarity.Common)
                };
            } else {
                return new String[] {
                        getCardFactory().getRandomCardIdForRarity(Rarity.Rare),
                        getCardFactory().getRandomCardIdForRarity(Rarity.Common),
                        getCardFactory().getRandomCardIdForRarity(Rarity.Common),
                        getCardFactory().getRandomCardIdForRarity(Rarity.Common),
                        getCardFactory().getRandomCardIdForRarity(Rarity.Common)
                };
            }
        }
        return new String[0];
    }

    public int computeEloGainForWin(User opponent) {
        int diff = Math.abs(getElo() - opponent.getElo());
        if (diff < 10) {
            diff = 10;
        }
        double multiplier = Math.log10(diff);
        if (getElo() > opponent.getElo()) {
            multiplier = 1 / multiplier;
        }
        return (int) (multiplier * BASE_ELO_GAIN);
    }

    public int adjustElo(int change) {
        elo += change;
        return elo;
    }
}
