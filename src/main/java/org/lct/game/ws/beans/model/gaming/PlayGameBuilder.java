/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.beans.model.gaming;

import org.joda.time.DateTime;
import org.lct.game.ws.beans.model.Game;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlayGameBuilder {
    private String id;
    private Game game;
    private String name;
    private Date creationDate;
    private Date startDate;
    private PlayerGame owner;
    private List<PlayerGame> playerGameList;
    private List<PlayRound> playRoundList;
    private String status;
    private int roundTime;

    public PlayGameBuilder() {
    }

    public PlayGameBuilder(PlayGame playGame) {
        this.id = playGame.getId();
        this.game = playGame.getGame();
        this.name = playGame.getName();
        this.creationDate = playGame.getCreationDate() != null ? new Date(playGame.getCreationDate().getTime()) : null;
        this.startDate = playGame.getStartDate() != null ? new Date(playGame.getStartDate().getTime()) : null;
        this.owner = playGame.getOwner();
        this.playerGameList = new ArrayList<>(playGame.getPlayerGameList());
        this.playRoundList = new ArrayList<>(playGame.getPlayRoundList());
        this.status = playGame.getStatus();
        this.roundTime = playGame.getRoundTime();
    }

    public PlayGameBuilder setGame(Game game) {
        this.game = game;
        return this;
    }

    public PlayGameBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public PlayGameBuilder setStartDate(Date startDate) {
        this.startDate = startDate;
        return this;
    }

    public PlayGameBuilder setOwner(PlayerGame owner) {
        this.owner = owner;
        return this;
    }

    public PlayGameBuilder setPlayerGameList(List<PlayerGame> playerGameList) {
        this.playerGameList = playerGameList;
        return this;
    }

    public PlayGameBuilder setPlayRoundList(List<PlayRound> playRoundList) {
        this.playRoundList = playRoundList;
        return this;
    }

    public PlayGameBuilder setStatus(String status) {
        this.status = status;
        return this;
    }

    public PlayGameBuilder setRoundTime(int roundTime) {
        this.roundTime = roundTime;
        return this;
    }

    public PlayGameBuilder setCreationDate(Date creationDate){
        this.creationDate = creationDate;
        return this;
    }

    public PlayGame createPlayGame() {
        Date endDate = startDate != null ? new DateTime(startDate).plusSeconds(roundTime * playRoundList.size()).toDate() : null;
        return new PlayGame(id, game, name, creationDate, startDate, endDate, owner, playerGameList, playRoundList, status, roundTime);
    }


}