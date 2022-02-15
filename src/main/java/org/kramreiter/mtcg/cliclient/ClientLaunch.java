package org.kramreiter.mtcg.cliclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.kramreiter.mtcg.card.CardFactory;
import org.kramreiter.mtcg.card.CardType;
import org.kramreiter.mtcg.card.GameMode;
import org.kramreiter.mtcg.comm.*;
import org.kramreiter.mtcg.user.CardFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;

public class ClientLaunch {
    static ObjectMapper mapper = new ObjectMapper();
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.println("Enter server ip address + port");
        Socket sock = null;
        PrintWriter sock_out = null;
        BufferedReader sock_in = null;
        try {
            String[] input = s.nextLine().split(":");
            String ip_adr = input[0];
            int port_nr = Integer.parseInt(input[1]);
            sock = new Socket(ip_adr, port_nr);
            sock_out = new PrintWriter(sock.getOutputStream(), true);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Server connection failed. Shutting down...");
            System.exit(1);
        }
        CommVars vars = new CommVars();
        Thread comm = new Thread(new CommThread(sock, vars));
        comm.start();
        String input;
        String uname = "";
        while (true) {
            Request req = new Request();
            if (!vars.isLoginStatus()) {
                vars.setResponseReceived(false);
                System.out.println("Enter \"r\" to register or leave empty to login\r\nEnter \"exit\" to quit");
                input = s.nextLine().trim();
                if (input.startsWith("exit")) {
                    try {
                        sock.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.exit(0);
                }
                boolean isRegister = input.startsWith("r");
                input = "";
                while (input.length() < 1) {
                    System.out.println("Enter username");
                    input = s.nextLine().trim();
                }
                String username = input;
                input = "";
                while (input.length() < 1) {
                    System.out.println("Enter password");
                    input = s.nextLine().trim();
                }
                String password = input;
                req.setMethod(Method.POST);
                req.setContentType(ContentType.JSON.type);
                RequestContent reqContent = new RequestContent();
                reqContent.setUsername(username);
                reqContent.setPassword(password);
                if (isRegister) {
                    req.setPathname("register");
                } else {
                    req.setPathname("login");
                }
                try {
                    req.setBody(mapper.writeValueAsString(reqContent));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                req.setContentLength(req.getBody().length());
                sock_out.printf(req.get());
                while (!vars.responseReceived) {
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {}
                }
                if (!Objects.equals(vars.getToken(), "")) {
                    uname = username;
                }
            } else {
                System.out.println("Enter your operation (\"help\" for full list)");
                input = s.nextLine().trim();
                RequestContent content = new RequestContent();
                content.setUsername(uname);
                content.setToken(vars.getToken());
                req.setMethod(Method.POST);
                req.setContentType(ContentType.JSON.type);
                switch (input) {
                case "help" -> System.out.println(
                        """
                                getcollection: view your card collection
                                getuserinfo: view your account information
                                gettrade: list your trades
                                getlibrary: view all cards in game
                                queueup: enter queue for games
                                setdeck: configure your deck
                                openpack: open a pack of card (costs coins)
                                offertrade: offer your cards up for trading
                                canceltrade: cancel ongoing trades
                                searchtrade: search for public trades
                                accepttrade: accept a specific trade offer
                                exit: close program"""
                );
                case "getcollection" -> getCollection(sock_out, req, content);
                case "getuserinfo" -> getUserInfo(sock_out, req, content);
                case "gettrade" -> getTrade(sock_out, req, content);
                case "openpack" -> openPack(sock_out, req, content);
                case "queueup" -> queueUp(sock_out, req, content, s);
                case "setdeck" -> setDeck(sock_out, req, content, s);
                case "offertrade" -> offerTrade(sock_out, req, content, s);
                case "accepttrade" -> acceptTrade(sock_out, req, content, s);
                case "searchtrade" -> searchTrade(sock_out, req, content, s);
                case "canceltrade" -> cancelTrade(sock_out, req, content, s);
                case "getlibrary" -> getLibrary(sock_out, req, content);
                case "exit" -> {
                    try {
                        sock.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.exit(0);
                }
                default -> System.out.println("unknown command");
                }
                System.out.println(input);
            }
        }
    }

    private static void getLibrary(PrintWriter writer, Request request, RequestContent content) {
        request.setPathname("getlibrary");
        try {
            String body = mapper.writeValueAsString(content);
            request.setBody(body);
            request.setContentLength(body.length());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        writer.printf(request.get());
    }

    private static void searchTrade(PrintWriter writer, Request request, RequestContent content, Scanner scanner) {
        request.setPathname("searchtrade");
        String cardId = null, in = null;
        do {
            System.out.println("Enter card ID to search trades for"
                    + "\nAlternatively, enter \"e\" to exit");
            in = scanner.nextLine().trim();
            if (CardFactory.getInstance().getCard(in) != null) {
                cardId = in;
            } else if (in.startsWith("e")) {
                return;
            }
        } while (cardId == null);
        content.setCards(new String[]{
                cardId
        });
        try {
            String body = mapper.writeValueAsString(content);
            request.setBody(body);
            request.setContentLength(body.length());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        writer.printf(request.get());
    }

    private static void acceptTrade(PrintWriter writer, Request request, RequestContent content, Scanner scanner) {
        request.setPathname("accepttrade");
        String cardId = null, in = null, tradeId = null;
        do {
            System.out.println("Enter trade ID to accept"
                    + "\nAlternatively, enter \"e\" to exit");
            in = scanner.nextLine().trim();
            boolean checkNumeric = true;
            try {
                Integer.parseInt(in);
            } catch (Exception e) {
                checkNumeric = false;
            }
            if (checkNumeric) {
                tradeId = in;
            } else if (in.startsWith("e")) {
                return;
            }
        } while (tradeId == null);
        do {
            System.out.println("Enter card ID to trade"
                    + "\nAlternatively, enter \"e\" to exit");
            in = scanner.nextLine().trim();
            if (CardFactory.getInstance().getCard(in) != null) {
                cardId = in;
            } else if (in.startsWith("e")) {
                return;
            }
        } while (cardId == null);
        content.setCards(new String[]{
                cardId
        });
        content.setId(tradeId);
        try {
            String body = mapper.writeValueAsString(content);
            request.setBody(body);
            request.setContentLength(body.length());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        writer.printf(request.get());
    }

    private static void cancelTrade(PrintWriter writer, Request request, RequestContent content, Scanner scanner) {
        request.setPathname("canceltrade");
        String cardId = null, in = null;
        do {
            System.out.println("Enter card ID to retract from trades"
                    + "\nAlternatively, enter \"e\" to exit");
            in = scanner.nextLine().trim();
            if (CardFactory.getInstance().getCard(in) != null) {
                cardId = in;
            } else if (in.startsWith("e")) {
                return;
            }
        } while (cardId == null);
        content.setCards(new String[]{
                cardId
        });
        try {
            String body = mapper.writeValueAsString(content);
            request.setBody(body);
            request.setContentLength(body.length());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        writer.printf(request.get());
    }

    private static void offerTrade(PrintWriter writer, Request request, RequestContent content, Scanner scanner) {
        request.setPathname("offertrade");
        String offer = null, type = null, in;
        do {
            System.out.println("Enter card ID to offer for trade"
                    + "\nAlternatively, enter \"e\" to exit");
            in = scanner.nextLine().trim();
            if (CardFactory.getInstance().getCard(in) != null) {
                offer = in;
            } else if (in.startsWith("e")) {
                return;
            }
        } while (offer == null);
        content.setCards(new String[]{
                offer
        });
        CardFilter filter = new CardFilter();
        int str;
        System.out.println("Search for specific card (\"c\") or leave empty to create custom filter");
        in = scanner.nextLine().trim();
        if (in.startsWith("c")) {
            do {
                System.out.println("Enter card ID to search for trade"
                        + "\nAlternatively, enter \"e\" to exit");
                in = scanner.nextLine().trim();
                if (CardFactory.getInstance().getCard(in) != null) {
                    filter.setCardId(in);
                } else if (in.startsWith("e")) {
                    return;
                }
            } while (filter.getCardId() == null);
        } else {
            do {
                System.out.println("Set minimum card strength"
                        + "\nAlternatively, enter \"e\" to exit");
                in = scanner.nextLine().trim();
                if (in.startsWith("e")) {
                    return;
                } else {
                    try {
                        filter.setMinStr(Integer.parseInt(in));
                    } catch (Exception e) {}
                }
            } while (filter.getMinStr() < 0);
            System.out.println("Filter for type (\"y\")?");
            filter.setMatchType(scanner.nextLine().startsWith("y"));
            if (filter.isMatchType()) {
                System.out.println("Enter type:");
                filter.setType(CardType.typeFromString(scanner.nextLine().trim()).toString());
            }
            System.out.println("Filter for spell (\"y\")?");
            filter.setMatchIsSpell(scanner.nextLine().startsWith("y"));
            if (filter.isMatchIsSpell()) {
                System.out.println("Card has to be spell (\"y\") or creature (empty)");
                filter.setSpell(scanner.nextLine().startsWith("y"));
            }
            System.out.println("Filter for effect (\"y\")?");
            filter.setMatchHasEffect(scanner.nextLine().startsWith("y"));
            if (filter.isMatchHasEffect()) {
                System.out.println("Card has an effect (\"y\") or not (empty)");
                filter.setHasEffect(scanner.nextLine().startsWith("y"));
            }
        }

        try {
            content.setFilter(mapper.writeValueAsString(filter));
            String body = mapper.writeValueAsString(content);
            request.setBody(body);
            request.setContentLength(body.length());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        writer.printf(request.get());
    }

    private static void getCollection(PrintWriter writer, Request request, RequestContent content) {
        request.setPathname("getcollection");
        try {
            String body = mapper.writeValueAsString(content);
            request.setBody(body);
            request.setContentLength(body.length());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        writer.printf(request.get());
    }

    private static void getUserInfo(PrintWriter writer, Request request, RequestContent content) {
        request.setPathname("getuserinfo");
        try {
            String body = mapper.writeValueAsString(content);
            request.setBody(body);
            request.setContentLength(body.length());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        writer.printf(request.get());
    }

    private static void getTrade(PrintWriter writer, Request request, RequestContent content) {
        request.setPathname("gettrade");
        try {
            String body = mapper.writeValueAsString(content);
            request.setBody(body);
            request.setContentLength(body.length());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        writer.printf(request.get());
    }

    private static void openPack(PrintWriter writer, Request request, RequestContent content) {
        request.setPathname("openpack");
        try {
            String body = mapper.writeValueAsString(content);
            request.setBody(body);
            request.setContentLength(body.length());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        writer.printf(request.get());
    }

    private static void queueUp(PrintWriter writer, Request request, RequestContent content, Scanner scanner) {
        request.setPathname("queueup");
        GameMode mode = null;
        do {
            System.out.println("Enter gamemode; \"c\" -> classic, \"s\" -> structured");
            String in = scanner.nextLine().trim();
            System.out.println(in);
            if (in.startsWith("c")) {
                mode = GameMode.Classic;
            } else if (in.startsWith("s")) {
                mode = GameMode.Structured;
            }
        } while (mode == null);
        content.setGameMode(mode);
        try {
            String body = mapper.writeValueAsString(content);
            request.setBody(body);
            request.setContentLength(body.length());
            writer.printf(request.get());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private static void setDeck(PrintWriter writer, Request request, RequestContent content, Scanner scanner) {
        request.setPathname("setdeck");
        HashSet<String> deck = new HashSet<>();
        GameMode mode = null;
        int decksize = 0;
        do {
            System.out.println("Enter gamemode; \"c\" -> classic, \"s\" -> structured");
            String in = scanner.nextLine().trim();
            System.out.println(in);
            if (in.startsWith("c")) {
                mode = GameMode.Classic;
                decksize = 4;
            } else if (in.startsWith("s")) {
                mode = GameMode.Structured;
                decksize = 12;
            }
        } while (mode == null);
        content.setGameMode(mode);
        do {
            System.out.println("Enter card ID to add to deck (current deck size: " + deck.size() + ")"
            + "\nAlternatively, enter \"e\" to exit deck creation or \"s\" to submit deck with less than full size");
            String in = scanner.nextLine().trim();
            if (CardFactory.getInstance().getCard(in) != null) {
                deck.add(in);
            } else if (in.startsWith("e")) {
                return;
            } else if (in.startsWith("s")) {
                break;
            }
        } while (deck.size() < decksize);
        if (deck.size() > 0) {
            content.setCards(deck.toArray(new String[0]));
            try {
                String body = mapper.writeValueAsString(content);
                request.setBody(body);
                request.setContentLength(body.length());
                writer.printf(request.get());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("no cards in deck, stopping deck creation");
        }
    }
}
