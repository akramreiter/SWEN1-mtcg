package org.kramreiter.mtcg;

import org.junit.jupiter.api.Test;
import org.kramreiter.mtcg.card.Card;
import org.kramreiter.mtcg.card.CardFactory;
import org.kramreiter.mtcg.card.Rarity;

public class CardFactoryTests {
    CardFactory cardFactory = CardFactory.getInstance();

    @Test
    void testGetCard() {
        Card c = cardFactory.getCard("3001");
        System.out.println(c);
        assert c != null;
    }

    @Test
    void testGetNonexistentCard() {
        //rarity for doesn't exist, therefore this card shouldn't be found
        Card c = cardFactory.getCard("4001");
        System.out.println(c);
        assert c == null;
    }

    @Test
    void testGetCardForLegendary() {
        Card c = cardFactory.getCard("3002", Rarity.Legendary);
        System.out.println(c);
        assert c != null;
    }

    @Test
    void testGetNonexistentCardForRarity() {
        Card c = cardFactory.getCard("0001", Rarity.Epic);
        System.out.println(c);
        assert c == null;
    }
}
