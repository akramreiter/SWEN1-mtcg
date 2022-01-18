package org.kramreiter.mtcg.comm;

import java.io.BufferedReader;
import java.io.IOException;

public class ResponseBuilder {
    static final String CONTENT_TYPE = "Content-Type: ";
    static final String CONTENT_LENGTH = "Content-Length: ";

    public static Response buildRequest(BufferedReader in) throws IOException {
        String line = in.readLine();
        Response response = new Response();

        if (line != null) {
            String[] splitFirstLine = line.split(" ");
            response.setStatus(getStatus(splitFirstLine));
            response.setMessage(getMessage(splitFirstLine));

            while (!line.isEmpty()) {
                line = in.readLine();
                if (line.startsWith(CONTENT_LENGTH)) {
                    response.setContentLength(getContentLength(line));
                }
                if (line.startsWith(CONTENT_TYPE)) {
                    response.setContentType(getContentType(line));
                }
            }

            int asciiChar;
            for (int i = 0; i < response.getContentLength(); i++) {
                asciiChar = in.read();
                String body = response.getContent();
                response.setContent(body + ((char) asciiChar));
            }
        }
        return response;
    }

    private static int getStatus(String[] splitFirstLine) {
        try {
            return Integer.parseInt(splitFirstLine[0]);
        } catch (Exception e) {
            return 500;
        }
    }

    private static String getMessage(String[] splitFirstLine) {
        try {
            return splitFirstLine[1];
        } catch (Exception e) {
            return "";
        }
    }

    private static Integer getContentLength(String line) {
        return Integer.parseInt(line.substring(CONTENT_LENGTH.length()));
    }

    private static String getContentType(String line) {
        return line.substring(CONTENT_TYPE.length());
    }
}
