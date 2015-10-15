/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.beans.view;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sgourio on 04/09/15.
 */
public class GameScore {

    private final List<PlayerGameScore> playerGameScoreList;
    private final Word topWord;
    private final int topTotal;

    public GameScore(List<PlayerGameScore> playerGameScoreList, Word topWord, int topTotal) {
        this.playerGameScoreList = playerGameScoreList;
        this.topWord = topWord;
        this.topTotal = topTotal;
    }

    public List<PlayerGameScore> getPlayerGameScoreList() {
        return playerGameScoreList;
    }

    public Word getTopWord() {
        return topWord;
    }

    public int getTopTotal() {
        return topTotal;
    }

    static public class PlayerGameScore{
        private final String name;
        private final int total;
        private final Word lastWord;
        private final int percentageFromTop;
        private final int lastBonus;

        public PlayerGameScore(String name, int total, Word lastWord, int percentageFromTop, int lastBonus) {
            this.name = name;
            this.total = total;
            this.lastWord = lastWord;
            this.percentageFromTop = percentageFromTop;
            this.lastBonus = lastBonus;
        }

        public String getName() {
            return name;
        }

        public int getTotal() {
            return total;
        }

        public Word getLastWord() {
            return lastWord;
        }

        public int getPercentageFromTop() {
            return percentageFromTop;
        }

        public int getLastBonus() {
            return lastBonus;
        }
    }
}
