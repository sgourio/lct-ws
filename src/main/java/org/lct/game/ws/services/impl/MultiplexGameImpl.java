/*
 * LCT
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.services.impl;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.lct.game.ws.beans.PlayGameStatus;
import org.lct.game.ws.beans.model.Game;
import org.lct.game.ws.beans.model.Round;
import org.lct.game.ws.beans.model.User;
import org.lct.game.ws.beans.model.gaming.PlayGame;
import org.lct.game.ws.beans.model.gaming.PlayRound;
import org.lct.game.ws.beans.model.multiplex.MultiplexGame;
import org.lct.game.ws.beans.view.MultiplexGameMetaBean;
import org.lct.game.ws.beans.view.PlayGameMetaBean;
import org.lct.game.ws.beans.view.Word;
import org.lct.game.ws.dao.MultiplexRepository;
import org.lct.game.ws.services.MultiplexGameService;
import org.lct.gameboard.ws.beans.model.BoardGameTemplate;
import org.lct.gameboard.ws.beans.model.Tile;
import org.lct.gameboard.ws.beans.view.BoardGame;
import org.lct.gameboard.ws.beans.view.DroppedTile;
import org.lct.gameboard.ws.beans.view.DroppedWord;
import org.lct.gameboard.ws.beans.view.Square;
import org.lct.gameboard.ws.services.impl.BoardGameTemplateEnum;
import org.springframework.cache.annotation.Cacheable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sgourio on 31/10/15.
 */
public class MultiplexGameImpl implements MultiplexGameService{

    private final MultiplexRepository multiplexRepository;

    public MultiplexGameImpl(MultiplexRepository multiplexRepository) {
        this.multiplexRepository = multiplexRepository;
    }

