package org.kramreiter.mtcg.server;

import org.kramreiter.mtcg.comm.Request;
import org.kramreiter.mtcg.comm.RequestContent;
import org.kramreiter.mtcg.comm.Response;

public interface ServerApp {
    Response handleRequest(Request content);
}
