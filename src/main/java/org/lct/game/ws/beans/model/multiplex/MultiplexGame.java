/*
 * LCT
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.beans.model.multiplex;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.lct.game.ws.beans.model.Game;
import org.lct.game.ws.beans.model.gaming.PlayRound;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sgourio on 31/10/15.
 */
public class MultiplexGame {

    @Id
    private String id;
    private final Game game;
    private final String name;
    private final Date creationDate;
    private final Date startDate;
    private final Date endDate;
    private final String owner;
    private final List<String> administratorsIds;
    private final List<PlayRound> playRoundList;
    private final String status; // created, running, ended,
    private final int roundTime; // seconds

    public MultiplexGame(@JsonProperty("id") String id, @JsonProperty("game") Game game, @JsonProperty("name") String name, @JsonProperty("creationDate") Date creationDate, @JsonProperty("startDate") Date startDate, @JsonProperty("endDate") Date endDate, @JsonProperty("owner") String owner, @JsonProperty("administratorsIds") List<String> administratorsIds,  @JsonProperty("playRoundList") List<PlayRound> playRoundList, @JsonProperty("status") String status, @JsonProperty("roundTime") int roundTime) {
        this.id = id;
        this.game = game;
        this.name = name;
        this.creationDate = creationDate != null ? (Date) creationDate.clone() : null;
        this.startDate = startDate != null ? (Date) startDate.clone() : null;
        this.endDate = endDate != null ? (Date) endDate.clone() : null;
        this.owner = owner;
        this.administratorsIds = new ArrayList<>(administratorsIds);
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

    public String getName() {
        return name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getOwner() {
        return owner;
    }

    public List<PlayRound> getPlayRoundList() {
        return playRoundList;
    }

    public String getStatus() {
        return status;
    }

    public int getRoundTime() {
        return roundTime;
    }

    public List<String> getAdministratorsIds() {
        return administratorsIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MultiplexGame that = (MultiplexGame) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
