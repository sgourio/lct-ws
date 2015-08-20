package org.lct.game.ws.beans.model.gaming;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.lct.game.ws.beans.model.Game;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

/**
 * A game played or beeing played
 * Created by sgourio on 14/08/15.
 */
public class PlayGame {

    @Id
    private String id;
    private final Game game;
    private final String name;
    private final Date startDate;
    private final PlayerGame owner;
    private final List<PlayerGame> playerGameList;
    private final List<PlayRound> playRoundList;
    private final String status; // opened, running, ended,
    private final int roundTime; // seconds


    public PlayGame(@JsonProperty("game") Game game, @JsonProperty("name") String name, @JsonProperty("startDate") Date startDate, @JsonProperty("owner") PlayerGame owner, @JsonProperty("playerGameList") List<PlayerGame> playerGameList, @JsonProperty("playRoundList") List<PlayRound> playRoundList, @JsonProperty("status") String status, @JsonProperty("roundTime") int roundTime) {
        this.game = game;
        this.name = name;
        this.startDate = startDate != null ? (Date) startDate.clone() : null;
        this.owner = owner;
        this.playerGameList = playerGameList;
        this.playRoundList = playRoundList;
        this.status = status;
        this.roundTime = roundTime;
    }

    public String getId() {
        return id;
    }

    public Game getGame() {
        return game;
    }

    public Date getStartDate() {
        return startDate != null ? (Date) startDate.clone() : null;
    }

    public PlayerGame getOwner() {
        return owner;
    }

    public List<PlayerGame> getPlayerGameList() {
        return playerGameList;
    }

    public String getStatus() {
        return status;
    }

    public int getRoundTime() {
        return roundTime;
    }

    public List<PlayRound> getPlayRoundList() {
        return playRoundList;
    }

    public String getName() {
        return name;
    }
}
