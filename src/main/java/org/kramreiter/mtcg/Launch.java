package org.kramreiter.mtcg;

import org.kramreiter.mtcg.card.*;
import org.kramreiter.mtcg.user.User;

public class Launch {
    public static void main(String[] args) {
        User player1 = new User("player one");
        User player2 = new User("player two");

        player1.setDeckCards(new String[] {
                "3001",
                "3002",
                "3003",
                "3008"
        });
        player2.setDeckCards(new String[] {
                "3004",
                "3009",
                "3006",
                "3007"
        });

        String[] game = GameManager.runGame(player1, player2, GameMode.Structured);
        for (String s : game) {
            System.out.println(s);
        }
        System.out.println("\n\n\n");
        game = GameManager.runGame(player1, player2, GameMode.Classic);
        for (String s : game) {
            System.out.println(s);
        }
    }
}
