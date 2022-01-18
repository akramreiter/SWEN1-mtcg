package org.kramreiter.mtcg.cliclient;

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

    }
}
