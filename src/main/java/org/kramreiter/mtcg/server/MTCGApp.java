package org.kramreiter.mtcg.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.kramreiter.mtcg.comm.*;

import java.util.ArrayList;

public class MTCGApp implements ServerApp {
    @Override
    public Response handleRequest(Request request) {
        ObjectMapper mapper = new ObjectMapper();
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
                case "login" -> output.setResponse(login(content));
                case "register" -> output.setResponse(register(content));
                case "setdeck" -> output.setResponse(setDeck(content));
                case "queueup" -> output.setResponse(queueUp(content));
                case "openpack" -> output.setResponse(openPack(content));
                case "searchtrade" -> output.setResponse(searchTrade(content));
                case "offertrade" -> output.setResponse(offerTrade(content));
                case "accepttrade" -> output.setResponse(acceptTrade(content));
                case "getcollection" -> output.setResponse(getCollection(content));
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

    public String[] login(RequestContent request) {
        return new String[] {"attempting login"};
    }

    public String[] register(RequestContent request) {
        return new String[] {"register"};
    }

    public String[] setDeck(RequestContent request) {
        return new String[0];
    }

    public String[] queueUp(RequestContent request) {
        return new String[0];
    }

    public String[] openPack(RequestContent request) {
        return new String[0];
    }

    public String[] searchTrade(RequestContent request) {
        return new String[0];
    }

    public String[] offerTrade(RequestContent request) {
        return new String[0];
    }

    public String[] acceptTrade(RequestContent request) {
        return new String[0];
    }

    public String[] getCollection(RequestContent request) {
        return new String[0];
    }
}
