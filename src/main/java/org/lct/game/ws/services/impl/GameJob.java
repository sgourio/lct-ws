package org.lct.game.ws.services.impl;

import org.joda.time.DateTime;
import org.lct.game.ws.beans.model.gaming.PlayGame;
import org.lct.game.ws.beans.view.GameScore;
import org.lct.game.ws.beans.view.Round;
import org.lct.game.ws.controllers.services.EventService;
import org.lct.game.ws.services.PlayGameService;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Created by sgourio on 14/08/15.
 */
public class GameJob extends QuartzJobBean implements InterruptableJob {
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
        DateTime playGameEndDate = new DateTime(playGame.getEndDate())  ;

        playGameService.updateScores(playGame, atTime);
        GameScore gameScore = playGameService.getScores(playGame, atTime);
        this.eventService.publishScores(playGame, gameScore);
        boolean isFinished = atTime.plusSeconds(2).isAfter(playGameEndDate);
        if ( isFinished) {
            PlayGame endedGame = playGameService.endGame(playGame);
            this.eventService.publishMetaData(endedGame);
            try {
                context.getScheduler().interrupt(context.getFireInstanceId());
            } catch (UnableToInterruptJobException e) {
                logger.error("",e);
            }
        } else {
            Round round = playGameService.getRound(playGame, atTime);
            if (round != null) {
                if (round.getRoundNumber() == 1) {
                    playGame = playGameService.startPlayGame(playGame);
                    this.eventService.publishMetaData(playGame);
                }
                logger.info("update timer " + playGameService.getTimer(playGame));
                this.eventService.publishTimer(playGame, playGameService.getTimer(playGame));
            }
            logger.info("Game " + playGame + ", round " + round);

            this.eventService.publishRound(playGame, round);
        }
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
    }
}
