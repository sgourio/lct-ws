package org.lct.game.ws.services.impl;

import org.joda.time.DateTime;
import org.lct.game.ws.beans.model.gaming.PlayGame;
import org.lct.game.ws.services.PlayGameService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Created by sgourio on 14/08/15.
 */
public class GameJob extends QuartzJobBean {
    private static final Logger logger = LoggerFactory.getLogger(GameJob.class);

    private PlayGame playGame;
    private PlayGameService playGameService;


    public void setPlayGame(PlayGame playGame) {
        this.playGame = playGame;
    }

    public void setPlayGameService(PlayGameService playGameService) {
        this.playGameService = playGameService;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info(playGame + " round " + playGameService.getRound(playGame, DateTime.now()));
        // test if finished
        if( playGameService.isEnded(playGame, DateTime.now())){
            playGameService.endGame(playGame);
        }

    }
}
