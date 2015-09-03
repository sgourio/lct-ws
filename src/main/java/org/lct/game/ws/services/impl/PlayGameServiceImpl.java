package org.lct.game.ws.services.impl;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.lct.dictionary.beans.Dictionary;
import org.lct.dictionary.services.DictionaryService;
import org.lct.game.ws.beans.PlayGameStatus;
import org.lct.game.ws.beans.model.ConnectedUserBean;
import org.lct.game.ws.beans.model.Game;
import org.lct.game.ws.beans.model.Round;
import org.lct.game.ws.beans.model.User;
import org.lct.game.ws.beans.model.gaming.*;
import org.lct.game.ws.beans.view.PlayGameMetaBean;
import org.lct.game.ws.beans.view.Word;
import org.lct.game.ws.beans.view.WordResult;
import org.lct.game.ws.dao.ConnectedUserRepository;
import org.lct.game.ws.dao.PlayGameRepository;
import org.lct.game.ws.services.EventService;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
    private final EventService eventService;
    private final DictionaryService dictionaryService;

    public PlayGameServiceImpl(PlayGameRepository playGameRepository, BoardService boardService, ConnectedUserRepository connectedUserRepository, SchedulerFactoryBean schedulerFactoryBean, EventService eventService, DictionaryService dictionaryService) {
        this.playGameRepository = playGameRepository;
        this.boardService = boardService;
        this.connectedUserRepository = connectedUserRepository;
        this.schedulerFactoryBean = schedulerFactoryBean;
        this.eventService = eventService;
        this.dictionaryService = dictionaryService;
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
                .setCreationDate(new Date())
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

    public PlayGame setUpGame(final PlayGame playGame, final Date startDate){
        DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
        logger.info("Start " + playGame +" at " + fmt.print(startDate.getTime()));
        PlayGameBuilder playGameBuilder = new PlayGameBuilder(playGame);
        playGameBuilder.setStartDate(startDate);
        PlayGame startedGame = playGameBuilder.createPlayGame();

        startedGame = playGameRepository.save(startedGame);
        this.eventService.publishMetaData(getPlayGameMetaBean(startedGame));
        scheduleGame(startedGame);
        return startedGame;
    }

    public PlayGame startPlayGame(final PlayGame playGame){
        PlayGameBuilder playGameBuilder = new PlayGameBuilder(playGame);
        playGameBuilder.setStatus(PlayGameStatus.running.getId());
        PlayGame startedGame = playGameBuilder.createPlayGame();
        return playGameRepository.save(startedGame);
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
        jobDataMap.put("playGameId", playGame.getId());
        jobDataMap.put("playGameService", this);
        jobDataMap.put("eventService", eventService);
        JobDetail jobDetail = JobBuilder.newJob(GameJob.class).setJobData(jobDataMap).build();
        return jobDetail;
    }

    @Override
    public void scheduleAllRunningGames() {
        List<PlayGame> playGameList = playGameRepository.findByStatus(PlayGameStatus.opened.getId());
        playGameList.addAll(playGameRepository.findByStatus(PlayGameStatus.running.getId()));
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
        if( roundNumber == 0){
            return null;
        }

        Round round = playGame.getGame().getRoundList().get(roundNumber - 1);
        BoardGameTemplate boardGameTemplate = new BoardGameTemplate(BoardGameTemplateEnum.classic.getSquares());
        BoardGame boardGame = new BoardGame(boardGameTemplate);
        for( int i = 0; i < roundNumber - 1 ; i++) {
            boardGame = boardGame.dropWord(playGame.getGame().getRoundList().get(i).getDroppedWord());
        }
        DateTime gameStartDate = new DateTime(playGame.getStartDate());
        Duration duration = new Duration((roundNumber-1) * playGame.getRoundTime() * 1000);
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
        return new org.lct.game.ws.beans.view.Round(playGame.getId(), roundNumber, boardGame, draw, roundStartDate.toDate(), roundEndDate.toDate(), lastDroppedWord);
    }

    /**
     * Synchronise the timer of game players
     */
    @Scheduled(fixedRate = 10000)
    public void updateTimers(){
        List<PlayGame> playGameList = playGameRepository.findByStatus(PlayGameStatus.running.getId());
        playGameList.addAll(playGameRepository.findByStatus(PlayGameStatus.opened.getId()));
        for(PlayGame playGame : playGameList){
            if( playGame.getStartDate() != null ) {
                eventService.publishTimer(playGame, getTimer(playGame));
            }
        }
    }

    @Override
    public long getTimer(PlayGame playGame){
        long countDown = 0;
        if( playGame != null && playGame.getStartDate() != null && !playGame.getStatus().equals(PlayGameStatus.ended.getId())) {
            org.lct.game.ws.beans.view.Round round = getRound(playGame, DateTime.now());
            if (round == null) {
                countDown = new Duration(DateTime.now(), new DateTime(playGame.getStartDate())).getStandardSeconds();
            } else {
                countDown = new Duration(DateTime.now(), new DateTime(round.getEndDate())).getStandardSeconds();
            }
        }
        return Math.abs(countDown);
    }

    @Override
    public PlayGameMetaBean getPlayGameMetaBean(String playGameId){
        return getPlayGameMetaBean(playGameRepository.findOne(playGameId));
    }

    @Override
    public PlayGameMetaBean getPlayGameMetaBean(PlayGame playGame){
        return playGameToPlayGameMetaBean(playGame);
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
    public PlayGame quitGame(String playGameId, User user){
        PlayGame playGame = playGameRepository.findOne(playGameId);
        PlayerGame playerGame = new PlayerGame(user.getId(), user.getName(), 0);
        if( playGame != null && playGame.getPlayerGameList().contains(playerGame) ) {
            logger.info(user + " quit " + playGame);
            playGame.getPlayerGameList().remove(playerGame);
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

    public WordResult result(List<DroppedWord> droppedWordList, Dictionary dictionary){
        List<Word> subWordList = new ArrayList<>();
        DroppedWord mainDroppedWord = droppedWordList.get(0);
        boolean valid = boardService.isValid(dictionaryService, dictionary, mainDroppedWord.getSquareList());
        Word mainWord = new Word(mainDroppedWord.getValue(), mainDroppedWord.getPoints(), valid);
        int total = mainDroppedWord.getPoints();
        boolean validTurn = valid;
        for( int i = 1 ; i < droppedWordList.size() ; i++){
            DroppedWord droppedWord = droppedWordList.get(i);
            total += droppedWord.getPoints();
            valid = boardService.isValid(dictionaryService, dictionary, droppedWord.getSquareList());
            subWordList.add(new Word(droppedWord.getValue(), droppedWord.getPoints(), valid));
            if( !valid ){
                validTurn = false;
            }
        }

        if(!validTurn){
            total = 0;
        }
        return new WordResult(mainWord, total, subWordList);
    }


    /**
     *  Get droppedWords in the game
     * @param boardGame
     * @param wordReference for example: MUT(I)EZ 	 3B
     * @return
     */
    public List<DroppedWord> getDroppedWords(BoardGame boardGame, String wordReference){
        List<DroppedWord> droppedWordList = new ArrayList<>();
        String[] wordReferenceTab = wordReference.split("\t");
        String serializedValue = wordReferenceTab[0];
        String reference = wordReferenceTab[1];
        if( serializedValue != null && reference != null ){
            try {
                int row = getRow(reference);
                int column = getColumn(reference);
                boolean horizontal = isHorizontal(reference);
                if( !horizontal ){
                    row = getColumn(reference);
                    column = getRow(reference);
                    boardGame = boardGame.transpose();
                }

                int currentColumn = column;
                List<DroppedTile> droppedTileList = getDroppedTileList(serializedValue);
                BoardGame activeBoardGame = boardGame;
                for( DroppedTile droppedTile : droppedTileList ){
                    if( activeBoardGame.getSquares()[row][currentColumn].isEmpty() ) {
                        activeBoardGame = activeBoardGame.dropTile(row, currentColumn, droppedTile);
                        DroppedWord verticalWord = boardService.getVerticalWord(activeBoardGame, row, currentColumn);
                        if( verticalWord.getSquareList().size() > 1 ) {
                            if (!horizontal) {
                                verticalWord = verticalWord.transpose();
                            }
                            droppedWordList.add(verticalWord);
                        }
                    }
                    currentColumn++;
                }
                DroppedWord horizontalWord = boardService.getHorizontalWord(activeBoardGame, row, column);
                if( !horizontal ){
                    horizontalWord = horizontalWord.transpose();
                }
                droppedWordList.add(0, horizontalWord);

            } catch (Exception e) {
                logger.error("",e);
            }
        }
        return droppedWordList;
    }

    public WordResult word(User user, String playGameId, DateTime atTime, String wordReference, Dictionary dictionary){
        WordResult wordResult = null;
        PlayGame playGame = this.getPlayGame(playGameId);
        if( playGame != null ) {
            org.lct.game.ws.beans.view.Round round = this.getRound(playGame, atTime);
            PlayRound playRound = playGame.getPlayRoundList().get(round.getRoundNumber() - 1);
            BoardGame boardGame = round.getBoardGame();
            List<DroppedWord> droppedWordList = getDroppedWords(boardGame, wordReference);
            wordResult = result(droppedWordList, dictionary);
            if( wordResult.getTotal() > 0) {
                PlayerRound playerRound = new PlayerRound(user.getId(), user.getName(), wordResult.getTotal(), wordReference);
                boolean toSave = true;
                Iterator<PlayerRound> playerRoundIterator = playRound.getPlayerRoundList().iterator();
                while(playerRoundIterator.hasNext()) {
                    PlayerRound player = playerRoundIterator.next();
                    if (player.getUserId().equals(user.getId())) {
                        if( player.getScore() > playerRound.getScore()){
                            toSave = false;
                        }else{
                            playerRoundIterator.remove();
                        }
                    }
                }
                if( toSave ){
                    playRound.getPlayerRoundList().add(playerRound);
                    playGameRepository.save(playGame);
                }
            }
        }
        return wordResult;
    }

    private int getRow(String reference) throws Exception {
        for( char c : reference.toCharArray()) {
            if(CharUtils.isAsciiAlpha(c)) {
                return "ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(c);
            }
        }
        throw new Exception("No alpha numeric char in '"+ reference+"'");
    }

    private int getColumn(String reference) throws Exception{
        String num = reference.replaceAll("[^0-9 ]", "");
        if(StringUtils.isNumeric(num)){
            return Integer.parseInt(num) - 1;
        }
        throw new Exception("No numeric part in '" + reference +"'");
    }

    private boolean isHorizontal(String reference){
        return CharUtils.isAsciiAlpha(reference.toCharArray()[0]);
    }

    private List<DroppedTile> getDroppedTileList(String word){
        List<DroppedTile> result = new ArrayList<>();
        boolean wildcard = false;
        for( char c : word.toCharArray()) {
            if( c == '('){
                wildcard = true;
                continue;
            }
            if( c == ')'){
                wildcard = false;
                continue;
            }
            Tile tile;
            if( wildcard ){
                tile = Tile.WILDCARD;
            }else{
                tile = charToTile(c);
            }

            result.add(new DroppedTile(tile, String.valueOf(c)));
        }
        return result;
    }

    private Tile charToTile(char c){
        try {
            if( c == '?'){
                return Tile.WILDCARD;
            }else {
                return (Tile) Tile.class.getDeclaredField(String.valueOf(c)).get(null);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }






}
