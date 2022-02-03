package org.kramreiter.mtcg.comm;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Response implements Serializable {
    private int status;
    private String message = "";
    private String contentType = "";
    private int contentLength;
    private String content = "";

    public Response() {}

    public Response(HttpStatus httpStatus, ContentType contentType, String content) {
        this.status = httpStatus.code;
        this.message = httpStatus.message;
        this.contentType = contentType.type;
        this.contentLength = content.length();
        this.content = content;
    }

    public String get() {
        return "HTTP/1.1 " + this.status + " " + this.message + "\r\n" +
                "Content-Type: " + this.contentType + "\r\n" +
                "Content-Length: " + this.contentLength + "\r\n" +
                "\r\n" +
                this.content;
    }
}
