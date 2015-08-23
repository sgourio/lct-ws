package org.lct.game.ws.beans.model.gaming;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

/**
 * A player of a game. His score is the taotal score for the game
 * Created by sgourio on 14/08/15.
 */
public class PlayerGame {

    private final String userId;
    private final String name;
    private final int score;

    public PlayerGame(@JsonProperty("userId") String userId, @JsonProperty("name") String name, @JsonProperty("score") int score) {
        this.name = name;
        this.score = score;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public int getScore() {
        return score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerGame that = (PlayerGame) o;

        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return userId != null ? userId.hashCode() : 0;
    }
}
