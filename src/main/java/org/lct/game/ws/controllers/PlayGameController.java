package org.lct.game.ws.controllers;

import org.lct.game.ws.beans.model.ConnectedUserBean;
import org.lct.game.ws.beans.model.Game;
import org.lct.game.ws.beans.model.User;
import org.lct.game.ws.beans.model.gaming.PlayGame;
import org.lct.game.ws.beans.model.gaming.PlayerGame;
import org.lct.game.ws.beans.view.PlayGameMetaBean;
import org.lct.game.ws.beans.view.PreparedGame;
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
    @RequestMapping(value="/game/{id}", method= RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public String joinGame(@PathVariable("id") String id, @ModelAttribute User user){
        // TODO check autorisation
        playGameService
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

}
