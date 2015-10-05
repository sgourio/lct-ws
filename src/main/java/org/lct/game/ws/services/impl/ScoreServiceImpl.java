/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.services.impl;

import org.lct.game.ws.beans.model.MonthlyScore;
import org.lct.game.ws.beans.model.MonthlyScoreGame;
import org.lct.game.ws.beans.view.MonthScoreBean;
import org.lct.game.ws.dao.MonthlyScoreRepository;
import org.lct.game.ws.services.ScoreService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by sgourio on 04/10/15.
 */
public class ScoreServiceImpl implements ScoreService{

    private final MonthlyScoreRepository monthlyScoreRepository;

    public ScoreServiceImpl(MonthlyScoreRepository monthlyScoreRepository) {
        this.monthlyScoreRepository = monthlyScoreRepository;
    }

    @Override
    public MonthScoreBean getMonthScoreBean(int year, int month, String sort) {


        List<MonthlyScore> monthlyScoreList = this.monthlyScoreRepository.findByYearAndMonth(year, month);

        List<MonthScoreBean.MonthScoreLineBean> monthScoreLineBeanList = new ArrayList<MonthScoreBean.MonthScoreLineBean>();

        for(MonthlyScore monthlyScore : monthlyScoreList){
            int points = 0;
            int totalScore = 0;
            int topScore = 0;
            for(MonthlyScoreGame monthlyScoreGame : monthlyScore.getMonthlyScoreGameList()){
                points += monthlyScoreGame.getPoints();
                totalScore += monthlyScoreGame.getScore();
                topScore += monthlyScoreGame.getTopScore();
            }
            int percent = (int) (((double) totalScore / topScore) * 10000);

            MonthScoreBean.MonthScoreLineBean monthScoreLineBean = new MonthScoreBean.MonthScoreLineBean(monthlyScore.getName(), monthlyScore.getMonthlyScoreGameList().size(), points, percent );
            monthScoreLineBeanList.add(monthScoreLineBean);
        }

        if( sort == null || sort.equals("score")){
            Collections.sort(monthScoreLineBeanList, new Comparator<MonthScoreBean.MonthScoreLineBean>() {
                @Override
                public int compare(MonthScoreBean.MonthScoreLineBean o1, MonthScoreBean.MonthScoreLineBean o2) {
                    return Integer.valueOf(o2.getScore()).compareTo(o1.getScore());
                }
            });
        }else if ( sort.equals("percent")){
            Collections.sort(monthScoreLineBeanList, new Comparator<MonthScoreBean.MonthScoreLineBean>() {
                @Override
                public int compare(MonthScoreBean.MonthScoreLineBean o1, MonthScoreBean.MonthScoreLineBean o2) {
                    return Integer.valueOf(o2.getPercentFromTop()).compareTo(o1.getPercentFromTop());
                }
            });
        }

        return new MonthScoreBean(monthScoreLineBeanList);
    }
}
