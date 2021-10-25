package org.kramreiter.mtcg;

import org.kramreiter.mtcg.card.*;
import org.kramreiter.mtcg.cliclient.CommThread;
import org.kramreiter.mtcg.user.User;

public class Launch {
    private static final int textDelay = 100;

    public static void main(String[] args) {
        User player1 = new User("player one");
        User player2 = new User("player two");

        CardFactory factory = CardFactory.getInstance();

        player1.setDeckCards(new String[] {
                "3001",
                "2019",
                "3012",
                "2012"
        });
        player2.setDeckCards(new String[] {
                "3006",
                "3007",
                "2014",
                "2003"
        });

        player1.setPayToWinCoins(100);
        while (player1.getPayToWinCoins() > 4) {
            System.out.println("opening pack");
            for (String c : player1.openPack()) {
                System.out.println(factory.getCard(c));
            }
        }

        String[] game = GameManager.runGame(player1, player2, GameMode.Structured);
        for (String s : game) {
            System.out.println(s);
            try {
                Thread.sleep(textDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("\n\n\n");
        game = GameManager.runGame(player1, player2, GameMode.Classic);
        for (String s : game) {
            System.out.println(s);
            try {
                Thread.sleep(textDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
