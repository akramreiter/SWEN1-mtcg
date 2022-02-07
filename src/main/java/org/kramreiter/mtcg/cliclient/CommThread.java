package org.kramreiter.mtcg.cliclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.kramreiter.mtcg.comm.ResponseBuilder;
import org.kramreiter.mtcg.comm.Response;
import org.kramreiter.mtcg.comm.ResponseContent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Objects;

public class CommThread implements Runnable {
    Socket socket;
    BufferedReader sock_in;
    CommVars vars;
    public CommThread(Socket sock, CommVars v) {
        socket = sock;
        vars = v;
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
                try {
                    Response res = ResponseBuilder.buildResponse(sock_in);
                    System.out.println(res.getContent());
                    ResponseContent content = mapper.readValue(res.getContent(), ResponseContent.class);
                    for (String s : content.getResponse()) {
                        System.out.println(s);
                    }
                    if (!Objects.equals(content.getToken(), "")) {
                        vars.setLoginStatus(true);
                        vars.setToken(content.getToken());
                    }
                    vars.setResponseReceived(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
