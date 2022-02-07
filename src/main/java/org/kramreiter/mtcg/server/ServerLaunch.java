package org.kramreiter.mtcg.server;

import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.kramreiter.mtcg.card.GameMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ServerLaunch {
    private static final int port = 4930;
    @Getter
    private static Session postgresSession;
    @Getter
    private static Map<GameMode, MatchmakingEngine> matchmakingEngine = new HashMap<>();
    public static final Object TRANSACTION_LOCK = new Object();
    private static final Logger SERVERLOGGER = Logger.getLogger("server");

    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        try {
            SessionFactory sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
            postgresSession = sessionFactory.openSession();
        }
        catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            e.printStackTrace();
            System.out.println("Database connection failed. Shutting down...");
            System.exit(1);
        }
        matchmakingEngine.put(GameMode.Classic, new MatchmakingEngine(GameMode.Classic));
        matchmakingEngine.put(GameMode.Structured, new MatchmakingEngine(GameMode.Structured));
        Thread engineClassicThread = new Thread(matchmakingEngine.get(GameMode.Classic));
        Thread engineStructuredThread = new Thread(matchmakingEngine.get(GameMode.Structured));
        engineClassicThread.start();
        engineStructuredThread.start();

        ArrayList<RequestHandler> connections = new ArrayList<>();
        ServerRunner runner = new ServerRunner(port, connections);
        Thread runThread = new Thread(runner);
        runThread.start();
    }
    public static Logger getLogger() {
        return SERVERLOGGER;
    }
}
