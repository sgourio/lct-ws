/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.beans.view;

import java.util.List;

/**
 * Created by sgourio on 29/08/15.
 */
public class WordResult {

    private final String word;
    private final int point;
    private final List<Word> subWordList;

    public WordResult(String word, int point, List<Word> subWordList) {
        this.word = word;
        this.point = point;
        this.subWordList = subWordList;
    }

    public String getWord() {
        return word;
    }

    public int getPoint() {
        return point;
    }

    public List<Word> getSubWordList() {
        return subWordList;
    }
}
