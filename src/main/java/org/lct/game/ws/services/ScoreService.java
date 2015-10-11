/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.services;

import org.lct.game.ws.beans.model.MonthlyScore;
import org.lct.game.ws.beans.view.MonthScoreBean;

/**
 * Created by sgourio on 04/10/15.
 */
public interface ScoreService {

    public MonthScoreBean getMonthScoreBean(int year, int month, String sort);
    public MonthlyScore getMonthScoreBeanForUser(int year, int month, String userId);

}
