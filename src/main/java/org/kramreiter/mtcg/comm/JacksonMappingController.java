package org.kramreiter.mtcg.comm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonMappingController implements MTCGController {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public String responseToJson(ResponseContent responseContent) {
        try {
            return mapper.writeValueAsString(responseContent);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ResponseContent jsonToResponse(String jsonContent) {
        try {
            return mapper.readValue(jsonContent, ResponseContent.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String requestToJson(RequestContent requestContent) {
        try {
            return mapper.writeValueAsString(requestContent);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public RequestContent jsonToRequest(String jsonContent) {
        try {
            return mapper.readValue(jsonContent, RequestContent.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
