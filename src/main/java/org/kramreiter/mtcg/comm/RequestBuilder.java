package org.kramreiter.mtcg.comm;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Locale;

public class RequestBuilder {
    static final String CONTENT_TYPE = "Content-Type: ";
    static final String CONTENT_LENGTH = "Content-Length: ";

    public static Request buildRequest(BufferedReader in) throws IOException {
        String line = null;
        Request request = new Request();

        do {
            line = in.readLine();
        } while (line == null);

        String[] splitFirstLine = line.split(" ");
        Boolean hasParams = splitFirstLine[1].contains("?");


        request.setMethod(getMethod(splitFirstLine));
        request.setPathname(getPathname(splitFirstLine, hasParams));
        request.setParams(getParams(splitFirstLine, hasParams));

        while (!line.isEmpty()) {
            line = in.readLine();
            if (line.startsWith(CONTENT_LENGTH)) {
                request.setContentLength(getContentLength(line));
            }
            if (line.startsWith(CONTENT_TYPE)) {
                request.setContentType(getContentType(line));
            }
        }

        if (request.getMethod() == Method.POST || request.getMethod() == Method.PUT) {
            int asciiChar;
            for (int i = 0; i < request.getContentLength(); i++) {
                asciiChar = in.read();
                String body = request.getBody();
                request.setBody(body + ((char) asciiChar));
                System.out.print((char) asciiChar);
            }
        }

        return request;
    }

    private static Method getMethod(String[] splitFirstLine) {
        return Method.valueOf(splitFirstLine[0].toUpperCase(Locale.ROOT));
    }

    private static String getPathname(String[] splitFirstLine, Boolean hasParams) {
        String out = splitFirstLine[1];
        if (hasParams) {
            out = out.split("\\?")[0];
        }
        if (splitFirstLine[1].contains("/")) {
            out = out.split("/")[1];
        }
        return out;
    }


    private static String getParams(String[] splittedFirstLine, Boolean hasParams) {
        if (hasParams) {
            return splittedFirstLine[1].split("\\?")[1];
        }

        return "";
    }

    private static Integer getContentLength(String line) {
        return Integer.parseInt(line.substring(CONTENT_LENGTH.length()));
    }

    private static String getContentType(String line) {
        return line.substring(CONTENT_TYPE.length());
    }
}
