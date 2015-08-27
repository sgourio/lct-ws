package org.lct.game.ws.services.impl;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.lct.game.ws.beans.PlayGameStatus;
import org.lct.game.ws.beans.model.ConnectedUserBean;
import org.lct.game.ws.beans.model.Game;
import org.lct.game.ws.beans.model.Round;
import org.lct.game.ws.beans.model.User;
import org.lct.game.ws.beans.model.gaming.*;
import org.lct.game.ws.beans.view.PlayGameMetaBean;
import org.lct.game.ws.dao.ConnectedUserRepository;
import org.lct.game.ws.dao.PlayGameRepository;
import org.lct.game.ws.services.PlayGameService;
import org.lct.gameboard.ws.beans.model.BoardGameTemplate;
import org.lct.gameboard.ws.beans.model.Tile;
import org.lct.gameboard.ws.beans.view.BoardGame;
import org.lct.gameboard.ws.beans.view.DroppedTile;
import org.lct.gameboard.ws.beans.view.DroppedWord;
import org.lct.gameboard.ws.services.BoardService;
import org.lct.gameboard.ws.services.impl.BoardGameTemplateEnum;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sgourio on 14/08/15.
 */
public class PlayGameServiceImpl implements PlayGameService {
    private static Logger logger = LoggerFactory.getLogger(PlayGameServiceImpl.class);

    private final PlayGameRepository playGameRepository;
    private final BoardService boardService;
    private final ConnectedUserRepository connectedUserRepository;
    private final SchedulerFactoryBean schedulerFactoryBean;

    public PlayGameServiceImpl(PlayGameRepository playGameRepository, BoardService boardService, ConnectedUserRepository connectedUserRepository, SchedulerFactoryBean schedulerFactoryBean) {
        this.playGameRepository = playGameRepository;
        this.boardService = boardService;
        this.connectedUserRepository = connectedUserRepository;
        this.schedulerFactoryBean = schedulerFactoryBean;
    }

    @Override
    public PlayGame openGame(Game game, String name, int roundTime, Date startDate, User user) {
        PlayerGame owner = new PlayerGame(user.getId(), user.getName(), 0);
        List<PlayRound> playRoundList = new ArrayList<>(game.getRoundList().size());
        for( Round round : game.getRoundList()){
            playRoundList.add(new PlayRound(new ArrayList<PlayerRound>()));
        }
        PlayGame playGame = new PlayGameBuilder()
                .setGame(game)
                .setName(name)
                .setStartDate(startDate)
                .setOwner(owner)
                .setPlayerGameList(new ArrayList<PlayerGame>())
                .setPlayRoundList(playRoundList)
                .setStatus(PlayGameStatus.opened.getId())
                .setRoundTime(roundTime).createPlayGame();
        playGame = playGameRepository.save(playGame);
        logger.info("Game '"+name+"'opened by " + user);
        return playGame;
    }

    public PlayGame startGame(final PlayGame playGame, final Date startDate){
        DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
        logger.info("Start " + playGame +" at " + fmt.print(startDate.getTime()));
        PlayGameBuilder playGameBuilder = new PlayGameBuilder(playGame);
        playGameBuilder.setStatus(PlayGameStatus.running.getId());
        playGameBuilder.setStartDate(startDate);
        PlayGame startedGame = playGameBuilder.createPlayGame();

        startedGame = playGameRepository.save(startedGame);
        scheduleGame(startedGame);
        return startedGame;
    }

    public PlayGame endGame(final PlayGame playGame){
        logger.info("Terminating game " + playGame);
        PlayGameBuilder playGameBuilder = new PlayGameBuilder(playGame);
        playGameBuilder.setStatus(PlayGameStatus.ended.getId());
        PlayGame endedGame = playGameBuilder.createPlayGame();
        return playGameRepository.save(endedGame);
    }

    public void scheduleGame(final PlayGame playGame){
        if( playGame.getStartDate() != null){
            int currentRoundNumber = getRoundNumber(playGame, DateTime.now());
            JobDetail jobDetail = buildJobDetail(playGame);
            ScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.repeatSecondlyForTotalCount(playGame.getPlayRoundList().size() - currentRoundNumber + 1, playGame.getRoundTime());
            TriggerBuilder triggerBuilder = TriggerBuilder.newTrigger();
            if( playGame.getStartDate().after(new Date())){
                triggerBuilder.startAt(playGame.getStartDate());
            }else{
                triggerBuilder.startNow();
            }
            Trigger trigger = triggerBuilder.withSchedule(scheduleBuilder).build();
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            try {
                if (!scheduler.isStarted()) {
                    scheduler.start();
                }
                scheduler.scheduleJob(jobDetail, trigger);
            } catch (SchedulerException e) {
                logger.error("", e);
            }
        }
    }

    private JobDetail buildJobDetail(PlayGame playGame){
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("playGame", playGame);
        jobDataMap.put("playGameService", this);
        JobDetail jobDetail = JobBuilder.newJob(GameJob.class).setJobData(jobDataMap).build();
        return jobDetail;
    }

    @Override
    public void scheduleAllRunningGames() {
        List<PlayGame> playGameList = playGameRepository.findByStatus(PlayGameStatus.running.getId());
        for( PlayGame playGame : playGameList ){
            scheduleGame(playGame);
        }
    }

