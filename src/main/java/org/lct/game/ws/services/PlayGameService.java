package org.lct.game.ws.services;

import org.joda.time.DateTime;
import org.lct.game.ws.beans.model.ConnectedUserBean;
import org.lct.game.ws.beans.model.Game;
import org.lct.game.ws.beans.model.User;
import org.lct.game.ws.beans.model.gaming.PlayGame;
import org.lct.game.ws.beans.model.gaming.PlayerGame;
import org.lct.game.ws.beans.view.PlayGameMetaBean;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

/**
 * Created by sgourio on 14/08/15.
 */
public interface PlayGameService {

    /**
     * Open a game to the public
     * @param game game to open
     * @param name game name
     * @param roundTime time to play a round
     * @param startDate game start date
     * @param user owner of the PlayGame
     * @return PlayGame with status opened
     */
    public PlayGame openGame(Game game, String name, int roundTime, Date startDate, User user);

    /**
     * Start a game
     * @param playGame
     * @param startDate
     * @return PlayGame with status running
     */
    public PlayGame startGame(PlayGame playGame, Date startDate);

    /**
     * End a game
     * @param playGame
     * @return
     */
    public PlayGame endGame(final PlayGame playGame);

    /**
     * Get a round of playGame at time
     * @param playGame PlayGame
     * @param atTime Get a round at this time
     * @return a Round
     */
    public org.lct.game.ws.beans.view.Round getRound(PlayGame playGame, DateTime atTime);

    /**
     * Get a round of playGame by its number
     * @param playGame PlayGame
     * @param roundNumber round number
     * @return a Round
     */
    public org.lct.game.ws.beans.view.Round getRound(PlayGame playGame, int roundNumber);

    /**
     * Get playGame with status opened and status running
     * @return a PlayGameMetaBean list
     */
    public List<PlayGameMetaBean> getActualPlayGame();

    /**
     * Get playGameMetaBean from playGame with id
     * @param playGameId
     * @return
     */
    public PlayGameMetaBean getPlayGameMetaBean(String playGameId);


    /**
     * User join the game
     * @param playGameId
     * @param user
     */
    public PlayGame joinGame(String playGameId, User user);

    /**
     * Get the list of connected user in the application
     * @return connectedUserBean list
     */
    public List<ConnectedUserBean> getConnectedUserList();

    /**
     * Get list of game players
     * @param playGameId
     * @return
     */
    public List<PlayerGame> getPlayerGameList(String playGameId);

    /**
     * Get PlayGame by id
     * @param playGameId
     * @return
     */
    public PlayGame getPlayGame(String playGameId);


    /**
     * Enter the game in the scheduler to change its state periodically and inform players
     * @param playGame
     */
    public void scheduleGame(final PlayGame playGame);

    /**
     * Schedule all running games at application start up
     */
    public void scheduleAllRunningGames();

    /**
     * is game ended at this time
     * @param playGame
     * @param atTime
     * @return
     */
    public boolean isEnded(PlayGame playGame , DateTime atTime);
}
