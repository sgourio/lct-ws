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

    private final Word word;
    private final int total;
    private final List<Word> subWordList;


    public WordResult(Word word, int total, List<Word> subWordList) {
        this.word = word;
        this.total = total;
        this.subWordList = subWordList;
    }

    public Word getWord() {
        return word;
    }

    public int getTotal() {
        return total;
    }

    public List<Word> getSubWordList() {
        return subWordList;
    }
}
