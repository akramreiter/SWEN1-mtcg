package org.kramreiter.mtcg.comm;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;
import org.kramreiter.mtcg.card.GameMode;

@Getter
@Setter
public class RequestContent {
    @JsonAlias("cards")
    private String[] cards = new String[0];
    @JsonAlias("filter")
    private String filter = "";
    @JsonAlias("game_mode")
    private GameMode gameMode;
    @JsonAlias("token")
    private String token = "";
    @JsonAlias("username")
    private String username = "";
    @JsonAlias("password")
    private String password = "";
}