    public boolean isEnded(PlayGame playGame , DateTime atTime){
        return atTime.isAfter(new DateTime(playGame.getStartDate()).plusSeconds(playGame.getRoundTime() * playGame.getPlayRoundList().size()));
    }

    public List<PlayGameMetaBean> getActualPlayGame(){
        List<PlayGame> playGameList = playGameRepository.findByStatus(PlayGameStatus.opened.getId());
        playGameList.addAll(playGameRepository.findByStatus(PlayGameStatus.running.getId()));
        return playGameToPlayGameMetaBean(playGameList);
    }

    private List<PlayGameMetaBean> playGameToPlayGameMetaBean(List<PlayGame> playGameList){
        List<PlayGameMetaBean> result = new ArrayList<>(playGameList.size());
        for( PlayGame playGame : playGameList){
            result.add(playGameToPlayGameMetaBean(playGame));
        }
        return result;
    }

    private PlayGameMetaBean playGameToPlayGameMetaBean(PlayGame playGame){
        Date endDate = null;
        int actualRoundNumber = 0;
        if( playGame.getStartDate() != null ) {
            DateTime startDate = new DateTime(playGame.getStartDate());
            endDate = startDate.plusSeconds(playGame.getRoundTime() * playGame.getPlayRoundList().size()).toDate();
            DateTime now = new DateTime();
            if( now.isAfter(startDate)) {
                actualRoundNumber = getRoundNumber(playGame, now);
            }
        }
        return new PlayGameMetaBean(playGame.getId(),
                playGame.getName(),
                playGame.getOwner().getName(),
                playGame.getPlayRoundList().size(),
                playGame.getRoundTime(),
                actualRoundNumber,
                playGame.getPlayerGameList().size(),
                playGame.getStatus(),
                playGame.getStartDate(),
                endDate);
    }

    @Override
    public org.lct.game.ws.beans.view.Round getRound(PlayGame playGame, DateTime atTime){
        return getRound(playGame, getRoundNumber(playGame, atTime));
    }

    private int getRoundNumber(PlayGame playGame, DateTime atTime){
        DateTime startedDateTime = new DateTime(playGame.getStartDate());
        if( startedDateTime.isAfter(atTime)){
            return 0;
        }
        Duration duration = new Duration(startedDateTime, atTime);
        int round = ((int) duration.getStandardSeconds() / playGame.getRoundTime()) + 1;
        return Math.min(round, playGame.getPlayRoundList().size());
    }

    @Override
    @Cacheable("round")
    public org.lct.game.ws.beans.view.Round getRound(PlayGame playGame, int roundNumber){

        Round round = playGame.getGame().getRoundList().get(roundNumber - 1);
        BoardGameTemplate boardGameTemplate = new BoardGameTemplate(BoardGameTemplateEnum.classic.getSquares());
        BoardGame boardGame = new BoardGame(boardGameTemplate);
        for( int i = 0; i < roundNumber - 1 ; i++) {
            boardGame = boardGame.dropWord(playGame.getGame().getRoundList().get(i).getDroppedWord());
        }
        DateTime gameStartDate = new DateTime(playGame.getStartDate());
        Duration duration = new Duration((roundNumber-1) * playGame.getRoundTime());
        DateTime roundStartDate = gameStartDate.plus(duration);
        DateTime roundEndDate = roundStartDate.plusSeconds(playGame.getRoundTime());
        DroppedWord lastDroppedWord = null;
        if( roundNumber > 1 ){
            lastDroppedWord = playGame.getGame().getRoundList().get(roundNumber-2).getDroppedWord();
        }
        List<DroppedTile> draw = new ArrayList<DroppedTile>();
        for( Tile tile : round.getDraw()){
            draw.add(new DroppedTile(tile, tile.getValue()));
        }
        return new org.lct.game.ws.beans.view.Round(roundNumber, boardGame, draw, roundStartDate.toDate(), roundEndDate.toDate(), lastDroppedWord);
    }

    @Override
    public PlayGameMetaBean getPlayGameMetaBean(String playGameId){
        return playGameToPlayGameMetaBean(playGameRepository.findOne(playGameId));
    }

    @Override
    public PlayGame joinGame(String playGameId, User user){

        PlayGame playGame = playGameRepository.findOne(playGameId);
        PlayerGame playerGame = new PlayerGame(user.getId(), user.getName(), 0);
        if( playGame != null && !playGame.getPlayerGameList().contains(playerGame) ) {
            logger.info(user + " join " + playGame);
            playGame.getPlayerGameList().add(playerGame);
            playGame = playGameRepository.save(playGame);
        }
        return playGame;
    }

    @Override
    public List<PlayerGame> getPlayerGameList(String playGameId){
        PlayGame playGame = playGameRepository.findOne(playGameId);
        return playGame.getPlayerGameList();
    }

    @Override
    public List<ConnectedUserBean> getConnectedUserList() {
        return connectedUserRepository.findAll();
    }

    @Override
    public PlayGame getPlayGame(String playGameId) {
        return playGameRepository.findOne(playGameId);
    }
}
