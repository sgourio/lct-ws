/*
 * LCT
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.beans.model.multiplex;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.lct.game.ws.beans.model.gaming.PlayRound;
import org.lct.game.ws.beans.view.Word;
import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * Created by sgourio on 03/11/15.
 */
public class MultiplexPlayerScore {

    @Id
    private String id;
    private final String multiplexId;
    private final String name;
    private final int roundNumber;
    private final int score; // round score
    private final Word word; // word reference
    private final int bonus;

    public MultiplexPlayerScore(@JsonProperty("id") String id, @JsonProperty("multiplexId") String multiplexId,@JsonProperty("name") String name,@JsonProperty("roundNumber") int roundNumber,@JsonProperty("score") int score,@JsonProperty("word") Word word,@JsonProperty("bonus") int bonus) {
        this.id = id;
        this.multiplexId = multiplexId;
        this.name = name;
        this.roundNumber = roundNumber;
        this.score = score;
        this.word = word;
        this.bonus = bonus;
    }

    public String getId() {
        return id;
    }

    public String getMultiplexId() {
        return multiplexId;
    }

    public String getName() {
        return name;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public int getScore() {
        return score;
    }

    public Word getWord() {
        return word;
    }

    public int getBonus() {
        return bonus;
    }
}
