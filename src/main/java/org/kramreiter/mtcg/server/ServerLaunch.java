package org.kramreiter.mtcg.server;

import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;
import java.util.logging.Logger;

public class ServerLaunch {
    private static final Logger SERVERLOGGER = Logger.getLogger("server");
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        ArrayList<RequestHandler> connections = new ArrayList<>();
        ServerRunner runner = new ServerRunner(4930, connections);
        Thread runThread = new Thread(runner);
        runThread.start();
    }
    public static Logger getLogger() {
        return SERVERLOGGER;
    }
}
