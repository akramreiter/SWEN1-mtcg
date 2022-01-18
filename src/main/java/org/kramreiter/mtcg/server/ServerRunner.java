package org.kramreiter.mtcg.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerRunner implements Runnable {
    private ArrayList<RequestHandler> connections;
    private ArrayList<Thread> threads = new ArrayList<>();
    private int serverPort;
    private Logger LOGGER = ServerLaunch.getLogger();

    public ServerRunner(int port, ArrayList<RequestHandler> list) {
        serverPort = port;
        if (list == null) {
            connections = new ArrayList<>();
        } else {
            connections = list;
        }
    }

    @Override
    public void run() {
        try {
            LOGGER.log(Level.INFO, "Starting server socket");
            ServerSocket server = new ServerSocket(serverPort);
            while (true) {
                LOGGER.log(Level.INFO, "Listening for connections");
                Socket next = server.accept();
                LOGGER.log(Level.INFO, "ServerRunner -> new Client connected");
                RequestHandler nextThread = new RequestHandler(next, new MTCGApp());
                Thread t = new Thread(nextThread);
                connections.add(nextThread);
                threads.add(t);
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
