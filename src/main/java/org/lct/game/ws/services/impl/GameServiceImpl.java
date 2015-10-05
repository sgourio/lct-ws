/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.services.impl;

import org.joda.time.DateTime;
import org.lct.dictionary.beans.Dictionary;
import org.lct.dictionary.services.DictionaryService;
import org.lct.game.ws.beans.model.Game;
import org.lct.game.ws.beans.view.GameMetaBean;
import org.lct.game.ws.beans.model.Round;
import org.lct.game.ws.dao.GameRepository;
import org.lct.game.ws.services.GameService;
import org.lct.game.ws.services.exceptions.IncompleteGameException;
import org.lct.game.ws.services.exceptions.InvalidRoundException;
import org.lct.gameboard.ws.beans.model.BoardGameTemplate;
import org.lct.gameboard.ws.beans.model.Tile;
import org.lct.gameboard.ws.beans.model.TileType;
import org.lct.gameboard.ws.beans.view.BoardGame;
import org.lct.gameboard.ws.beans.view.DroppedWord;
import org.lct.gameboard.ws.beans.view.Square;
import org.lct.gameboard.ws.services.BoardService;
import org.lct.gameboard.ws.services.impl.BoardGameTemplateEnum;
import org.lct.gameboard.ws.services.impl.DeckTemplateEnum;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.*;

/**
 * Created by sgourio on 25/05/15.
 */
