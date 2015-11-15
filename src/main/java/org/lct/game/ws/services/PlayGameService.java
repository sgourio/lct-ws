package org.lct.game.ws.services;

import org.joda.time.DateTime;
import org.lct.dictionary.beans.Dictionary;
import org.lct.game.ws.beans.model.ConnectedUserBean;
import org.lct.game.ws.beans.model.Game;
import org.lct.game.ws.beans.model.User;
import org.lct.game.ws.beans.model.gaming.PlayGame;
import org.lct.game.ws.beans.model.gaming.PlayerGame;
import org.lct.game.ws.beans.view.GameScore;
import org.lct.game.ws.beans.view.PlayGameMetaBean;
import org.lct.game.ws.beans.view.WordResult;

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
     * @param user owner of the PlayGame
     * @return PlayGame with status opened
     */
    public PlayGame openGame(Game game, String name, int roundTime, User user, DateTime atTime, List<String> authorizedUserIds);
    public PlayGame openAutoGame(Game game, String name, int roundTime, DateTime atTime);

    /**
     * Set up a playGame by setting its start date
     * @param playGame
     * @param startDate
     * @return
     */
    public PlayGame setUpGame(final PlayGame playGame, final DateTime startDate);

    /**
     * Start a game
     * @param playGame
     * @return PlayGame with status running
     */
    public PlayGame startPlayGame(final PlayGame playGame);

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

    public int getRoundNumber(PlayGame playGame, DateTime atTime);

    public org.lct.game.ws.beans.view.Round getEndedRound(PlayGame playGame);
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
    public List<PlayGame> getActualPlayGame();

    /**
     * User join the game
     * @param playGameId
     * @param user
     * @param dateTime
     */
    public PlayGame joinGame(String playGameId, User user, DateTime dateTime);

    /**
     * quitGame
     * @param playGameId
     * @param user
     * @return
     */
    public PlayGame quitGame(String playGameId, User user);

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
    public List<PlayerGame> getPlayerListForGame(String playGameId);

    /**
     * Get PlayGame by id
     * @param playGameId
     * @return
     */
    public PlayGame getPlayGame(String playGameId);

    /**
     * Get timer for a game
     * @param playGame
     * @return
     */
    public long getTimer(PlayGame playGame);


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

    public void updateTimers();

    public WordResult word(User user, String playGameId, DateTime atTime, String wordReference, Dictionary dictionary);

    public GameScore getScores(PlayGame playGame, DateTime atTime);

    public void updateScores(PlayGame playGame, DateTime atTime);

    /**
     * Update month score
     * @param playGame
     */
    public void updateMonthlyScores(PlayGame playGame);

    public List<Game> getTodayGamesPlayed();

    /**
     * Is player has subscribeGame
     * @param playGameId
     * @param user
     * @return
     */
    public boolean isHasSubscirbeGame(String playGameId, User user);
}
