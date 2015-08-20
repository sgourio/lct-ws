package org.lct.game.ws.controllers;

import org.lct.game.ws.beans.model.Game;
import org.lct.game.ws.beans.view.GameMetaBean;
import org.lct.game.ws.services.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Controler for administration action.
 * Restrict to admin profiles ( defined in application-config.xml )
 * Created by sgourio on 17/08/15.
 */
@RestController
@RequestMapping(value="/admin")
public class AdminController {

    private static Logger logger = LoggerFactory.getLogger(AdminController.class);
    @Autowired
    private GameService gameService;

    @RequestMapping(value="/generate", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public GameMetaBean generate(){
        Game game = gameService.generate();
        return gameService.gameToGameMeta(game);
    }
}
