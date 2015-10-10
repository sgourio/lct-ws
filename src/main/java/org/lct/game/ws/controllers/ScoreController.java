/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.controllers;

import org.lct.game.ws.beans.model.User;
import org.lct.game.ws.beans.view.MonthScoreBean;
import org.lct.game.ws.beans.view.PlayGameMetaBean;
import org.lct.game.ws.services.ScoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by sgourio on 04/10/15.
 */
@RestController
public class ScoreController {
    private static Logger logger = LoggerFactory.getLogger(ScoreController.class);

    @Autowired
    private ScoreService scoreService;

    /**
     * Get score for this month
     * @return the PlayGameMetaData
     */
    @RequestMapping(value="/ranking/{year}/{month}", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public MonthScoreBean getMonthScoreBean(@PathVariable("year") int year, @PathVariable("month") int month, @RequestParam(value="sort", required = false) String sort){
        return scoreService.getMonthScoreBean(year, month, sort);
    }
}
