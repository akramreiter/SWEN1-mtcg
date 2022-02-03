package org.kramreiter.mtcg.cliclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.kramreiter.mtcg.comm.ContentType;
import org.kramreiter.mtcg.comm.Method;
import org.kramreiter.mtcg.comm.Request;
import org.kramreiter.mtcg.comm.RequestContent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientLaunch {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.println("Enter server ip address + port");
        ObjectMapper mapper = new ObjectMapper();
        Socket sock = null;

        PrintWriter sock_out = null;

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
        Thread comm = new Thread(new CommThread(sock));
        comm.start();

        boolean login_status = false;
        String input;
        while (true) {
            Request req = new Request();
            if (!login_status) {
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
            } else {
                System.out.println("Enter your operation (\"help\" for full list)");
                input = s.nextLine();
                switch (input) {

                }
            }
        }
    }
}
