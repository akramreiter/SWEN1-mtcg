package org.kramreiter.mtcg;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.kramreiter.mtcg.card.*;
import org.kramreiter.mtcg.user.CardList;
import org.kramreiter.mtcg.user.User;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.SocketHandler;

public class Launch {
    private static SessionFactory sessionFactory;
    private static final int textDelay = 1;

    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            StandardServiceRegistryBuilder.destroy( registry );
            e.printStackTrace();
        }
        User player1 = new User("player one", "asdf");
        User player2 = new User("player two", "asdf");
        player1.setElo(1550);
        CardFactory factory = CardFactory.getInstance();

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(player1);
        session.save(player2);
        session.getTransaction().commit();
        session.close();

        CardList p1deck = new CardList();
        p1deck.addCards(new String[] {
                "3001",
                "2019",
                "3012",
                "2012"
        });
        CardList p2deck = new CardList();
        p2deck.addCards(new String[] {
                "3006",
                "3007",
                "2014",
                "2003"
        });

        player1.setDeckCardlistStructured(p1deck);
        player2.setDeckCardlistStructured(p2deck);

        /*player1.setPayToWinCoins(100);
        while (player1.getPayToWinCoins() > 4) {
            System.out.println("opening pack");
            for (String c : player1.openPack()) {
                System.out.println(factory.getCard(c).cardInfo() + "\n");
            }
        }*/

        String[] game = GameManager.runGame(player1, player2, GameMode.Structured);
        for (String s : game) {
            System.out.println(s);
            try {
                Thread.sleep(textDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("\n\nP1 Elo: " + player1.getElo() + ", P2 Elo: " + player2.getElo() + "\n\n");
        game = GameManager.runGame(player1, player2, GameMode.Classic);
        for (String s : game) {
            System.out.println(s);
            try {
                Thread.sleep(textDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("\n\nP1 Elo: " + player1.getElo() + ", P2 Elo: " + player2.getElo());
    }
}