public class GameServiceImpl implements GameService{
    private static Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);


    private final GameRepository gameRepository;

    private final BoardService boardService;

    private final DictionaryService dictionaryService;

    private final SchedulerFactoryBean schedulerFactoryBean;

    public GameServiceImpl(GameRepository gameRepository, BoardService boardService, DictionaryService dictionaryService, SchedulerFactoryBean schedulerFactoryBean) {
        this.gameRepository = gameRepository;
        this.boardService = boardService;
        this.dictionaryService = dictionaryService;
        this.schedulerFactoryBean = schedulerFactoryBean;
    }

    @Override
    public void add(Game game) throws RuntimeException {
        if( check(game) ) {
            this.gameRepository.insert(game);
        }else{
            logger.info("Error, game check failed");
        }
    }

    @Override
    public void save(Game game) throws RuntimeException {
        if( check(game) ) {
            this.gameRepository.save(game);
        }
    }

    @Override
    public Game getById(String gameId) {
        return this.gameRepository.findOne(gameId);
    }

    @Override
    public List<Game> getByAuthorId(String authorId) {
        return this.gameRepository.findByAuthorId(authorId);
    }

    @Override
    public List<Game> getByAuthorId(String authorId, int max) {
        int nbPage = (int) (this.gameRepository.countByAuthorId("auto") / max) - 1;
        if( nbPage < 0 ) nbPage = 0;
        int page = (int) Math.round( Math.random() * nbPage);
        return this.gameRepository.findByAuthorId(authorId, new PageRequest(page, max));
    }

    /**
     * Check if game is complete
     * @param game
     * @return
     * @throws RuntimeException
     */
    private boolean check(Game game) throws RuntimeException{
        DeckTemplateEnum deckTemplate = DeckTemplateEnum.french;
        BoardGameTemplate boardGameTemplate = new BoardGameTemplate(BoardGameTemplateEnum.classic.getSquares());
        BoardGame boardGame = new BoardGame(boardGameTemplate);

        List<Tile> deck = new ArrayList<Tile>(deckTemplate.getTileList());

        int roundNumber = 0;
        Iterator<Round> roundIterator = game.getRoundList().iterator();
        while(roundIterator.hasNext()){
            Round round = roundIterator.next();
            roundNumber++;
            for( Tile tile : round.getDraw()){
                Iterator<Tile> deckIterator = deck.iterator();
                while(deckIterator.hasNext()){
                    Tile deckTile = deckIterator.next();
                    if( tile.equals(deckTile)){
                        deckIterator.remove();
                        break;
                    }
                }
            }
//            deck.removeAll(round.getDraw());
            if( isGameFinish(deck, round.getDraw()) ){
                throw new InvalidRoundException("The game should be finished at turn " + roundNumber);
            }

            Set<DroppedWord> droppedWordList = boardService.findBestWord(dictionaryService, Dictionary.french, boardGame, round.getDraw());
            if( !droppedWordList.contains(round.getDroppedWord()) ){
                throw new InvalidRoundException("Not the best word possible in round " + roundNumber);
            }

            boardGame = boardGame.dropWord(round.getDroppedWord());
            List<Tile> resultDraw = new ArrayList<Tile>(round.getDraw());
            for(Square square : round.getDroppedWord().getSquareList()){
                resultDraw.remove(square.getDroppedTile().getTile());
            }

            if( !roundIterator.hasNext() && !isGameFinish(deck, resultDraw)){
                throw new IncompleteGameException("The game is not finished at round " + roundNumber);
            }

            for( Tile tile : resultDraw){
                deck.add(tile);
            }
        }

        return true;
    }


    private boolean isGameFinish(List<Tile> deck, List<Tile> draw){
        List<Tile> totalList = new ArrayList<Tile>(deck);
        totalList.addAll(draw);
        return (totalList.size() <= 1 || isOnlyConsonants(totalList) || isOnlyVowels(totalList)) && !isYOrWildcardInTiles(totalList);
    }

    /**
     * Check if there is only vowel in tileList (Since Y is considered as vowel or consonant, this function is not equals to !isOnlyConsonants)
     * @param tileList
     * @return true if there is only vowels
     */
    public boolean isOnlyVowels(List<Tile> tileList){
        boolean onlyVowels = true;
        for( Tile tile : tileList){
            if( tile.isConsonant()){
                onlyVowels = false;
                break;
            }
        }
        return onlyVowels;
    }

    /**
     * Check if there is only consonant in tileList (Since Y is considered as vowel or consonant, this function is not equals to !isOnlyVowels)
     * @param tileList
     * @return true s'il n'y a que des consonnes
     */
    public boolean isOnlyConsonants(List<Tile> tileList){
        boolean onlyConsonants = true;
        for( Tile tile : tileList){
            if( tile.isVowel()){
                onlyConsonants = false;
                break;
            }
        }
        return onlyConsonants;
    }

    public boolean isYOrWildcardInTiles(List<Tile> tileList){
        for( Tile tile : tileList){
            if( tile.isConsonant() && tile.isVowel() ){
                return true;
            }
        }
        return false;
    }

    public Game generate(){
        List<Tile> deck = DeckTemplateEnum.french.getTileList();
        BoardGameTemplate boardGameTemplate = new BoardGameTemplate(BoardGameTemplateEnum.classic.getSquares());
        BoardGame boardGame = new BoardGame(boardGameTemplate);
        int turnNumber = 0;
        List<Tile> currentDraw = new ArrayList<Tile>();
        List<Round> roundList = new ArrayList<Round>();
        while( !isGameFinish(deck, currentDraw) ){
            turnNumber++;
            currentDraw = draw(turnNumber, deck, currentDraw);

            Set<DroppedWord> droppedWordList = boardService.findBestWord(dictionaryService, Dictionary.french, boardGame, new ArrayList<Tile>(currentDraw));
            if( droppedWordList.size() > 0) {
                Round round = new Round(new ArrayList<Tile>(currentDraw), droppedWordList.iterator().next());
                roundList.add(round);
                boardGame = boardGame.dropWord(round.getDroppedWord());
                for(Square square : round.getDroppedWord().getSquareList()){
                    currentDraw.remove(square.getDroppedTile().getTile());
                }
                boardService.logBoardGame(boardGame);
            }else{
                turnNumber--;
                clearDraw(deck, currentDraw);
            }
        }
        String name = "#" + this.gameRepository.count();
        Game game = new Game(name, "fr", roundList,"auto", "auto");
        this.gameRepository.insert(game);
        logger.info("Save new generated game named " + name);
        return game;
    }

    private void clearDraw(List<Tile> deck, List<Tile> draw){
        deck.addAll(draw);
        draw.clear();
    }

    private List<Tile> draw(int turnNumber, List<Tile> deck, List<Tile> prev){
        int limit = Math.min(7 - prev.size(), deck.size());
        boolean correctDraw = false;
        int nbTest = 0;
        List<Tile> newDraw = new ArrayList<Tile>(prev);

        while( !correctDraw ){
            nbTest++;
            Collections.shuffle(deck);
            newDraw = new ArrayList<Tile>(prev);
            for( int i = 0 ;  i < limit ; i++){
                newDraw.add(deck.get(i));
            }
            correctDraw = this.checkDraw(turnNumber, newDraw, deck) || nbTest > 100;
        }
        if( correctDraw && nbTest <= 100){
            for( int i = 0 ;  i < limit ; i++){
                deck.remove(0);
            }
        }
        return newDraw;
    }

    private boolean checkDraw(int turnNumber, List<Tile> draw, List<Tile> deck){
        if( draw.size() < 7 ){
            return true;
        }
        int vowels = 0;
        int consonnants = 0;
        boolean isVowelLeft = !this.isOnlyConsonnant(deck);
        boolean isConsonnantLeft = !this.isOnlyVowel(deck);
        for(Tile tile : draw){
            if( tile.getTileType() != TileType.vowel ){
                consonnants++;
            }
            if( tile.getTileType() != TileType.consonant){
                vowels++;
            }
        }
        return (turnNumber >= 16 && ( (vowels >=1 && consonnants >= 1) || (vowels >=1 && !isConsonnantLeft) || ( !isVowelLeft && consonnants >= 1) )	) ||
                (turnNumber <= 15 && ((vowels >=2 && consonnants >= 2) || (vowels >=2 && !isConsonnantLeft) || ( !isVowelLeft && consonnants >= 2) ));
    }

    private boolean isOnlyVowel(List<Tile> tileList){
        for( Tile tile : tileList){
            if( tile.getTileType() != TileType.vowel){
                return false;
            }
        }
        return true;
    }

    private boolean isOnlyConsonnant(List<Tile> tileList){
        for( Tile tile : tileList){
            if( tile.getTileType() != TileType.consonant){
                return false;
            }
        }
        return true;
    }

    public void readyGame(Game game){


        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("test", "toto");
        JobDetail jobDetail = JobBuilder.newJob(GameJob.class).setJobData(jobDataMap).build();
        Trigger trigger1 = TriggerBuilder.newTrigger().startAt(DateBuilder.futureDate(10, DateBuilder.IntervalUnit.SECOND)).build();
        Scheduler scheduler = null;
        try {
            scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
            scheduler.scheduleJob(jobDetail, trigger1);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

//
//        SimpleTriggerFactoryBean simpleTriggerFactoryBean = new SimpleTriggerFactoryBean();
//        SchedulerFactoryBean fb = new SchedulerFactoryBean();
//        GameJob gameJob = new GameJob();
//        JobDetail jobDetail = (JobDetail) gameJob;
//        fb.setJobDetails(jobDetail);
    }

    public List<GameMetaBean> gameToGameMeta(List<Game> gameList){
        List<GameMetaBean> gameMetaBeanList = new ArrayList<GameMetaBean>();
        for( Game game : gameList ){
            gameMetaBeanList.add(gameToGameMeta(game));
        }
        return gameMetaBeanList;
    }

    public GameMetaBean gameToGameMeta(Game game){
        int maxScore = 0;
        for(Round round : game.getRoundList()){
            maxScore += round.getDroppedWord().getPoints();
        }
        return new GameMetaBean(game.getId(), game.getName(), game.getAuthorName(), game.getRoundList().size(), maxScore);
    }


    @Override
    public void automaticGameBuilder(){
        JobDetail jobDetail = buildJobDetail();
        ScheduleBuilder scheduleBuilder = DailyTimeIntervalScheduleBuilder
                .dailyTimeIntervalSchedule()
                .onEveryDay()
                .withIntervalInSeconds(1)
                .startingDailyAt(TimeOfDay.hourAndMinuteOfDay(3, 30))
                .endingDailyAt(TimeOfDay.hourAndMinuteOfDay(5, 30));
        TriggerBuilder triggerBuilder =  TriggerBuilder.newTrigger();
        triggerBuilder.startNow();
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

    private JobDetail buildJobDetail(){
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("gameService", this);
        JobDetail jobDetail = JobBuilder.newJob(AutoCreateGameJob.class).setJobData(jobDataMap).build();
        return jobDetail;
    }
}
