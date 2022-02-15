package org.kramreiter.mtcg.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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
    private static int CLASSIC_DECK_SIZE = 4;
    private static int STRUCTURED_DECK_SIZE = 12;
    @NaturalId
    protected String username;
    protected int hashedPassword;
    protected String accessToken;
    protected String deckCardsClassic;
    protected String deckCardsStructured;
    @Setter(AccessLevel.PROTECTED)
    protected String ownedCards;
    protected int elo;
    protected int payToWinCoins = 20;
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
        ObjectMapper mapper = new ObjectMapper();
        try {
            ownedCards = mapper.writeValueAsString(new CardList());
            deckCardsClassic = mapper.writeValueAsString(new CardList());
            deckCardsStructured = mapper.writeValueAsString(new CardList());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    protected User() {
        this.elo = 1000;
        this.legendRoll = true;
    }

    public String[] openPack() {
        String[] pack = new String[0];
        if (payToWinCoins > 4) {
            payToWinCoins -= 5;
            double random = Math.random();
            if (random > 0.95 || legendRoll) {
                if (legendRoll) {
                    legendRoll = false;
                }
                pack = new String[] {
                        getCardFactory().getRandomCardIdForRarity(Rarity.Legendary),
                        getCardFactory().getRandomCardIdForRarity(Rarity.Epic),
                        getCardFactory().getRandomCardIdForRarity(Rarity.Rare),
                        getCardFactory().getRandomCardIdForRarity(Rarity.Common),
                        getCardFactory().getRandomCardIdForRarity(Rarity.Common)
                };
            } else if (random > 0.75) {
                pack = new String[] {
                        getCardFactory().getRandomCardIdForRarity(Rarity.Epic),
                        getCardFactory().getRandomCardIdForRarity(Rarity.Rare),
                        getCardFactory().getRandomCardIdForRarity(Rarity.Common),
                        getCardFactory().getRandomCardIdForRarity(Rarity.Common),
                        getCardFactory().getRandomCardIdForRarity(Rarity.Common)
                };
            } else {
                pack = new String[] {
                        getCardFactory().getRandomCardIdForRarity(Rarity.Rare),
                        getCardFactory().getRandomCardIdForRarity(Rarity.Common),
                        getCardFactory().getRandomCardIdForRarity(Rarity.Common),
                        getCardFactory().getRandomCardIdForRarity(Rarity.Common),
                        getCardFactory().getRandomCardIdForRarity(Rarity.Common)
                };
            }
            ObjectMapper mapper = new ObjectMapper();
            CardList cl;
            try {
                cl = mapper.readValue(ownedCards, CardList.class);
            } catch (Exception e) {
                cl = new CardList();
            }
            for (String card : pack) {
                cl.addCard(card);
            }
            try {
                ownedCards = mapper.writer().writeValueAsString(cl);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return pack;
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
    public boolean setDeckCardlistClassic(CardList list) {
        ObjectWriter writer = new ObjectMapper().writer();
        int cards = 0;
        for (String s : list.get().keySet()) {
            if (list.get().get(s) > 1) return false;
            cards++;
        }
        if (cards > CLASSIC_DECK_SIZE) return false;
        try {
            this.deckCardsClassic = writer.writeValueAsString(list);
            return true;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Transient
    public CardList getDeckCardlistClassic() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(this.deckCardsClassic, CardList.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new CardList();
    }

    @Transient
    public boolean setDeckCardlistStructured(CardList list) {
        ObjectWriter writer = new ObjectMapper().writer();
        int cards = 0;
        for (String s : list.get().keySet()) {
            if (list.get().get(s) > 1) return false;
            cards++;
        }
        if (cards > STRUCTURED_DECK_SIZE) return false;
        try {
            this.deckCardsStructured = writer.writeValueAsString(list);
            return true;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            this.deckCardsStructured = null;
        }
        return false;
    }

    @Transient
    public CardList getDeckCardlistStructured() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(this.deckCardsStructured, CardList.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new CardList();
    }

    @Transient
    public boolean setOwnedCardlist(CardList list) {
        ObjectWriter writer = new ObjectMapper().writer();
        try {
            this.ownedCards = writer.writeValueAsString(list);
            return true;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            this.deckCardsStructured = null;
        }
        return false;
    }

    @Transient
    public CardList getOwnedCardlist() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(this.ownedCards, CardList.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new CardList();
    }

    @Transient
    public String generateAccessToken() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            s.append((char) (48 + Math.random() * 75));
        }
        accessToken = s.toString();
        return accessToken;
    }

    @Transient
    public boolean compareHashedPassword(String pw) {
        if (pw.hashCode() == hashedPassword) return true;
        return false;
    }
}
