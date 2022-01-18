package org.kramreiter.mtcg.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import net.bytebuddy.implementation.bind.annotation.IgnoreForBinding;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;
import org.kramreiter.mtcg.card.CardFactory;
import org.kramreiter.mtcg.card.Rarity;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    private static int BASE_ELO_GAIN = 20;
    @NaturalId
    protected String username;
    protected int hashedPassword;
    protected String accessToken;
    protected String deckCards;
    @Setter(AccessLevel.PROTECTED)
    protected String ownedCards;
    protected int elo;
    protected int payToWinCoins = 20;
    protected int freeRolls;
    protected boolean legendRoll;

    protected boolean queueClassic;
    protected boolean queueStructured;

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.NONE)
    private static CardFactory cardFactory = CardFactory.getInstance();
    @Getter(AccessLevel.NONE)
    private Long id;

    public User(String username, String password) {
        this.username = username;
        setHashedPassword(password.hashCode());
        this.elo = 1000;
        this.legendRoll = true;
    }

    protected User() {
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

    /**
     * Calculates elo gain when winning against the given opponent.
     * If the difference between the opponent's elo and the user's own is lower than 100, it scales linearly up to *2.0.
     * At a difference of 100 or above, the multiplier is determined by the value of the log10(diff) instead.
     * This ensures a somewhat smooth curve for elo gain across the board
     * diff 100 -> 2.0 multiplier
     * diff 550 -> 3.0 multiplier
     * diff 5050 -> 4.0 multiplier
     * @param opponent User to compute gain against
     * @return elo gain
     */
    public int computeEloGainForWin(User opponent) {
        int diff = Math.abs(getElo() - opponent.getElo());
        double multiplier;
        if (diff < 100) {
            multiplier = 1.0 + (double) diff / 100;
        } else {
            multiplier = Math.log10(2 * diff - 100);
        }
        if (getElo() > opponent.getElo()) {
            multiplier = 1 / multiplier;
        }
        return (int) Math.round(multiplier * BASE_ELO_GAIN);
    }

    public int adjustElo(int change) {
        elo += change;
        return elo;
    }

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    public Long getId() {
        return id;
    }

    @Transient
    public void setDeckCardlist(CardList list) {
        ObjectWriter writer = new ObjectMapper().writer();
        try {
            this.deckCards = writer.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            this.deckCards = null;
        }
    }

    @Transient
    public CardList getDeckCardlist() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(this.deckCards, CardList.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new CardList();
    }
}
