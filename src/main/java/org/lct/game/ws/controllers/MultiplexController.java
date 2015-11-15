/*
 * LCT
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.controllers;

import org.joda.time.DateTime;
import org.lct.dictionary.beans.Dictionary;
import org.lct.game.ws.beans.model.Game;
import org.lct.game.ws.beans.model.User;
import org.lct.game.ws.beans.model.multiplex.MultiplexGame;
import org.lct.game.ws.beans.model.multiplex.MultiplexPlayerScore;
import org.lct.game.ws.beans.view.*;
import org.lct.game.ws.controllers.services.EventService;
import org.lct.game.ws.services.GameService;
import org.lct.game.ws.services.MultiplexGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        MultiplexGame multiplexGame = multiplexGameService.openGame(game, preparedGame.getClubId(), preparedGame.getGameName(), preparedGame.getRoundTime(), user, DateTime.now());
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

    @RequestMapping(value="/game/{id}/message", method= RequestMethod.POST)
    @ResponseStatus(value= HttpStatus.OK)
    public void sendMessage(@PathVariable("id") String id, @RequestBody String message, @ModelAttribute User user){
        eventService.displayToMultiplex(id, message);
    }

    @RequestMapping(value="/game/{id}/score", method= RequestMethod.POST,  produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    public MultiplexPlayerScore sendScore(@RequestBody MultiplexPlayerScore multiplexPlayerScore, @ModelAttribute User user){
        return multiplexGameService.saveScore(multiplexPlayerScore);
    }

    @RequestMapping(value="/game/{id}/score/{round}", method= RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    public List<MultiplexPlayerScore> getRoundScore(@PathVariable("id") String id, @PathVariable("round") int round){
        return multiplexGameService.getRoundScore(id, round);
    }

    @RequestMapping(value="/game/{id}/total/{round}", method= RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    public List<MultiplexPlayerTotalScoreBean> getRoundTotalScore(@PathVariable("id") String id, @PathVariable("round") int round){
        return multiplexGameService.getRoundTotalScore(id, round);
    }

    @RequestMapping(value="/game/{id}/word", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public MultiplexPlayerScore putWord(@PathVariable("id") String multiplexGameId, @RequestBody MultiplexPuWord putWord, @ModelAttribute User user){
        MultiplexPlayerScore result = null;
        WordResult wordResult = multiplexGameService.word(multiplexGameId, putWord.getRoundNumber(), putWord.getWordReference(), Dictionary.french);
        if( putWord.getId() != null ) {
            result = multiplexGameService.getPlayerRoundScore(putWord.getId());
        }
        String id = result != null ? result.getId() : null;
        result = multiplexGameService.saveScore(new MultiplexPlayerScore(id, multiplexGameId, putWord.getName(), putWord.getRoundNumber(), wordResult.getTotal(), wordResult.getWord(), putWord.getBonus() ));
        return result;
    }
}
