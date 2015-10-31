/*
 * LCT
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.controllers;

import org.joda.time.DateTime;
import org.lct.game.ws.beans.model.Game;
import org.lct.game.ws.beans.model.User;
import org.lct.game.ws.beans.model.multiplex.MultiplexGame;
import org.lct.game.ws.beans.view.MultiplexGameMetaBean;
import org.lct.game.ws.beans.view.PreparedGame;
import org.lct.game.ws.services.EventService;
import org.lct.game.ws.services.GameService;
import org.lct.game.ws.services.MultiplexGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by sgourio on 31/10/15.
 */
@RestController
@RequestMapping(value="/multiplex")
public class MultiplexController {
    @Autowired
    private GameService gameService;

    @Autowired
    private EventService eventService;

    @Autowired
    private MultiplexGameService multiplexGameService;



    @RequestMapping(value="/openGame", method= RequestMethod.PUT, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(value= HttpStatus.CREATED)
    @ResponseBody
    public String openGame(@RequestBody PreparedGame preparedGame, @ModelAttribute User user){
        Game game = gameService.getById(preparedGame.getGameId());
        MultiplexGame multiplexGame = multiplexGameService.openGame(game, preparedGame.getGameName(), preparedGame.getRoundTime(), user, DateTime.now());
        return multiplexGame.getId();
    }

    /**
     * Get MultiplexGameMetaData for game with id
     * @return the PlayGameMetaData
     */
    @RequestMapping(value="/game/{id}", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public MultiplexGameMetaBean getMultiplexGameMetaBean(@PathVariable("id") String id, @ModelAttribute User user){
        return multiplexGameService.getMultiplexGameMetaBean(id);
    }

    @RequestMapping(value="/game/{id}/active/{round}", method= RequestMethod.GET)
    @ResponseStatus(value= HttpStatus.OK)
    public void changeRound(@PathVariable("id") String id, @PathVariable("round") int round, @ModelAttribute User user){
        eventService.publishMultiplexRound(id, multiplexGameService.getRound(id, round));
    }


}
