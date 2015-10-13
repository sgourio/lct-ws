/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.controllers;

import org.lct.dictionary.services.DictionaryService;
import org.lct.game.ws.beans.model.Game;
import org.lct.game.ws.beans.view.GameMetaBean;
import org.lct.game.ws.beans.model.User;
import org.lct.game.ws.services.GameService;
import org.lct.game.ws.services.exceptions.IncompleteGameException;
import org.lct.gameboard.ws.services.BoardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * REST Controller to manage Game ( template of game ).
 * Created by sgourio on 25/05/15.
 */
@RestController
@RequestMapping(value="/game/{lang}")
public class GameController {

    private static Logger logger = LoggerFactory.getLogger(GameController.class);

    @Autowired
    private BoardService boardService;

    @Autowired
    private DictionaryService dictionaryService;


    @Autowired
    private GameService gameService;

    /**
     * Add a new game template in database
     * @param lang fr or en
     * @param game game to save
     * @param user owner of the game
     * @return the id of the game saved
     * @throws Exception
     */
    @RequestMapping(value="/add", method= RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(value= HttpStatus.CREATED)
    @ResponseBody
    public String create(@PathVariable("lang") String lang, @RequestBody Game game, @ModelAttribute User user) throws Exception{
        logger.info("Create game...");
        try {
            gameService.add(new Game(game.getName(), game.getLang(), game.getRoundList(), user.getId(), user.getNickname(), new Date()));
        }catch (Exception e){
            logger.error("",e);
            throw e;
        }
        return game.getId();
    }

    /**
     * Replace an existing game in database.
     * @param lang fr or en
     * @param id id of the game to replace
     * @param game game to put
     * @param user owner of the game
     * @throws IncompleteGameException
     */
    @RequestMapping(value="/{id}", method= RequestMethod.PUT, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public void put(@PathVariable("lang") String lang, @PathVariable("id") String id, @RequestBody Game game, @ModelAttribute User user) throws IncompleteGameException {
        logger.info("save game " + game.getName() +"...");
        gameService.save(new Game(game.getName(), game.getLang(), game.getRoundList(), user.getId(), user.getNickname(), new Date()));
    }

    /**
     * Get the game template
     * @param lang fr or en
     * @param id id of the game template to get
     * @return a Game
     * @throws IncompleteGameException
     */
    @RequestMapping(value="/{id}", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public Game readGame(@PathVariable("lang") String lang, @PathVariable("id") String id) throws IncompleteGameException {
        logger.info("Get game " + id);
        return gameService.getById(id);
    }

    /**
     * Get the games prepared by the owner
     * @param lang fr or en
     * @param user owner of the game
     * @return a list of GameMetaBean
     * @throws IncompleteGameException
     */
    @RequestMapping(value="/", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public List<GameMetaBean> findGameByAuthor(@PathVariable("lang") String lang, @ModelAttribute("user") User user) throws IncompleteGameException {
        logger.info("Get game for author" + user);
        List<Game> gameList = gameService.getByAuthorId(user.getId());
        return gameService.gameToGameMeta(gameList);
    }

    /**
     * Get a random set of generated games by computer
     * @param lang fr or en
     * @param max limit of the number of items in result
     * @return a list of GameMetaBean
     * @throws IncompleteGameException
     */
    @RequestMapping(value="/auto", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public List<GameMetaBean> findGameAuto(@PathVariable("lang") String lang, @RequestParam("max") Integer max) throws IncompleteGameException {
        List<Game> gameList = gameService.getByAuthorId("auto", max);
        return gameService.gameToGameMeta(gameList);
    }




}
