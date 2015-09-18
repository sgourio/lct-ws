package org.lct.game.ws.beans.model.gaming;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;
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
    private final Date creationDate;
    private final Date startDate;
    private final Date endDate;
    private final String owner;
    private final List<PlayRound> playRoundList;
    private final String status; // opened, running, ended,
    private final int roundTime; // seconds


    public PlayGame(@JsonProperty("id") String id, @JsonProperty("game") Game game, @JsonProperty("name") String name, @JsonProperty("creationDate") Date creationDate, @JsonProperty("startDate") Date startDate, @JsonProperty("endDate") Date endDate, @JsonProperty("owner") String owner, @JsonProperty("playRoundList") List<PlayRound> playRoundList, @JsonProperty("status") String status, @JsonProperty("roundTime") int roundTime) {
        this.id = id;
        this.game = game;
        this.name = name;
        this.creationDate = creationDate != null ? (Date) creationDate.clone() : null;
        this.startDate = startDate != null ? (Date) startDate.clone() : null;
        this.endDate = endDate != null ? (Date) endDate.clone() : null;
        this.owner = owner;
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

    public Date getCreationDate() {
        return creationDate != null ? (Date) creationDate.clone() : null;
    }

    public String getOwner() {
        return owner;
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

    @Override
    public String toString() {
        return this.name;
    }

    public Date getEndDate() {
        return endDate != null ? (Date) endDate.clone() : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayGame playGame = (PlayGame) o;

        if (id != null ? !id.equals(playGame.id) : playGame.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
