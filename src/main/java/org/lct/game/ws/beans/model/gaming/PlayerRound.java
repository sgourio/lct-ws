package org.lct.game.ws.beans.model.gaming;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

/**
 * A player for a round. His score is the score of the round.
 * Created by sgourio on 14/08/15.
 */
public class PlayerRound {

    private final String userId;
    private final String name;
    private final int score;
    private final String word; // word reference

    public PlayerRound(@JsonProperty("userId") String userId, @JsonProperty("name") String name, @JsonProperty("score") int score, @JsonProperty("word") String word) {
        this.userId = userId;
        this.name = name;
        this.score = score;
        this.word = word;
    }


    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public String getUserId() {
        return userId;
    }

    public String getWord() {
        return word;
    }
}
