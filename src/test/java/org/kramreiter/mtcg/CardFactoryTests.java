package org.kramreiter.mtcg;

import org.junit.jupiter.api.Test;
import org.kramreiter.mtcg.card.Card;
import org.kramreiter.mtcg.card.CardFactory;
import org.kramreiter.mtcg.card.Rarity;

public class CardFactoryTests {
    @Test
    void testGetCard() {
        Card c = CardFactory.getCard("3001");
        System.out.println(c);
        assert c != null;
    }

    @Test
    void testGetCardForLegendary() {
        Card c = CardFactory.getCard("3002", Rarity.Legendary);
        System.out.println(c);
        assert c != null;
    }
}
