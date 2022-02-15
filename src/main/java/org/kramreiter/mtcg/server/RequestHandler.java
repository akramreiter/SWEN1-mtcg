package org.kramreiter.mtcg.server;

import org.kramreiter.mtcg.comm.*;
import org.kramreiter.mtcg.comm.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private Socket clientSocket;
    private ServerApp app;
    private PrintWriter output;
    private BufferedReader in;

    public RequestHandler(Socket clientSocket, ServerApp app) throws IOException {
        this.clientSocket = clientSocket;
        this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
        this.output = new PrintWriter(this.clientSocket.getOutputStream(), true);
        this.app = app;
        if (this.app instanceof MTCGApp) {
            ((MTCGApp) this.app).setWriter(this.output);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Response response;
                Request request = RequestBuilder.buildRequest(this.in);

                if (request.getPathname() == null) {
                    response = new Response(
                            HttpStatus.BAD_REQUEST,
                            ContentType.JSON,
                            "[]"
                    );
                } else {
                    response = this.app.handleRequest(request);
                }
                output.printf(response.get());
            }
        } catch (IOException e) {
            System.err.println(Thread.currentThread().getName() + " Error: " + e.getMessage());
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
                if (in != null) {
                    in.close();
                    clientSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
