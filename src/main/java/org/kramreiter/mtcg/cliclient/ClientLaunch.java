package org.kramreiter.mtcg.cliclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.kramreiter.mtcg.card.CardFactory;
import org.kramreiter.mtcg.card.GameMode;
import org.kramreiter.mtcg.comm.*;

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
            sock_in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
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
                System.out.println("2");
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
                                queueup: enter queue for games
                                setdeck: configure your deck
                                openpack: open a pack of card (costs coins)
                                searchtrade: search for public trades
                                offertrade: offer your cards up for trading
                                accepttrade: accept a specific trade offer
                                exit: close program"""
                );
                case "getcollection" -> getCollection(sock_out, req, content);
                case "getuserinfo" -> getUserInfo(sock_out, req, content);
                case "openpack" -> openPack(sock_out, req, content);
                case "queueup" -> queueUp(sock_out, req, content, s);
                case "setdeck" -> setDeck(sock_out, req, content, s);
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
