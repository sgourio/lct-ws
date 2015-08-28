package org.lct.game.ws.services.impl;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.lct.game.ws.beans.model.gaming.PlayGame;
import org.lct.game.ws.beans.view.Round;
import org.lct.game.ws.services.EventService;
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
        PlayGame playGame = playGameService.getPlayGame(playGameId);
        Round round = playGameService.getRound(playGame, DateTime.now());
        logger.info(playGame + " round " + round);
        if( round != null ) {

            if( round.getRoundNumber() == 1 ){
                playGame = playGameService.startPlayGame(playGame);
            }

            // test if finished
            if (playGameService.isEnded(playGame, DateTime.now())) {
                playGameService.endGame(playGame);
            }else {
                long countDown = new Duration(DateTime.now(), new DateTime(round.getEndDate()).plus(playGame.getRoundTime() * 1000)).getStandardSeconds();
                this.eventService.publishTimer(playGame, countDown);
            }
            this.eventService.publishRound(playGame, round);
        }
    }
}
