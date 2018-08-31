/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.services.impl;

import org.joda.time.DateTime;
import org.lct.game.ws.beans.PlayGameStatus;
import org.lct.game.ws.beans.model.Game;
import org.lct.game.ws.beans.model.gaming.PlayGame;
import org.lct.game.ws.beans.view.GameMetaBean;
import org.lct.game.ws.beans.view.PlayGameMetaBean;
import org.lct.game.ws.services.GameService;
import org.lct.game.ws.services.PlayGameService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Iterator;
import java.util.List;

/**
 * Created by sgourio on 10/10/15.
 */
public class AutoGameLauncherJob extends QuartzJobBean {
    private static final Logger logger = LoggerFactory.getLogger(AutoGameLauncherJob.class);

    private GameService gameService;
    private PlayGameService playGameService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        DateTime fireTime = new DateTime(context.getFireTime());
        List<Game> gameList = gameService.getByAuthorIdAndCreationDateAfter("auto", fireTime.withTimeAtStartOfDay());
        if( gameList.size() < 20 ) {
            gameList = gameService.getByAuthorId("auto", 30);
        }
        //remove game played today
        List<Game> alreadyPlayedGameList = playGameService.getTodayGamesPlayed();
        Iterator<Game> gameIterator = gameList.iterator();
        while( gameIterator.hasNext() ){
            Game game = gameIterator.next();
            for( Game alreadyPlayedGame : alreadyPlayedGameList){
                if( game.getId().equals( alreadyPlayedGame.getId() )){
                    gameIterator.remove();
                    break;
                }
            }
        }
        if( gameList.size() < 10 ){
            return;
        }


        boolean next75s = false;
        boolean next90s = false;
        boolean next2min = false;
        boolean next3min = false;
        boolean next19h = false;
        boolean next20h = false;
        boolean next20h30 = false;
        boolean next21h = false;
        boolean next21h30 = false;
        boolean next22h = false;
        boolean nextOpenForAll = false;
        DateTime endOf75s = null;
        DateTime endOf90s = null;
        DateTime endOf2min = null;
        DateTime endOf3min = null;
        DateTime endOfOpenForAll = null;

        List<PlayGame> actualPlayGameMetaBeanList = playGameService.getActualPlayGame();
        for( PlayGame playGameMetaBean : actualPlayGameMetaBeanList ){
            if( playGameMetaBean.getOwner().equals("auto") &&
                    (playGameMetaBean.getName().equals("Blitz")
                            || playGameMetaBean.getName().equals("Rapide")
                            || playGameMetaBean.getName().equals("La 2 minutes")
                            || playGameMetaBean.getName().equals("Comme en tournois") ) ) {
                if (PlayGameStatus.opened.getId().equals(playGameMetaBean.getStatus())) {
                    switch (playGameMetaBean.getRoundTime()) {
                        case 75:
                            next75s = true;
                            break;
                        case 90:
                            next90s = true;
                            break;
                        case 120:
                            next2min = true;
                            break;
                        case 180:
                            next3min = true;
                            break;
                    }
                } else if (PlayGameStatus.running.getId().equals(playGameMetaBean.getStatus())) {
                    DateTime endDate = new DateTime(playGameMetaBean.getEndDate());
                    switch (playGameMetaBean.getRoundTime()) {
                        case 75:
                            endOf75s = endDate;
                            break;
                        case 90:
                            endOf90s = endDate;
                            break;
                        case 120:
                            endOf2min = endDate;
                            break;
                        case 180:
                            endOf3min = endDate;
                            break;
                    }
                }
            }else if( playGameMetaBean.getOwner().equals("auto") && playGameMetaBean.getName().equals("Rdv 19h") ){
                next19h = true;
            }else if( playGameMetaBean.getOwner().equals("auto") && playGameMetaBean.getName().equals("Rdv 20h") ){
                next20h = true;
            }else if( playGameMetaBean.getOwner().equals("auto") && playGameMetaBean.getName().equals("Rdv 20h30") ){
                next20h30 = true;
            }else if( playGameMetaBean.getOwner().equals("auto") && playGameMetaBean.getName().equals("Rdv 21h") ){
                next21h = true;
            }else if( playGameMetaBean.getOwner().equals("auto") && playGameMetaBean.getName().equals("Rdv 21h30") ){
                next21h30 = true;
            }else if( playGameMetaBean.getOwner().equals("auto") && playGameMetaBean.getName().equals("Rdv 22h") ){
                next22h = true;
            }else if( playGameMetaBean.getOwner().equals("auto") && playGameMetaBean.getName().equals("Ouverte à tous") ) {
                if (PlayGameStatus.opened.getId().equals(playGameMetaBean.getStatus())) {
                    nextOpenForAll = true;
                } else if (PlayGameStatus.running.getId().equals(playGameMetaBean.getStatus())) {
                    DateTime endDate = new DateTime(playGameMetaBean.getEndDate());
                    endOfOpenForAll = endDate;
                }
            }
        }

        if( !next75s ){
            setUpNextGame(endOf75s, 75, "Blitz", gameList.remove(0));
        }
        if( !next90s ){
            setUpNextGame(endOf90s, 90, "Rapide", gameList.remove(0));
        }
        if( !next2min ){
            setUpNextGame(endOf2min, 120, "La 2 minutes", gameList.remove(0));
        }
        if( !next3min ){
            setUpNextGame(endOf3min, 180, "Comme en tournois", gameList.remove(0));
        }
        if( !nextOpenForAll ){
            setUpNextGame(endOfOpenForAll, 120, "Ouverte à tous", gameList.remove(0));
        }
        if( !next19h ){
            setUpNextGame(DateTime.now().withTimeAtStartOfDay().withHourOfDay(19).minusMinutes(3), 120, "Rdv 19h", gameList.remove(0));
        }
        if( !next20h ){
            setUpNextGame(DateTime.now().withTimeAtStartOfDay().withHourOfDay(20).minusMinutes(3), 120, "Rdv 20h", gameList.remove(0));
        }
        if( !next20h30 ){
            setUpNextGame(DateTime.now().withTimeAtStartOfDay().withHourOfDay(20).withMinuteOfHour(27), 120, "Rdv 20h30", gameList.remove(0));
        }
        if( !next21h ){
            setUpNextGame(DateTime.now().withTimeAtStartOfDay().withHourOfDay(21).minusMinutes(3), 120, "Rdv 21h", gameList.remove(0));
        }
        if( !next21h30 ){
            setUpNextGame(DateTime.now().withTimeAtStartOfDay().withHourOfDay(21).withMinuteOfHour(27), 120, "Rdv 21h30", gameList.remove(0));
        }
        if( !next22h ){
            setUpNextGame(DateTime.now().withTimeAtStartOfDay().withHourOfDay(22).minusMinutes(3), 120, "Rdv 22h", gameList.remove(0));
        }
    }

    private void setUpNextGame(DateTime endOfPreviousGame, int second, String name, Game game){
        DateTime nextStart = endOfPreviousGame;
        if( nextStart == null ){
            nextStart = DateTime.now();
        }
        nextStart = nextStart.withSecondOfMinute(0).plusMinutes(3);
        PlayGame playGame = playGameService.openAutoGame(game, name, second, DateTime.now());
        playGameService.setUpGame(playGame, nextStart);
    }


    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }

    public void setPlayGameService(PlayGameService playGameService) {
        this.playGameService = playGameService;
    }
}
