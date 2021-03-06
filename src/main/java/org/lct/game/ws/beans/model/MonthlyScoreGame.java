/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.beans.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by sgourio on 03/10/15.
 */
public class MonthlyScoreGame {
    private final String gameName;
    private final String gameId;
    private final Date gameDate;
    private final int points;
    private final int percentFromTop; // *100
    private final int position;
    private final int nbPlayers;
    private final int score;
    private final int topScore;
    private final boolean hasPlayedFirstRound;

    public MonthlyScoreGame( @JsonProperty("gameName") String gameName, @JsonProperty("gameId") String gameId, @JsonProperty("gameDate")  Date gameDate, @JsonProperty("points") int points, @JsonProperty("percentFromTop") int percentFromTop, @JsonProperty("position") int position,  @JsonProperty("nbPlayers") int nbPlayers, @JsonProperty("score") int score, @JsonProperty("topScore") int topScore, @JsonProperty("hasPlayedFirstRound") boolean hasPlayedFirstRound) {
        this.gameName = gameName;
        this.gameId = gameId;
        this.gameDate = gameDate;
        this.points = points;
        this.percentFromTop = percentFromTop;
        this.position = position;
        this.nbPlayers = nbPlayers;
        this.score = score;
        this.topScore = topScore;
        this.hasPlayedFirstRound = hasPlayedFirstRound;
    }

    public boolean isHasPlayedFirstRound() {
        return hasPlayedFirstRound;
    }

    public String getGameName() {
        return gameName;
    }

    public String getGameId() {
        return gameId;
    }

    public int getPoints() {
        return points;
    }

    public int getPercentFromTop() {
        return percentFromTop;
    }

    public int getPosition() {
        return position;
    }

    public Date getGameDate() {
        return gameDate;
    }

    public int getTopScore() {
        return topScore;
    }

    public int getScore() {
        return score;
    }

    public int getNbPlayers() {
        return nbPlayers;
    }
}
