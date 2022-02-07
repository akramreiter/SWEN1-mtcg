package org.kramreiter.mtcg.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.kramreiter.mtcg.card.GameManager;
import org.kramreiter.mtcg.card.GameMode;
import org.kramreiter.mtcg.comm.ContentType;
import org.kramreiter.mtcg.comm.HttpStatus;
import org.kramreiter.mtcg.comm.Response;
import org.kramreiter.mtcg.comm.ResponseContent;
import org.kramreiter.mtcg.user.User;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MatchmakingEngine implements Runnable {
    private GameMode mode;
    private HashMap<String, PrintWriter> queuedUsers = new HashMap<>();
    @Getter
    @Setter
    private boolean active = true;

    public MatchmakingEngine(GameMode mode) {
        this.mode = mode;
    }

    @Override
    public void run() {
        while (active) {
            List<String> users = new ArrayList<>(queuedUsers.keySet().stream().toList());
            while (users.size() > 1) {
                String player1 = users.remove((int)(Math.random() * users.size()));
                String player2 = users.remove((int)(Math.random() * users.size()));
                PrintWriter player1out = queuedUsers.get(player1);
                PrintWriter player2out = queuedUsers.get(player2);
                String[] output = null;

                lock: synchronized (ServerLaunch.TRANSACTION_LOCK) {
                    Session session = ServerLaunch.getPostgresSession();
                    session.beginTransaction();
                    List<User> p1user = session.createQuery("from User where username = '" + player1 + "'").getResultList();
                    List<User> p2user = session.createQuery("from User where username = '" + player2 + "'").getResultList();

                    if (p1user.size() < 1) {
                        session.getTransaction().rollback();
                        queuedUsers.remove(player1);
                        break lock;
                    }
                    if (p2user.size() < 1) {
                        session.getTransaction().rollback();
                        queuedUsers.remove(player2);
                        break lock;
                    }
                    queuedUsers.remove(player1);
                    queuedUsers.remove(player2);
                    User user1 = p1user.get(0);
                    User user2 = p2user.get(0);
                    output = GameManager.runGame(user1, user2, mode);
                    queuedUsers.remove(player1);
                    queuedUsers.remove(player2);
                    session.update(user1);
                    session.update(user2);
                    session.getTransaction().commit();
                }
                ResponseContent content = new ResponseContent();
                content.setResponse(output);
                ObjectMapper mapper = new ObjectMapper();
                Response resp;
                try {
                    resp = new Response(HttpStatus.OK, ContentType.JSON, mapper.writer().writeValueAsString(content));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    continue;
                }
                try {
                    player1out.printf(resp.get());
                } catch (Exception e) {}
                try {
                    player2out.printf(resp.get());
                } catch (Exception e) {}
            }
            try {
                Thread.sleep(200);
            } catch (Exception e) {}
        }
    }

    public void queuePlayer(String playerName, PrintWriter writer) {
        queuedUsers.put(playerName, writer);
    }
}
