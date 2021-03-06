package org.kramreiter.mtcg.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import org.hibernate.Session;
import org.kramreiter.mtcg.card.Card;
import org.kramreiter.mtcg.card.CardFactory;
import org.kramreiter.mtcg.card.GameMode;
import org.kramreiter.mtcg.comm.*;
import org.kramreiter.mtcg.user.CardFilter;
import org.kramreiter.mtcg.user.CardList;
import org.kramreiter.mtcg.user.Trade;
import org.kramreiter.mtcg.user.User;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MTCGApp implements ServerApp {
    Session session = ServerLaunch.getPostgresSession();
    ObjectMapper mapper = new ObjectMapper();
    CardFactory cardFactory = CardFactory.getInstance();
    @Setter
    PrintWriter writer;

    @Override
    public Response handleRequest(Request request) {
        ResponseContent output = new ResponseContent();
        try {
            ArrayList<Method> allowedMethods = new ArrayList<>();
            allowedMethods.add(Method.POST);
            allowedMethods.add(Method.PUT);
            if (!allowedMethods.contains(request.getMethod())) {
                ResponseContent out = new ResponseContent();
                out.setResponse(new String[] {""});
                return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{}");
            }
            RequestContent content = mapper.readValue(request.getBody(), RequestContent.class);
            switch (request.getPathname()) {
                case "login" -> output = login(content);
                case "register" -> output = register(content);
                case "setdeck" -> output = setDeck(content);
                case "queueup" -> output = queueUp(content);
                case "openpack" -> output = openPack(content);
                case "searchtrade" -> output = searchTrade(content);
                case "offertrade" -> output = offerTrade(content);
                case "accepttrade" -> output = acceptTrade(content);
                case "canceltrade" -> output = cancelTrade(content);
                case "getcollection" -> output = getCollection(content);
                case "getuserinfo" -> output = getUserInfo(content);
                case "gettrade" -> output = getTrade(content);
                case "getlibrary" -> output = getLibrary(content);
                default -> output.setResponse(new String[]{"[ERR] unknown command"});
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (output.getResponse().length > 0) {
            try {
                return new Response(HttpStatus.OK, ContentType.JSON, mapper.writer().writeValueAsString(output));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "[]");
    }

    public ResponseContent getLibrary(RequestContent request) {
        ResponseContent out = new ResponseContent();
        ArrayList<String> output = new ArrayList<>();
        for (Card c : cardFactory.getFullLibrary()) {
            output.add(c.toString());
        }
        out.setResponse(output.toArray(new String[0]));
        return out;
    }

    public ResponseContent login(RequestContent request) {
        ResponseContent out = new ResponseContent();
        if (request.getUsername() == null || request.getPassword() == null || request.getUsername().indexOf("'") > -1) {
            out.setResponse(new String[] {
                    "[ERR] command \"register\" failed: invalid input"
            });
            return out;
        }
        synchronized (ServerLaunch.TRANSACTION_LOCK) {
            session.beginTransaction();
            List<User> users = session.createQuery("from User where username = '" + request.getUsername() + "'").getResultList();
            if (users.size() < 1) {
                session.getTransaction().rollback();
                out.setResponse(new String[]{
                        "[ERR] command \"login\" failed: username doesn't exists"
                });
                return out;
            }
            if (!users.get(0).compareHashedPassword(request.getPassword())) {
                session.getTransaction().rollback();
                out.setResponse(new String[]{
                        "[ERR] command \"login\" failed: password doesn't match"
                });
                return out;
            }
            out.setToken(users.get(0).generateAccessToken());
            session.save(users.get(0));
            session.getTransaction().commit();
        }
        out.setResponse(new String[] {
                "[INFO] user \"" + request.getUsername() + "\" successfully logged in"
        });
        return out;
    }

    public ResponseContent register(RequestContent request) {
        ResponseContent out = new ResponseContent();
        if (request.getUsername() == null || request.getPassword() == null || request.getUsername().indexOf("'") > -1) {
            out.setResponse(new String[] {
                    "[ERR] command \"register\" failed: invalid input"
            });
            return out;
        }
        synchronized (ServerLaunch.TRANSACTION_LOCK) {
            session.beginTransaction();
            if (((Long) session.createQuery("select count(*) from User where username = '" + request.getUsername() + "'").iterate().next()).intValue() > 0) {
                session.getTransaction().rollback();
                out.setResponse(new String[]{
                        "[ERR] command \"register\" failed: username already exists"
                });
                return out;
            }
            User user = new User(request.getUsername(), request.getPassword());
            out.setToken(user.generateAccessToken());
            session.save(user);
            session.getTransaction().commit();
        }
        out.setResponse(new String[] {
                "[INFO] user \"" + request.getUsername() + "\" successfully registered"
        });
        return out;
    }

    public ResponseContent setDeck(RequestContent request) {
        ResponseContent out = new ResponseContent();
        User activeUser;
        try {
            activeUser = auth(request.getUsername(), request.getToken());
        } catch (IllegalAccessException e) {
            out.setResponse(new String[] {
                    "[ERR] command \"setdeck\" failed: user authentication failed"
            });
            return out;
        }
        CardList cl = new CardList();
        if (!cl.addCards(request.getCards())) {
            out.setResponse(new String[] {
                    "[ERR] command \"setdeck\" failed: cards for deck invalid"
            });
            return out;
        }
        for (String s : request.getCards()) {
            if (!activeUser.getOwnedCardlist().get().keySet().contains(s)) {
                out.setResponse(new String[] {
                        "[ERR] command \"setdeck\" failed: trying to add not owned cards to deck"
                });
                return out;
            }
        }
        synchronized (ServerLaunch.TRANSACTION_LOCK) {
            session.beginTransaction();
            boolean success = false;
            switch (request.getGameMode()) {
                case Classic -> success = activeUser.setDeckCardlistClassic(cl);
                case Structured -> success = activeUser.setDeckCardlistStructured(cl);
            }
            if (!success) {
                session.getTransaction().rollback();
                out.setResponse(new String[]{
                        "[ERR] command \"setdeck\" failed: invalid decklist"
                });
            } else {
                session.getTransaction().commit();
                ArrayList<String> output = new ArrayList<>();
                output.add("decklist set: ");
                for (String s : request.getCards()) {
                    output.add(cardFactory.getCard(s).toString());
                }
                out.setResponse(output.toArray(new String[0]));
            }
        }
        return out;
    }

    public ResponseContent queueUp(RequestContent request) {
        ResponseContent out = new ResponseContent();
        User activeUser;
        try {
            activeUser = auth(request.getUsername(), request.getToken());
        } catch (IllegalAccessException e) {
            out.setResponse(new String[]{
                    "[ERR] command \"queueup\" failed: user authentication failed"
            });
            return out;
        }
        try {
            CardList deck = new CardList();
            switch (request.getGameMode()) {
                case Classic -> deck = activeUser.getDeckCardlistClassic();
                case Structured -> deck = activeUser.getDeckCardlistStructured();
            }
            if (deck.size() < 1) throw new Exception();
            MatchmakingEngine engine = ServerLaunch.getMatchmakingEngine().get(request.getGameMode());
            engine.queuePlayer(activeUser.getUsername(), writer);
            out.setResponse(new String[]{
                    "Successfully entered queue"
            });
        } catch (Exception e) {
            out.setResponse(new String[]{
                    "[ERR] command \"queueup\" failed: entering queue failed"
            });
        }
        return out;
    }

    public ResponseContent openPack(RequestContent request) {
        ResponseContent out = new ResponseContent();
        User activeUser = null;
        try {
            activeUser = auth(request.getUsername(), request.getToken());
        } catch (IllegalAccessException e) {
            out.setResponse(new String[]{
                    "[ERR] command \"openpack\" failed: user authentication failed"
            });
            return out;
        }
        while (session.getTransaction().isActive()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String[] pack;
        synchronized (ServerLaunch.TRANSACTION_LOCK) {
            session.beginTransaction();
            pack = activeUser.openPack();
            session.update(activeUser);
            session.getTransaction().commit();
        }
        if (pack.length > 0) {
            ArrayList<String> output = new ArrayList<>();
            for (String s : pack) {
                output.add(cardFactory.getCard(s).toString());
            }
            out.setResponse(output.toArray(new String[0]));
        } else {
            out.setResponse(new String[] {
                    "[ERR] couldn't open pack, user didn't even have enough coins"
            });
        }
        return out;
    }

    public ResponseContent searchTrade(RequestContent request) {
        ResponseContent out = new ResponseContent();
        User activeUser;
        try {
            activeUser = auth(request.getUsername(), request.getToken());
        } catch (IllegalAccessException e) {
            out.setResponse(new String[]{
                    "[ERR] command \"searchtrade\" failed: user authentication failed"
            });
            return out;
        }
        List<Trade> trades = session.createQuery("from Trade where offeringUser != '" + request.getUsername() + "' and cardOffer = '" + request.getCards()[0] + "'").getResultList();
        return getTradesList(out, trades);
    }

    public ResponseContent getTrade(RequestContent request) {
        ResponseContent out = new ResponseContent();
        User activeUser;
        try {
            activeUser = auth(request.getUsername(), request.getToken());
        } catch (IllegalAccessException e) {
            out.setResponse(new String[]{
                    "[ERR] command \"accepttrade\" failed: user authentication failed"
            });
            return out;
        }
        List<Trade> trades = session.createQuery("from Trade where offeringUser = '" + request.getUsername() + "'").getResultList();
        return getTradesList(out, trades);
    }

    public ResponseContent offerTrade(RequestContent request) {
        ResponseContent out = new ResponseContent();

        User activeUser;
        try {
            activeUser = auth(request.getUsername(), request.getToken());
        } catch (IllegalAccessException e) {
            out.setResponse(new String[]{
                    "[ERR] command \"offertrade\" failed: user authentication failed"
            });
            return out;
        }
        try {
            CardFilter filter = mapper.readValue(request.getFilter(), CardFilter.class);
            CardList ownedCards = activeUser.getOwnedCardlist();
            if (!ownedCards.get().containsKey(request.getCards()[0])) throw new Exception();
            ownedCards.removeCard(request.getCards()[0]);
            Trade trade = new Trade(activeUser, request.getCards()[0], filter);
            synchronized (ServerLaunch.TRANSACTION_LOCK) {
                session.beginTransaction();
                activeUser.setOwnedCardlist(ownedCards);
                session.update(activeUser);
                session.save(trade);
                session.getTransaction().commit();
            }
            Card c = cardFactory.getCard(request.getCards()[0]);
            out.setResponse(new String[]{
                    c.getName() + " offered for trade"
            });
        } catch (Exception e) {
            out.setResponse(new String[]{
                    "[ERR] command \"offertrade\" failed: trade creation not successful"
            });
        }
        return out;
    }

    public ResponseContent acceptTrade(RequestContent request) {
        ResponseContent out = new ResponseContent();
        User activeUser;
        try {
            activeUser = auth(request.getUsername(), request.getToken());
        } catch (IllegalAccessException e) {
            out.setResponse(new String[]{
                    "[ERR] command \"accepttrade\" failed: user authentication failed"
            });
            return out;
        }
        try {
            Trade trade = (Trade) session.createQuery("from Trade where id = '" + Long.parseLong(request.getId()) + "'").getResultList().get(0);
            CardFilter filter = mapper.readValue(trade.getCardFilter(), CardFilter.class);
            if (filter.matchFilter(request.getCards()[0])) {
                User trader = (User) session.createQuery("from User where username = '" + trade.getOfferingUser() + "'").getResultList().get(0);
                CardList traderCl = trader.getOwnedCardlist();
                CardList userCl = activeUser.getOwnedCardlist();
                if (!userCl.removeCard(request.getCards()[0])) throw new Exception();
                synchronized (ServerLaunch.TRANSACTION_LOCK) {
                    try {
                        session.beginTransaction();
                        traderCl.addCard(request.getCards()[0]);
                        userCl.addCard(trade.getCardOffer());
                        activeUser.setOwnedCardlist(userCl);
                        trader.setOwnedCardlist(traderCl);
                        session.delete(trade);
                        session.update(activeUser);
                        session.update(trader);
                        session.getTransaction().commit();
                        out.setResponse(new String[] {
                                "trade successful, received " + cardFactory.getCard(trade.getCardOffer()).toString()
                        });
                    } catch (Exception e) {
                        session.getTransaction().rollback();
                        throw new Exception();
                    }
                }
            } else {
                out.setResponse(new String[]{
                        "[ERR] command \"accepttrade\" failed: card doesn't meet requirements"
                });
            }
        } catch (Exception e) {
            out.setResponse(new String[]{
                    "[ERR] command \"accepttrade\" failed: couldn't execute trade"
            });
        }
        return out;
    }

    public ResponseContent cancelTrade(RequestContent request) {
        ResponseContent out = new ResponseContent();
        User activeUser;
        try {
            activeUser = auth(request.getUsername(), request.getToken());
        } catch (IllegalAccessException e) {
            out.setResponse(new String[]{
                    "[ERR] command \"canceltrade\" failed: user authentication failed"
            });
            return out;
        }
        synchronized (ServerLaunch.TRANSACTION_LOCK) {
            session.beginTransaction();
            try {
                CardList ownedCards = activeUser.getOwnedCardlist();
                List<Trade> trades = session.createQuery("from Trade where offeringUser = '" + request.getUsername() + "' and cardOffer = '" + request.getCards()[0] + "'").getResultList();
                int count = 0;
                for (Trade t : trades) {
                    session.remove(t);
                    count++;
                    ownedCards.addCard(request.getCards()[0]);
                }
                activeUser.setOwnedCardlist(ownedCards);
                session.update(activeUser);
                session.getTransaction().commit();
                out.setResponse(new String[]{
                        count + " trades canceled"
                });
            } catch (Exception e) {
                session.getTransaction().rollback();
                out.setResponse(new String[]{
                        "[ERR] command \"canceltrade\" failed: couldn't cancel trades"
                });
            }
        }
        return out;
    }

    public ResponseContent getCollection(RequestContent request) {
        ResponseContent out = new ResponseContent();
        User activeUser;
        try {
            activeUser = auth(request.getUsername(), request.getToken());
        } catch (IllegalAccessException e) {
            out.setResponse(new String[]{
                    "[ERR] command \"getcollection\" failed: user authentication failed"
            });
            return out;
        }
        ArrayList<String> output = new ArrayList<>();
        CardList list = activeUser.getOwnedCardlist();
        output.add("total cards owned: " + list.size());
        for (String s : list.get().keySet()) {
            Card c = cardFactory.getCard(s);
            output.add(list.get().get(s) + "*" + s + ": " + c.toString());
        }
        out.setResponse(output.toArray(new String[0]));
        return out;
    }

    public ResponseContent getUserInfo(RequestContent request) {
        ResponseContent out = new ResponseContent();
        User activeUser;
        try {
            activeUser = auth(request.getUsername(), request.getToken());
        } catch (IllegalAccessException e) {
            out.setResponse(new String[]{
                    "[ERR] command \"getcollection\" failed: user authentication failed"
            });
            return out;
        }
        StringBuilder classicDeck = new StringBuilder();
        for (String s : activeUser.getDeckCardlistClassic().get().keySet()) {
            if (classicDeck.length() > 1) classicDeck.append(", ");
            classicDeck.append(cardFactory.getCard(s).getName());
        }
        StringBuilder structuredDeck = new StringBuilder();
        for (String s : activeUser.getDeckCardlistStructured().get().keySet()) {
            if (structuredDeck.length() > 1) structuredDeck.append(", ");
            structuredDeck.append(cardFactory.getCard(s).getName());
        }
        out.setResponse(new String[] {
                "user: " + activeUser.getUsername(),
                "coins: " + activeUser.getPayToWinCoins() + ", can afford " + activeUser.getPayToWinCoins() / 5 + " packs",
                "elo: " + activeUser.getElo(),
                "deck for classic mode: " + classicDeck.toString(),
                "deck for structured mode: " + structuredDeck.toString()
        });
        return out;
    }

    private User auth(String username, String token) throws IllegalAccessException {
        if (username == null || token == null || username.contains("'")) {
            throw new IllegalAccessException();
        }
        List<User> users = session.createQuery("from User where username = '" + username + "'").getResultList();
        if (users.size() < 1) {
            throw new IllegalAccessException();
        }
        if (!(Objects.equals(users.get(0).getAccessToken(), token))) {
            throw new IllegalAccessException();
        }
        return users.get(0);
    }

    private ResponseContent getTradesList(ResponseContent out, List<Trade> trades) {
        ArrayList<String> output = new ArrayList<>();
        output.add("Trades found: " + trades.size());
        for (Trade t : trades) {
            try {
                CardFilter f = mapper.readValue(t.getCardFilter(), CardFilter.class);
                output.add("Trade: " + t.getId() + ": " + t.getCardOffer() + " - " + cardFactory.getCard(t.getCardOffer().toString()));
                if (f.getCardId() != null) {
                    output.add("Requirement: " + f.getCardId() + " - " + cardFactory.getCard(f.getCardId().toString()));
                } else {
                    output.add("Requirement: " + f);
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        out.setResponse(output.toArray(new String[0]));
        return out;
    }
}
