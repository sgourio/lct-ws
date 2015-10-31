/*
 * LCT
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.beans.view;

import java.util.Date;

/**
 * Created by sgourio on 31/10/15.
 */
public class MultiplexGameMetaBean {

    private final String multiplexGameId;
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

    public MultiplexGameMetaBean(String multiplexGameId, String name, String owner, String creator, String creatorId, String templateName, int numberOfRound, int timeByRound, int actualRoundNumber, int nbPlayers, String status, Date startDate, Date endDate) {
        this.multiplexGameId = multiplexGameId;
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
    }

    public String getMultiplexGameId() {
        return multiplexGameId;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
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

    public int getNumberOfRound() {
        return numberOfRound;
    }

    public int getTimeByRound() {
        return timeByRound;
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
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
}
