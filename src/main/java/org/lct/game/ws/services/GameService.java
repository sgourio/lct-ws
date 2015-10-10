/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.services;


import org.joda.time.DateTime;
import org.lct.game.ws.beans.model.Game;
import org.lct.game.ws.beans.view.GameMetaBean;

import java.util.List;

/**
 * Created by sgourio on 22/03/15.
 */
public interface GameService {

    /**
     * Create a new game
     * @param game game to add
     * @throws RuntimeException
     */
    public void add(Game game) throws RuntimeException;

    /**
     * Save an existing game
     * @param game game to save
     * @throws RuntimeException
     */
    public void save(Game game) throws RuntimeException;

    /**
     * Get a game by its id
     * @param gameId id of the game
     * @return a Game
     */
    public Game getById(String gameId);

    /**
     * Get game list by owner id
     * @param authorId id of the owner of the game
     * @return a game list
     */
    public List<Game> getByAuthorId(String authorId);

    /**
     * Get game list by owner id limit to 'max'
     * @param authorId owner id
     * @param max limit parameter
     * @return a game list
     */
    public List<Game> getByAuthorId(String authorId, int max);

    /**
     * Get game list by owner id and creation date after
     * @param authorId
     * @param creationDate
     * @return a game list
     */
    public List<Game> getByAuthorIdAndCreationDateAfter(String authorId, DateTime creationDate);

    /**
     * Generate an entire new game and save it
     * @return the Game generated
     */
    public Game generate();


    /**
     * wrapper to transform a game list in a GameMetaBean list
     * @param gameList game list to transform
     * @return a GameMetaBean list
     */
    public List<GameMetaBean> gameToGameMeta(List<Game> gameList);

    /**
     * wrapper to transform a game in a GameMetaBean
     * @param game game to transform
     * @return a GameMetaBean
     */
    public GameMetaBean gameToGameMeta(Game game);

    /**
     * Start automatic game builder.
     */
    public void automaticGameBuilder();

}
