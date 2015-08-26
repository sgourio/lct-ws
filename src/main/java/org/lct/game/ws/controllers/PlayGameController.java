package org.lct.game.ws.controllers;

import org.joda.time.DateTime;
import org.lct.game.ws.beans.model.ConnectedUserBean;
import org.lct.game.ws.beans.model.Game;
import org.lct.game.ws.beans.model.User;
import org.lct.game.ws.beans.model.gaming.PlayGame;
import org.lct.game.ws.beans.model.gaming.PlayGameBuilder;
import org.lct.game.ws.beans.model.gaming.PlayerGame;
import org.lct.game.ws.beans.view.PlayGameMetaBean;
import org.lct.game.ws.beans.view.PreparedGame;
import org.lct.game.ws.beans.view.Round;
import org.lct.game.ws.beans.view.ToStartGame;
import org.lct.game.ws.services.EventService;
import org.lct.game.ws.services.GameService;
import org.lct.game.ws.services.PlayGameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Controller to manage PlayGame ( games actually played by users )
 * Created by sgourio on 10/08/15.
 */
@RestController
@RequestMapping(value="/play")
public class PlayGameController {
    private static Logger logger = LoggerFactory.getLogger(PlayGameController.class);

    @Autowired
    private GameService gameService;

    @Autowired
    private PlayGameService playGameService;

    @Autowired
    private EventService eventService;

    /**
     * Open a game for playing ( the game is not started yet, but it is available to enter in )
     * @param preparedGame game attributes
     * @param user owner of the PlayGame
     * @return the id of the PlayGame opened
     */
    @RequestMapping(value="/openGame", method= RequestMethod.PUT, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(value= HttpStatus.CREATED)
    @ResponseBody
    public String openGame(@RequestBody PreparedGame preparedGame, @ModelAttribute User user){
        Game game = gameService.getById(preparedGame.getGameId());
        PlayGame playGame = playGameService.openGame(game, preparedGame.getGameName(), preparedGame.getRoundTime(), null, user);
        return playGame.getId();
    }

    /**
     * Start a game at a time for playing
     * @param toStartGame game attributes
     * @param user owner of the PlayGame
     * @return the id of the PlayGame started
     */
    @RequestMapping(value="/startGame", method= RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public String startGame(@RequestBody ToStartGame toStartGame, @ModelAttribute User user){
        PlayGame playGame = playGameService.getPlayGame(toStartGame.getPlayGameId());
        PlayGame result = playGameService.startGame(playGame, toStartGame.getStartDate(), user);
        return result.getId();
    }


    /**
     * List of PlayGame is status "opened" or "running"
     * @return the PlayGame list
     */
    @RequestMapping(value="/games", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public List<PlayGameMetaBean> getActualPlayGameList(){
        return playGameService.getActualPlayGame();
    }

    /**
     * Get PlayGameMetaData for game with id
     * @return the PlayGameMetaData
     */
    @RequestMapping(value="/game/{id}", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public PlayGameMetaBean getPlayGameMetaBean(@PathVariable("id") String id, @ModelAttribute User user){
        return playGameService.getPlayGameMetaBean(id);
    }

    /**
     * Get PlayGameMetaData for game with id
     * @return the PlayGameMetaData
     */
    @RequestMapping(value="/game/{id}/join", method= RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public String joinGame(@PathVariable("id") String id, @ModelAttribute User user){
        // TODO check autorisation
        PlayGame playGame = playGameService.joinGame(id, user);
        if( playGame != null ) {
            eventService.joinGame(playGame);
        }
        return "joined";
    }

    /**
     * Return the list of Connected User in the application
     * @return ConnectedUser list
     */
    @RequestMapping(value="/connectedUser", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public List<ConnectedUserBean> getConnectedUserList(){
        return playGameService.getConnectedUserList();
    }

    /**
     * Return the list of a game player
     * @return Player list
     */
    @RequestMapping(value="/game/{id}/players", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public List<PlayerGame> getPlayers(@PathVariable("id") String playGameId, @ModelAttribute User user){
        return playGameService.getPlayerGameList(playGameId);
    }

    @RequestMapping(value="/game/{id}/round/current", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public Round getCurrentRound(@PathVariable("id") String playGameId, @ModelAttribute User user){
        PlayGame playGame = playGameService.getPlayGame(playGameId);
        return playGameService.getRound(playGame, new DateTime());
    }

}
