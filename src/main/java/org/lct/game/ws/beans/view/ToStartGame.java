/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.beans.view;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by sgourio on 24/08/15.
 */
public class ToStartGame {
    private final String playGameId;
    private final Date startDate;

    public ToStartGame(@JsonProperty("playGameId") String playGameId, @JsonProperty("startDate") Date startDate) {
        this.playGameId = playGameId;
        this.startDate = startDate;
    }

    public String getPlayGameId() {
        return playGameId;
    }

    public Date getStartDate() {
        return startDate;
    }
}
