/*
 * LCT
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.services.impl;

import org.joda.time.DateTime;
import org.lct.game.ws.beans.PlayGameStatus;
import org.lct.game.ws.beans.model.Game;
import org.lct.game.ws.beans.model.gaming.PlayGame;
import org.lct.game.ws.services.GameService;
import org.lct.game.ws.services.PlayGameService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;

/**
 * Created by sgourio on 04/06/16.
 */
public class RemoveGameNotStartedJob extends QuartzJobBean {
    private static final Logger logger = LoggerFactory.getLogger(RemoveGameNotStartedJob.class);

    private PlayGameService playGameService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        List<PlayGame> actualPlayGameMetaBeanList = playGameService.getActualPlayGame();
        DateTime limit = new DateTime().minusMinutes(30);
        for( PlayGame playGame : actualPlayGameMetaBeanList ){
            if( PlayGameStatus.opened.getId().equals(playGame.getStatus())
                    && !playGame.getOwner().equals("auto")
                    && playGame.getStartDate() == null
                    && limit.isAfter(new DateTime(playGame.getCreationDate())) ){
                logger.info("Close game " + playGame.getName() +" because it is open for more 30 min");
                playGameService.endGame(playGame);
            }
        }
    }

    public void setPlayGameService(PlayGameService playGameService) {
        this.playGameService = playGameService;
    }
}
