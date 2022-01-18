package org.kramreiter.mtcg.comm;

public interface MTCGController {
    String responseToJson(ResponseContent responseContent);
    ResponseContent jsonToResponse(String jsonContent);
    String requestToJson(RequestContent requestContent);
    RequestContent jsonToRequest(String jsonContent);
}
