package org.kramreiter.mtcg.comm;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Request implements Serializable {
    private Method method;
    private String pathname;
    private String params = "";
    private String contentType;
    private Integer contentLength;
    private String body = "";

    public String get() {
        String out = method.name() + " " + pathname;
        if (params.length() > 0) {
            out += "?" + params;
        }
        out += "\r\nContent-Type: " + this.contentType +
                "\r\nContent-Length: " + this.contentLength +
                "\r\n\r\n" + this.body;
        return out;
    }
}
