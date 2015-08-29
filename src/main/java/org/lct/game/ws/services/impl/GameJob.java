package org.lct.game.ws.services.impl;

import org.joda.time.DateTime;
import org.lct.game.ws.beans.model.gaming.PlayGame;
import org.lct.game.ws.beans.view.Round;
import org.lct.game.ws.services.EventService;
import org.lct.game.ws.services.PlayGameService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Created by sgourio on 14/08/15.
 */
public class GameJob extends QuartzJobBean {
    private static final Logger logger = LoggerFactory.getLogger(GameJob.class);

    private String playGameId;
    private PlayGameService playGameService;
    private EventService eventService;


    public void setPlayGameId(String playGameId) {
        this.playGameId = playGameId;
    }

    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    public void setPlayGameService(PlayGameService playGameService) {
        this.playGameService = playGameService;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        DateTime atTime = new DateTime(context.getFireTime());
        PlayGame playGame = playGameService.getPlayGame(playGameId);
        DateTime playGameEndDate = new DateTime(playGame.getEndDate());
        boolean isFinished = atTime.isEqual(playGameEndDate) || atTime.isAfter(playGameEndDate);
        if ( isFinished) {
            playGameService.endGame(playGame);
            this.eventService.publishMetaData(playGameService.getPlayGameMetaBean(playGame));
            try {
                context.getScheduler().interrupt(context.getFireInstanceId());
            } catch (UnableToInterruptJobException e) {
                logger.error("",e);
            }
        } else {
            Round round = playGameService.getRound(playGame, atTime);
            logger.info(playGame + " round " + round);
            if (round != null) {
                if (round.getRoundNumber() == 1) {
                    playGame = playGameService.startPlayGame(playGame);
                    this.eventService.publishMetaData(playGameService.getPlayGameMetaBean(playGame));
                }
                this.eventService.publishTimer(playGame, playGameService.getTimer(playGame));
            }
            this.eventService.publishRound(playGame, round);
        }
    }
}
