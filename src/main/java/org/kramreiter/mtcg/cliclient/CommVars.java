package org.kramreiter.mtcg.cliclient;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommVars {
    boolean loginStatus = false;
    boolean responseReceived = false;
    String token = "";
}
