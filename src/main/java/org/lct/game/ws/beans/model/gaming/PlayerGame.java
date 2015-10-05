package org.lct.game.ws.beans.model.gaming;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * A player of a game. His score is the total score for the game
 * Created by sgourio on 14/08/15.
 */
public class PlayerGame implements Comparable<PlayerGame> {

    @Id
    private String id;
    private final String userId;
    private final String playGameId;
    private final String name;
    private final int score; // global

    public PlayerGame(@JsonProperty("id") String id, @JsonProperty("userId") String userId, @JsonProperty("playGameId") String playGameId, @JsonProperty("name") String name, @JsonProperty("score") int score) {
        this.id = id;
        this.playGameId = playGameId;
        this.name = name;
        this.score = score;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public String getPlayGameId() {
        return playGameId;
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

    @Override
    public int compareTo(PlayerGame o) {
        return Integer.valueOf(o.getScore()).compareTo(this.score);
    }
}
