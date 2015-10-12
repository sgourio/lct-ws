/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.services.impl;

import org.lct.game.ws.beans.model.Game;
import org.lct.game.ws.beans.view.GameMetaBean;
import org.lct.game.ws.services.GameService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Created by sgourio on 04/10/15.
 */
public class AutoCreateGameJob  extends QuartzJobBean  {
    private static final Logger logger = LoggerFactory.getLogger(AutoCreateGameJob.class);

    private boolean running = false;
    private GameService gameService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        if( !running ) {
            running = true;
            try {
                Game game = gameService.generate();
                GameMetaBean gameMetaBean = gameService.gameToGameMeta(game);
                logger.info("Game created: " + gameMetaBean.getName() + " : " + gameMetaBean.getRounds() + " rounds " + gameMetaBean.getMaxScore() + " pts");
            }catch (Exception e){
                logger.error("Generation error", e);
            }finally {
                running = false;
            }
        }
    }


    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }
}
