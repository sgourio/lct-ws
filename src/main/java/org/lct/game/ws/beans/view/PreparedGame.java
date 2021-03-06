package org.lct.game.ws.beans.view;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * This bean is used to PUT a new playGame
 * Created by sgourio on 19/08/15.
 */
public class PreparedGame {
    private final String gameId;
    private final String gameName;
    private final int roundTime;
    private final String clubId;
    private final List<String> authorizedUserIds;

    public PreparedGame(@JsonProperty("gameId") String gameId, @JsonProperty("gameName") String gameName, @JsonProperty("roundTime") int roundTime, @JsonProperty("clubId") String clubId, @JsonProperty("authorizedUserIds") List<String> authorizedUserIds) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.roundTime = roundTime;
        this.clubId = clubId;
        this.authorizedUserIds = authorizedUserIds != null ? new ArrayList<>(authorizedUserIds) : new ArrayList<String>();
    }

    public String getGameId() {
        return gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public int getRoundTime() {
        return roundTime;
    }

    public String getClubId() {
        return clubId;
    }

    public List<String> getAuthorizedUserIds() {
        return authorizedUserIds;
    }
}
