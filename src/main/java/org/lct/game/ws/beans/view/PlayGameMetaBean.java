package org.lct.game.ws.beans.view;

import java.util.Date;

/**
 * Meta data of game beeing played
 * Created by sgourio on 19/08/15.
 */
public class PlayGameMetaBean {
    private final String playGameId;
    private final String name;
    private final String owner;
    private final int actualRoundNumber;
    private final int nbPlayers;
    private final String status;
    private final Date startDate;
    private final Date endDate;

    public PlayGameMetaBean(String playGameId, String name, String owner, int actualRoundNumber, int nbPlayers, String status, Date startDate, Date endDate) {
        this.playGameId = playGameId;
        this.name = name;
        this.owner = owner;
        this.actualRoundNumber = actualRoundNumber;
        this.nbPlayers = nbPlayers;
        this.status = status;
        this.startDate = startDate != null ? (Date) startDate.clone() : null;
        this.endDate = endDate != null ? (Date) endDate.clone() : null;
    }

    public String getPlayGameId() {
        return playGameId;
    }

    public String getName() {
        return name;
    }

    public int getActualRoundNumber() {
        return actualRoundNumber;
    }

    public int getNbPlayers() {
        return nbPlayers;
    }

    public String getStatus() {
        return status;
    }

    public Date getStartDate() {
        return startDate != null ? (Date) startDate.clone() : null;
    }

    public Date getEndDate() {
        return endDate != null ? (Date) endDate.clone() : null;
    }

    public String getOwner() {
        return owner;
    }
}
