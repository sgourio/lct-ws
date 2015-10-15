package org.lct.game.ws.beans.model.gaming;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.lct.game.ws.beans.view.Word;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * A player for a round. His score is the score of the round.
 * Created by sgourio on 14/08/15.
 */
public class PlayerRound {

    @Id
    private String id;
    private Date playDate;
    private final String userId;
    private final String name;
    private final int roundNumber;
    private final String playGameId;
    private final int score; // turn score
    private final Word word; // word reference
    private final int bonus;

    public PlayerRound(@JsonProperty("id") String id, @JsonProperty("playDate") Date playDate, @JsonProperty("userId") String userId, @JsonProperty("playGameId") String playGameId, @JsonProperty("roundNumber") int roundNumber, @JsonProperty("name") String name, @JsonProperty("score") int score, @JsonProperty("word") Word word, @JsonProperty("score") int bonus) {
        this.id = id;
        this.playDate = playDate;
        this.userId = userId;
        this.playGameId = playGameId;
        this.roundNumber = roundNumber;
        this.name = name;
        this.score = score;
        this.word = word;
        this.bonus = bonus;
    }

    public String getId() {
        return id;
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

    public Word getWord() {
        return word;
    }

    public String getPlayGameId() {
        return playGameId;
    }

    public Date getPlayDate() {
        return playDate;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public int getBonus() {
        return bonus;
    }
}
