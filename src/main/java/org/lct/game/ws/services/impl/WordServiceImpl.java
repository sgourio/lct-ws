/*
 * LCT
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.services.impl;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.lct.dictionary.beans.Dictionary;
import org.lct.dictionary.services.DictionaryService;
import org.lct.game.ws.beans.view.Round;
import org.lct.game.ws.beans.view.Word;
import org.lct.game.ws.beans.view.WordResult;
import org.lct.game.ws.services.WordService;
import org.lct.gameboard.ws.beans.model.Tile;
import org.lct.gameboard.ws.beans.view.BoardGame;
import org.lct.gameboard.ws.beans.view.DroppedTile;
import org.lct.gameboard.ws.beans.view.DroppedWord;
import org.lct.gameboard.ws.beans.view.Square;
import org.lct.gameboard.ws.services.BoardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sgourio on 03/11/15.
 */
public class WordServiceImpl implements WordService {

    private final static Logger logger = LoggerFactory.getLogger(WordServiceImpl.class);

    private final BoardService boardService;
    private final DictionaryService dictionaryService;

    public WordServiceImpl(BoardService boardService, DictionaryService dictionaryService) {
        this.boardService = boardService;
        this.dictionaryService = dictionaryService;
    }

    public WordResult putWord(Round round, String wordReference, Dictionary dictionary){
        BoardGame boardGame = round.getBoardGame();
        List<DroppedWord> droppedWordList = getDroppedWords(boardGame, wordReference);
        return result(round, droppedWordList, dictionary);
    }

    private WordResult result(Round round, List<DroppedWord> droppedWordList, Dictionary dictionary){
        List<Word> subWordList = new ArrayList<>();
        DroppedWord mainDroppedWord = droppedWordList.get(0);
        boolean valid = boardService.isValid(dictionaryService, dictionary, mainDroppedWord.getSquareList());
        boolean scrabble = isScrabble(mainDroppedWord);
        Word mainWord = new Word(mainDroppedWord.getValue(), mainDroppedWord.getReference(), mainDroppedWord.getPoints(), valid, scrabble);
        int total = mainDroppedWord.getPoints();
        boolean validTurn = valid;
        for( int i = 1 ; i < droppedWordList.size() ; i++){
            DroppedWord droppedWord = droppedWordList.get(i);
            valid = boardService.isValid(dictionaryService, dictionary, droppedWord.getSquareList());
            subWordList.add(new Word(droppedWord.getValue(), droppedWord.getReference(), droppedWord.getPoints(), valid, false));
            if( !valid ){
                validTurn = false;
            }
        }

        if( !checkLettersFromDraw(round, mainDroppedWord)){
            validTurn = false;
        }
        BoardGame activeBoardGame = round.getBoardGame().dropWord(mainDroppedWord, round.getRoundNumber());
        if( !boardService.isAttached(activeBoardGame, mainDroppedWord.getRow(), mainDroppedWord.getColumn())){
            validTurn = false;
        }

        if(!validTurn){
            total = 0;
        }
        return new WordResult(mainWord, total, subWordList);
    }

    private boolean checkLettersFromDraw(Round round, DroppedWord droppedWord){
        first : for( Square square : droppedWord.getSquareList()){
            if( square.isJustDropped() ){
                for( DroppedTile droppedTile : round.getDraw() ){
                    if( droppedTile.getTile().equals( square.getDroppedTile().getTile() )){
                        continue first;
                    }
                }
                return false;
            }
        }
        return true;
    }

    private boolean isScrabble(DroppedWord droppedWord){
        int nbDropped = 0;
        for( Square square : droppedWord.getSquareList()){
            if( square.isJustDropped() ){
                nbDropped++;
            }
        }
        return nbDropped == 7;
    }

    /**
     *  Get droppedWords in the game
     * @param boardGame
     * @param wordReference for example: MUT(I)EZ 	 3B
     * @return
     */
    private List<DroppedWord> getDroppedWords(BoardGame boardGame, String wordReference){
        List<DroppedWord> droppedWordList = new ArrayList<>();
        String[] wordReferenceTab = wordReference.split("\t");
        String serializedValue = wordReferenceTab[1];
        String reference = wordReferenceTab[0];
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
                int points = 0;
                for( DroppedTile droppedTile : droppedTileList ){
                    if( activeBoardGame.getSquares()[row][currentColumn].isEmpty() ) {
                        activeBoardGame = activeBoardGame.dropTile(row, currentColumn, droppedTile);
                        DroppedWord verticalWord = boardService.getVerticalWord(activeBoardGame, row, currentColumn);
                        if( verticalWord.getSquareList().size() > 1 ) {
                            if (!horizontal) {
                                verticalWord = verticalWord.transpose();
                            }
                            droppedWordList.add(verticalWord);
                            points += verticalWord.getPoints();
                        }
                    }
                    currentColumn++;
                }
                DroppedWord horizontalWord = boardService.getHorizontalWord(activeBoardGame, row, column, points);
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
