/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.beans.view;

import java.util.List;

/**
 * Score of a month
 * Created by sgourio on 04/10/15.
 */
public class MonthScoreBean {

    private final List<MonthScoreLineBean> monthScoreLineBeanList;


    public MonthScoreBean(List<MonthScoreLineBean> monthScoreLineBeanList) {
        this.monthScoreLineBeanList = monthScoreLineBeanList;
    }

    public List<MonthScoreLineBean> getMonthScoreLineBeanList() {
        return monthScoreLineBeanList;
    }

    public static final class MonthScoreLineBean{
        private final String name;
        private final int nbGames;
        private final int score;
        private final int percentFromTop;

        public MonthScoreLineBean(String name, int nbGames, int score, int percentFromTop) {
            this.name = name;
            this.nbGames = nbGames;
            this.score = score;
            this.percentFromTop = percentFromTop;
        }

        public String getName() {
            return name;
        }

        public int getNbGames() {
            return nbGames;
        }

        public int getScore() {
            return score;
        }

        public int getPercentFromTop() {
            return percentFromTop;
        }
    }

}
