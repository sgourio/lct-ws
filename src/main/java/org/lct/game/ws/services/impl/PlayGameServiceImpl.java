package org.lct.game.ws.services.impl;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.lct.game.ws.beans.PlayGameStatus;
import org.lct.game.ws.beans.model.ConnectedUserBean;
import org.lct.game.ws.beans.model.Game;
import org.lct.game.ws.beans.model.Round;
import org.lct.game.ws.beans.model.User;
import org.lct.game.ws.beans.model.gaming.PlayGame;
import org.lct.game.ws.beans.model.gaming.PlayRound;
import org.lct.game.ws.beans.model.gaming.PlayerGame;
import org.lct.game.ws.beans.model.gaming.PlayerRound;
import org.lct.game.ws.beans.view.PlayGameMetaBean;
import org.lct.game.ws.dao.ConnectedUserRepository;
import org.lct.game.ws.dao.PlayGameRepository;
import org.lct.game.ws.services.PlayGameService;
import org.lct.gameboard.ws.beans.model.BoardGameTemplate;
import org.lct.gameboard.ws.beans.view.BoardGame;
import org.lct.gameboard.ws.beans.view.DroppedWord;
import org.lct.gameboard.ws.services.BoardService;
import org.lct.gameboard.ws.services.impl.BoardGameTemplateEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;

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


    public PlayGameServiceImpl(PlayGameRepository playGameRepository, BoardService boardService, ConnectedUserRepository connectedUserRepository) {
        this.playGameRepository = playGameRepository;
        this.boardService = boardService;
        this.connectedUserRepository = connectedUserRepository;
    }

    @Override
    public PlayGame openGame(Game game, String name, int roundTime, Date startDate, User user) {
        PlayerGame owner = new PlayerGame(user.getId(), user.getName(), 0);
        List<PlayRound> playRoundList = new ArrayList<>(game.getRoundList().size());
        for( Round round : game.getRoundList()){
            playRoundList.add(new PlayRound(new ArrayList<PlayerRound>()));
        }
        PlayGame playGame = new PlayGame(game, name, startDate, owner, new ArrayList<PlayerGame>(), playRoundList, PlayGameStatus.opened.getId(), roundTime);
        playGame = playGameRepository.save(playGame);
        logger.info("Game '"+name+"'opened by " + user);
        return playGame;
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
        DateTime dateTime = new DateTime(playGame.getStartDate());
        Duration duration = new Duration(dateTime, atTime);
        if( duration.getStandardHours() > 3){
            return playGame.getPlayRoundList().size();
        }
        return ((int) duration.getStandardSeconds() / playGame.getRoundTime()) + 1;

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
        return new org.lct.game.ws.beans.view.Round(roundNumber, boardGame, round.getDraw(), roundStartDate.toDate(), roundEndDate.toDate(), lastDroppedWord);
    }

    @Override
    public PlayGameMetaBean getPlayGameMetaBean(String playGameId){
        return playGameToPlayGameMetaBean(playGameRepository.findOne(playGameId));
    }

    @Override
    public void joinGame(String playGameId, User user){
        PlayGame playGame = playGameRepository.findOne(playGameId);
        playGame.getPlayerGameList().add(new PlayerGame(user.getId(), user.getName(), 0));
        playGameRepository.save(playGame);
    }

    @Override
    public List<ConnectedUserBean> getConnectedUserList() {
        return connectedUserRepository.findAll();
    }
}
