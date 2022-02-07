package org.kramreiter.mtcg;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kramreiter.mtcg.card.CardFactory;
import org.kramreiter.mtcg.card.Deck;
import org.kramreiter.mtcg.user.CardList;
import org.kramreiter.mtcg.user.User;
import org.mockito.Mockito;

public class UserTests {
    User testUser;
    User testUser2;

    @BeforeEach
    public void setupUser() {
        testUser = new User("Pro Gamer", "asdfghjk");
        CardList list = new CardList();
        list.addCards(new String[] {
                "0001",
                "0002",
                "0003",
                "0004"
        });
        testUser.setDeckCardlistClassic(list);
        testUser.setElo(1500);
        testUser2 = new User("New Gamer", "asdfghjk");
        testUser2.setDeckCardlistClassic(list);
    }

    @Test
    public void userHasInitialCoins() {
        assert testUser.getPayToWinCoins() == 20;
    }

    @Test
    public void userHasInitialElo() {
        assert testUser2.getElo() == 1000;
    }

    @Test
    public void pullCardsWorks() {
        String[] cards = testUser.openPack();
        assert cards.length == 5;
    }

    @Test
    public void testEloMultiplier() {
        System.out.println("1000 wins against 1500 -> " + testUser2.computeEloGainForWin(testUser));
        System.out.println("1500 wins against 1000 -> " + testUser.computeEloGainForWin(testUser2));
        assert testUser2.computeEloGainForWin(testUser) > 40;
        assert testUser.computeEloGainForWin(testUser2) < 10;
    }
}
