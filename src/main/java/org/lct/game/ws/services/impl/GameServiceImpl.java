/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.services.impl;

import org.lct.dictionary.beans.Dictionary;
import org.lct.dictionary.services.DictionaryService;
import org.lct.game.ws.beans.model.Game;
import org.lct.game.ws.beans.model.Round;
import org.lct.game.ws.dao.GameRepository;
import org.lct.game.ws.services.GameService;
import org.lct.game.ws.services.exceptions.IncompleteGameException;
import org.lct.game.ws.services.exceptions.InvalidRoundException;
import org.lct.gameboard.ws.beans.model.BoardGameTemplate;
import org.lct.gameboard.ws.beans.model.Tile;
import org.lct.gameboard.ws.beans.view.BoardGame;
import org.lct.gameboard.ws.beans.view.DroppedWord;
import org.lct.gameboard.ws.beans.view.Square;
import org.lct.gameboard.ws.services.BoardService;
import org.lct.gameboard.ws.services.impl.BoardGameTemplateEnum;
import org.lct.gameboard.ws.services.impl.DeckTemplateEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by sgourio on 25/05/15.
 */
public class GameServiceImpl implements GameService{
    private static Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);


    private final GameRepository repository;

    private final BoardService boardService;

    private final DictionaryService dictionaryService;

    public GameServiceImpl(GameRepository repository, BoardService boardService, DictionaryService dictionaryService) {
        this.repository = repository;
        this.boardService = boardService;
        this.dictionaryService = dictionaryService;
    }

    @Override
    public void add(Game game) throws RuntimeException {
        if( check(game) ) {
            this.repository.insert(game);
        }else{
            logger.info("Error, game check failed");
        }
    }

    @Override
    public void save(Game game) throws RuntimeException {
        if( check(game) ) {
            this.repository.save(game);
        }
    }

    @Override
    public Game getById(String gameId) {
        return this.repository.findOne(gameId);
    }

    @Override
    public List<Game> getByAuthorId(String authorId) {
        return this.repository.findByAuthorId(authorId);
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
        }

        return true;
    }


    private boolean isGameFinish(List<Tile> deck, List<Tile> draw){
        List<Tile> totalList = new ArrayList<Tile>(deck);
        totalList.addAll(draw);
        return totalList.size() <= 1 || isOnlyConsonants(totalList) || isOnlyVowels(totalList);
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
}
