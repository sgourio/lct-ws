/*
 * LCT
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.controllers;

/**
 * Created by sgourio on 08/11/15.
 */

import org.joda.time.DateTime;
import org.lct.game.ws.beans.model.Club;
import org.lct.game.ws.beans.model.User;
import org.lct.game.ws.beans.view.ClubBean;
import org.lct.game.ws.beans.view.UserBean;
import org.lct.game.ws.services.ClubService;
import org.lct.game.ws.services.UserService;
import org.lct.game.ws.services.exceptions.IncompleteGameException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value="/club")
public class ClubController {

    private final Logger logger = LoggerFactory.getLogger(ClubController.class);

    @Autowired
    private ClubService clubService;

    @Autowired
    private UserService userService;

    @RequestMapping(value="", method= RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public String create(@RequestBody String name, @ModelAttribute User user) throws IncompleteGameException {
        logger.info("save club " + name +"...");
        Club club = clubService.create(name, DateTime.now());
        userService.subscribeClub(user, club.getId());
        return club.getId();
    }

    @RequestMapping(value="/{id}", method= RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public void rename(@PathVariable("id") String id, @RequestBody String name, @ModelAttribute User user) throws IncompleteGameException {
        logger.info("renamed club : " + name + "...");
        if( user.getClubIds().contains(id )) {
            clubService.rename(id, name);
        }
    }

    @RequestMapping(value="/{id}", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public ClubBean getClub(@PathVariable("id") String id, @ModelAttribute User owner){
        Club club = clubService.findById(id);
        if( club == null ){
            return null;
        }
        List<User> userList = userService.findUserListByClub(id);
        List<UserBean> userBeanList = new ArrayList<>();
        for( User user : userList ){
            userBeanList.add(new UserBean(user.getId(), user.getName(), user.getProfilPictureURL(), user.getProfilLink(), user.getNickname()));
        }
        return new ClubBean(userBeanList, club.getStatus(), club.getName(), club.getCreationDate(), club.getId());
    }

    @RequestMapping(value="", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public List<ClubBean> getMyClubs(@ModelAttribute User owner){
        List<ClubBean> result = new ArrayList<>();
        List<Club> clubList = clubService.findByUser(owner);
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

}
