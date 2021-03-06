package org.kramreiter.mtcg.comm;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseContent {
    @JsonAlias("response")
    private String[] response = new String[0];
    @JsonAlias("token")
    private String token = "";
}
