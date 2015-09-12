package org.lct.game.ws.beans.model.gaming;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.lct.game.ws.beans.view.Word;
import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * A round of a game
 * Created by sgourio on 14/08/15.
 */
public class PlayRound {
    private final Word word; // best word
    private final int score; // total score

    public PlayRound(Word word, int score) {
        this.word = word;
        this.score = score;
    }

    public Word getWord() {
        return word;
    }

    public int getScore() {
        return score;
    }
}
