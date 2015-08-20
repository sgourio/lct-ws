/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.beans.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.lct.gameboard.ws.beans.model.Tile;
import org.lct.gameboard.ws.beans.view.DroppedWord;

import java.util.List;

/**
 * Template round of a game
 * Created by sgourio on 26/05/15.
 */
public class Round {

    private final List<Tile> draw;
    private final DroppedWord droppedWord;

    public Round(@JsonProperty("draw") List<Tile> draw, @JsonProperty("droppedWord") DroppedWord droppedWord) {
        this.draw = draw;
        this.droppedWord = droppedWord;
    }

    public List<Tile> getDraw() {
        return draw;
    }

    public DroppedWord getDroppedWord() {
        return droppedWord;
    }
}
