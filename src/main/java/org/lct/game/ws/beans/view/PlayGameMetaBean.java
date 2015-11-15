package org.lct.game.ws.beans.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Meta data of game beeing played
 * Created by sgourio on 19/08/15.
 */
public class PlayGameMetaBean {
    private final String playGameId;
    private final String name;
    private final String owner;
    private final String creator;
    private final String creatorId;
    private final String templateName;
    private final int numberOfRound;
    private final int timeByRound;
    private final int actualRoundNumber;
    private final int nbPlayers;
    private final String status;
    private final Date startDate;
    private final Date endDate;
    private final List<UserBean> authorizedUserList;

    public PlayGameMetaBean(String playGameId, String name, String owner, String creator, String creatorId, String templateName, int numberOfRound, int timeByRound, int actualRoundNumber, int nbPlayers, String status, Date startDate, Date endDate, List<UserBean> authorizedUserList) {
        this.playGameId = playGameId;
        this.name = name;
        this.owner = owner;
        this.creatorId = creatorId;
        this.creator = creator;
        this.templateName = templateName;
        this.numberOfRound = numberOfRound;
        this.timeByRound = timeByRound;
        this.actualRoundNumber = actualRoundNumber;
        this.nbPlayers = nbPlayers;
        this.status = status;
        this.startDate = startDate != null ? (Date) startDate.clone() : null;
        this.endDate = endDate != null ? (Date) endDate.clone() : null;
        this.authorizedUserList = authorizedUserList != null? new ArrayList<>(authorizedUserList) : new ArrayList<UserBean>();
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

    public int getNumberOfRound() {
        return numberOfRound;
    }

    public int getTimeByRound() {
        return timeByRound;
    }

    public String getCreator() {
        return creator;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public List<UserBean> getAuthorizedUserList() {
        return authorizedUserList;
    }
}
