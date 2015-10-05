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
    private String gameName;
    private String gameId;
    private Date gameDate;
    private int points;
    private int percentFromTop; // *100
    private int position;
    private int score;
    private int topScore;

    public MonthlyScoreGame( @JsonProperty("gameName") String gameName, @JsonProperty("gameId") String gameId, @JsonProperty("gameDate")  Date gameDate, @JsonProperty("points") int points, @JsonProperty("percentFromTop") int percentFromTop, @JsonProperty("position") int position, @JsonProperty int score, @JsonProperty int topScore) {
        this.gameName = gameName;
        this.gameId = gameId;
        this.gameDate = gameDate;
        this.points = points;
        this.percentFromTop = percentFromTop;
        this.position = position;
        this.score = score;
        this.topScore = topScore;
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
}
