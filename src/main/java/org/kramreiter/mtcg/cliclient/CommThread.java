package org.kramreiter.mtcg.cliclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.kramreiter.mtcg.comm.ResponseBuilder;
import org.kramreiter.mtcg.comm.Response;
import org.kramreiter.mtcg.comm.ResponseContent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class CommThread implements Runnable {
    Socket socket;
    BufferedReader sock_in;
    public CommThread(Socket sock) {
        socket = sock;
        try {
            sock_in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            sock_in = null;
        }
    }
    @Override
    public void run() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            while (true) {
                String in = sock_in.readLine();
                try {
                    Response res = ResponseBuilder.buildResponse(sock_in);
                    ResponseContent content = mapper.readValue(res.getContent(), ResponseContent.class);
                    for (String s : content.getResponse()) {
                        System.out.println(s);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