    @Override
    public MultiplexGame openGame(Game game, String name, int roundTime, User user, DateTime atTime) {
        List<String> admnistratorsIds = new ArrayList<String>();
        admnistratorsIds.add(user.getId());
        List<PlayRound> playRoundList = new ArrayList<PlayRound>(game.getRoundList().size());
        int total = 0;
        for( Round round : game.getRoundList()){
            DroppedWord droppedWord = round.getDroppedWord();
            total += droppedWord.getPoints();
            Word word = new Word(droppedWord.getValue(), droppedWord.getReference(), droppedWord.getPoints(), true, isScrabble(droppedWord));
            playRoundList.add( new PlayRound(word, total) );
        }
        MultiplexGame multiplexGame = new MultiplexGame(null, game, name, atTime.toDate(), null, null, user.getNickname(), admnistratorsIds, playRoundList, PlayGameStatus.opened.getId(), roundTime);
        multiplexGame = multiplexRepository.save(multiplexGame);
        return multiplexGame;
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

    @Override
    public org.lct.game.ws.beans.view.Round getRound(String multiplexGameId, int roundNumber){
        MultiplexGame multiplexGame = multiplexRepository.findOne(multiplexGameId);
        if( roundNumber == 0 ){
            return getRound0(multiplexGame);
        }
        if( multiplexGame.getPlayRoundList().size() < roundNumber){
            return getEndedRound(multiplexGame);
        }
        return getRound(multiplexGame, roundNumber);
    }
    @Override
    @Cacheable("round")
    public org.lct.game.ws.beans.view.Round getRound(MultiplexGame multiplexGame, int roundNumber){
        if( roundNumber == 0){
            return null;
        }
        Round round = multiplexGame.getGame().getRoundList().get(roundNumber - 1);
        BoardGameTemplate boardGameTemplate = new BoardGameTemplate(BoardGameTemplateEnum.classic.getSquares());
        BoardGame boardGame = new BoardGame(boardGameTemplate);
        for( int i = 0; i < roundNumber - 1 ; i++) {
            boardGame = boardGame.dropWord(multiplexGame.getGame().getRoundList().get(i).getDroppedWord(), i+1);
        }
        DateTime gameStartDate = new DateTime(multiplexGame.getStartDate());
        Duration duration = new Duration((roundNumber-1) * multiplexGame.getRoundTime() * 1000);
        DateTime roundStartDate = gameStartDate.plus(duration);
        DateTime roundEndDate = roundStartDate.plusSeconds(multiplexGame.getRoundTime());
        DroppedWord lastDroppedWord = null;
        List<DroppedTile> oldTiles = new ArrayList<DroppedTile>();
        if( roundNumber > 1 ){
            Round lastRound = multiplexGame.getGame().getRoundList().get(roundNumber-2);
            lastDroppedWord = lastRound.getDroppedWord();
            for( Tile tile : lastRound.getDraw()){
                oldTiles.add(new DroppedTile(tile, tile.getValue()));
            }
            for( Square square : lastDroppedWord.getSquareList()){
                if ( square.isJustDropped() ) {
                    oldTiles.remove(square.getDroppedTile());
                }
            }

        }
        List<DroppedTile> draw = new ArrayList<DroppedTile>();
        for( Tile tile : round.getDraw()){
            draw.add(new DroppedTile(tile, tile.getValue()));
        }

        List<DroppedTile> tempOld = new ArrayList<DroppedTile>(oldTiles);
        List<DroppedTile> newTiles = new ArrayList<DroppedTile>();
        for( DroppedTile droppedTile : draw){
            if( tempOld.contains(droppedTile)){
                tempOld.remove(droppedTile);
            }else{
                newTiles.add(droppedTile);
            }
        }

        return new org.lct.game.ws.beans.view.Round(multiplexGame.getId(), roundNumber, boardGame, draw, oldTiles, newTiles, roundStartDate.toDate(), roundEndDate.toDate(), lastDroppedWord);
    }

    private org.lct.game.ws.beans.view.Round getEndedRound(MultiplexGame multiplexGame){
        BoardGameTemplate boardGameTemplate = new BoardGameTemplate(BoardGameTemplateEnum.classic.getSquares());
        BoardGame boardGame = new BoardGame(boardGameTemplate);
        int nbRounds = multiplexGame.getGame().getRoundList().size();
        for( int i = 0; i < nbRounds ; i++) {
            boardGame = boardGame.dropWord(multiplexGame.getGame().getRoundList().get(i).getDroppedWord(), i+1);
        }

        List<DroppedTile> draw = new ArrayList<DroppedTile>();
        Round lastRound = multiplexGame.getGame().getRoundList().get(nbRounds -1);

        for( Tile tile : lastRound.getDraw()){

            draw.add(new DroppedTile(tile, tile.getValue()));
        }
        for( Square square : lastRound.getDroppedWord().getSquareList()){
            if( square.isJustDropped() ) {
                draw.remove(square.getDroppedTile());
            }
        }
        return new org.lct.game.ws.beans.view.Round(multiplexGame.getId(), nbRounds+1, boardGame, draw, new ArrayList<DroppedTile>(), new ArrayList<DroppedTile>(), null, null, lastRound.getDroppedWord());
    }

    private org.lct.game.ws.beans.view.Round getRound0(MultiplexGame multiplexGame){
        BoardGameTemplate boardGameTemplate = new BoardGameTemplate(BoardGameTemplateEnum.classic.getSquares());
        BoardGame boardGame = new BoardGame(boardGameTemplate);
        List<DroppedTile> draw = new ArrayList<DroppedTile>();
        return new org.lct.game.ws.beans.view.Round(multiplexGame.getId(), 0, boardGame, draw, new ArrayList<DroppedTile>(), new ArrayList<DroppedTile>(), null, null, null);
    }


    private MultiplexGameMetaBean multiplexGameToMultiplexGameMetaBean(MultiplexGame multiplexGame){
        Date endDate = null;
        int actualRoundNumber = 0;
        if( multiplexGame.getStartDate() != null ) {
            DateTime startDate = new DateTime(multiplexGame.getStartDate());
            endDate = multiplexGame.getEndDate();
            DateTime now = new DateTime();
        }
        return new MultiplexGameMetaBean(multiplexGame.getId(),
                multiplexGame.getName(),
                multiplexGame.getOwner(),
                multiplexGame.getGame().getAuthorName(),
                multiplexGame.getGame().getAuthorId(),
                multiplexGame.getGame().getName(),
                multiplexGame.getPlayRoundList().size(),
                multiplexGame.getRoundTime(),
                actualRoundNumber,
                0,
                multiplexGame.getStatus(),
                multiplexGame.getStartDate(),
                endDate);
    }

    @Override
    public MultiplexGameMetaBean getMultiplexGameMetaBean(MultiplexGame multiplexGame){
        return multiplexGameToMultiplexGameMetaBean(multiplexGame);
    }

    @Override
    public MultiplexGameMetaBean getMultiplexGameMetaBean(String multiplexGameId){
        return getMultiplexGameMetaBean(multiplexRepository.findOne(multiplexGameId));
    }
}
