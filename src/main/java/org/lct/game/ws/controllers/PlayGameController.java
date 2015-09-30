package org.lct.game.ws.controllers;

import org.joda.time.DateTime;
import org.lct.dictionary.beans.Dictionary;
import org.lct.game.ws.beans.model.Chat;
import org.lct.game.ws.beans.model.ConnectedUserBean;
import org.lct.game.ws.beans.model.Game;
import org.lct.game.ws.beans.model.User;
import org.lct.game.ws.beans.model.gaming.PlayGame;
import org.lct.game.ws.beans.model.gaming.PlayerGame;
import org.lct.game.ws.beans.view.*;
import org.lct.game.ws.dao.ChatRepository;
import org.lct.game.ws.services.EventService;
import org.lct.game.ws.services.GameService;
import org.lct.game.ws.services.PlayGameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @Autowired
    private ChatRepository chatRepository;

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
        DateTime startAt = new DateTime(toStartGame.getStartDate());
        startAt = startAt.withMillisOfSecond(0);
        PlayGame result = playGameService.setUpGame(playGame, startAt);
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
     * Join game
     * @return the PlayGameMetaData
     */
    @RequestMapping(value="/game/{id}/join", method= RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public String joinGame(@PathVariable("id") String id, @ModelAttribute User user){
        // TODO check autorisation
        PlayGame playGame = playGameService.joinGame(id, user);
        if( playGame != null ) {
            eventService.publishPlayers(playGame, playGameService.getPlayerListForGame(id));
        }
        return "joined";
    }

    /**
     * qui game
     * @return the PlayGameMetaData
     */
    @RequestMapping(value="/game/{id}/quit", method= RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public String quitGame(@PathVariable("id") String id, @ModelAttribute User user){
        // TODO check autorisation
        PlayGame playGame = playGameService.quitGame(id, user);
        if( playGame != null ) {
            eventService.publishPlayers(playGame, playGameService.getPlayerListForGame(playGame.getId()));
        }
        return "quited";
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
        return playGameService.getPlayerListForGame(playGameId);
    }

    /**
     * Return the time left for next round or gameStart
     * @return Player list
     */
    @RequestMapping(value="/game/{id}/timer", method= RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public String getTimer(@PathVariable("id") String playGameId, @ModelAttribute User user){
        String result = "";
        if( playGameId != null ) {
            PlayGame playGame = playGameService.getPlayGame(playGameId);
            result = String.valueOf(playGameService.getTimer(playGame));
        }
        return  result;
    }


    @RequestMapping(value="/game/{id}/round/current", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public Round getCurrentRound(@PathVariable("id") String playGameId, @ModelAttribute User user){
        PlayGame playGame = playGameService.getPlayGame(playGameId);
        return playGameService.getRound(playGame, new DateTime());
    }

    @RequestMapping(value="/game/{id}/word", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public WordResult putWord(@PathVariable("id") String playGameId, @RequestBody() PutWord putWord, @ModelAttribute User user){
        DateTime callDate = DateTime.now();
        PlayGame playGame = playGameService.getPlayGame(playGameId);
        Round round = playGameService.getRound(playGame,callDate);
        if( round.getRoundNumber() == putWord.getRoundNumber() ) {
            WordResult wordResult = playGameService.word(user, playGameId, callDate, putWord.getWordReference(), Dictionary.french);
            return wordResult;
        }else{
            logger.info("Round has finished");
            return new WordResult(null, 0 , new ArrayList<Word>());
        }
    }

    @RequestMapping(value="/game/{id}/scores", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public GameScore getScores(@PathVariable("id") String playGameId){
        DateTime callDate = DateTime.now();
        PlayGame playGame = playGameService.getPlayGame(playGameId);
        return playGameService.getScores(playGame, callDate);
    }



    @RequestMapping(value="/chat/{id}", method= RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public String writeChat(@PathVariable("id") String chatId, @RequestBody String message, @ModelAttribute User user){
        eventService.addChatMessage(user, message, chatId);
        eventService.publishChat(chatId);
        return chatId;
    }

    @RequestMapping(value="/chat/{id}", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public Chat getChat(@PathVariable("id") String chatId){
        return chatRepository.findOne(chatId);
    }

//    public String retest(){
//        PGame pGame = ImmutablePGame.builder()
//                .gameName("test")
//                .creationDate(new Date())
//                .endDate(new Date())
//                .owner("test")
//                .roundTime(0)
//                .startDate(new Date())
//                .status("test")
//                .build();
//        try {
//            PGameDTO pGameDTO = new PGameDTO(null, pGame);
//            pGameDTO = pGameRepository.save(pGameDTO);
//            pGameDTO = pGameRepository.findOne(pGameDTO.getId());
//            logger.info(pGameDTO.getpGame().getGameName());
//        }catch (Throwable t){
//            logger.error("",t);
//        }
//        return "";
//    }
}
