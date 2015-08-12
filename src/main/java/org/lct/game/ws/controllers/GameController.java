/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.lct.dictionary.services.DictionaryService;
import org.lct.game.ws.beans.model.Game;
import org.lct.game.ws.beans.model.GameMetaBean;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
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



//    @ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="The game is not complete")
//    @ExceptionHandler(IncompleteGameException.class)
//    @ResponseBody
//    public void notComplete() {
//        logger.info("exception");
//    }


    @RequestMapping(value="/add", method= RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(value= HttpStatus.CREATED)
    @ResponseBody
    public String create(@PathVariable("lang") String lang, @RequestBody Game game, @ModelAttribute User user) throws Exception{
        logger.info("Create game...");
        try {
            gameService.add(new Game(game.getName(), game.getLang(), game.getRoundList(), user.getId()));
        }catch (Exception e){
            logger.error("",e);
            throw e;
        }
        return game.getId();
    }

    @RequestMapping(value="/{id}", method= RequestMethod.PUT, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public void put(@PathVariable("lang") String lang, @PathVariable("id") String id, @RequestBody Game game, @ModelAttribute User user) throws IncompleteGameException {
        logger.info("save game " + game.getName() +"...");
        gameService.save(new Game(game.getName(), game.getLang(), game.getRoundList(), user.getId()));
    }

    @RequestMapping(value="/{id}", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public Game readGame(@PathVariable("lang") String lang, @PathVariable("id") String id) throws IncompleteGameException {
        logger.info("Get game " + id);
        return gameService.getById(id);
    }

    @RequestMapping(value="/", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public List<GameMetaBean> findGameByAuthor(@PathVariable("lang") String lang, @ModelAttribute("user") User user) throws IncompleteGameException {
        logger.info("Get game for author" + user);
        List<Game> gameList = gameService.getByAuthorId(user.getId());
        List<GameMetaBean> gameMetaBeanList = new ArrayList<GameMetaBean>();
        ObjectMapper mapper = new ObjectMapper();
        for( Game game : gameList ){
            GameMetaBean gameMetaBean = mapper.convertValue(game, GameMetaBean.class);
            gameMetaBeanList.add(gameMetaBean);
        }

        return gameMetaBeanList;
    }
}
