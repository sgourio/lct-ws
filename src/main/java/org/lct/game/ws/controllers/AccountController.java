/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.controllers;

import org.joda.time.DateTime;
import org.lct.game.ws.beans.model.Game;
import org.lct.game.ws.beans.model.MonthlyScore;
import org.lct.game.ws.beans.model.User;
import org.lct.game.ws.beans.view.UserBean;
import org.lct.game.ws.services.ScoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Created by sgourio on 11/10/15.
 */
@RestController
@RequestMapping(value="/account")
public class AccountController {

    private static Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private ScoreService scoreService;

    @RequestMapping(value="/me", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public UserBean getUser(@ModelAttribute User user) throws Exception{
        return new UserBean(user.getId(), user.getName(), user.getProfilPictureURL(), user.getProfilLink());
    }

    @RequestMapping(value="/me/scores", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public MonthlyScore getUserScores(@ModelAttribute User user) throws Exception{
        DateTime now = DateTime.now();
        return scoreService.getMonthScoreBeanForUser( now.getYear(), now.getMonthOfYear(), user.getId() );
    }
}
