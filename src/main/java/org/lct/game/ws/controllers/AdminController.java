package org.lct.game.ws.controllers;

import org.lct.game.ws.beans.ClubStatus;
import org.lct.game.ws.beans.model.Club;
import org.lct.game.ws.beans.model.Game;
import org.lct.game.ws.beans.model.User;
import org.lct.game.ws.beans.view.ClubBean;
import org.lct.game.ws.beans.view.GameMetaBean;
import org.lct.game.ws.beans.view.UserBean;
import org.lct.game.ws.services.ClubService;
import org.lct.game.ws.services.GameService;
import org.lct.game.ws.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for administration action.
 * Restrict to admin profiles ( defined in application-config.xml )
 * Created by sgourio on 17/08/15.
 */
@RestController
@RequestMapping(value="/admin")
public class AdminController {

    private static Logger logger = LoggerFactory.getLogger(AdminController.class);
    @Autowired
    private GameService gameService;

    @Autowired
    private ClubService clubService;

    @Autowired
    private UserService userService;

    @RequestMapping(value="/generate", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public GameMetaBean generate(){
        Game game = gameService.generate();
        return gameService.gameToGameMeta(game);
    }

    @RequestMapping(value="/club", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public List<ClubBean> getClubs(@RequestParam("status") String status){
        List<ClubBean> result = new ArrayList<>();
        List<Club> clubList = clubService.findByStatus(ClubStatus.valueOf(status));
        for( Club club : clubList ) {
            List<User> userList = userService.findUserListByClub(club.getId());
            List<UserBean> userBeanList = new ArrayList<>();
            for (User user : userList) {
                userBeanList.add(new UserBean(user.getId(), user.getName(), user.getProfilPictureURL(), user.getProfilLink(), user.getNickname()));
            }
            result.add(new ClubBean(userBeanList, club.getStatus(), club.getName(), club.getCreationDate(), club.getId()));
        }
        return result;
    }

    @RequestMapping(value="/club/{id}/active", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public Club activateClub(@PathVariable("id") String id){
        return clubService.activate(id);
    }

    @RequestMapping(value="/club/{id}/suspend", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public Club suspendClub(@PathVariable("id") String id){
        return clubService.suspend(id);
    }
}
