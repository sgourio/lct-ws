package org.lct.game.ws.beans.model.gaming;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * A round of a game
 * Created by sgourio on 14/08/15.
 */
public class PlayRound {
    private final List<PlayerRound> playerRoundList;

    public PlayRound(@JsonProperty("playerRoundList") List<PlayerRound> playerRoundList) {
        this.playerRoundList = playerRoundList;
    }

    public List<PlayerRound> getPlayerRoundList() {
        return playerRoundList;
    }
}
