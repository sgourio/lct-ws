/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.beans.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * Created by sgourio on 03/10/15.
 */
public class MonthlyScore {

    @Id
    private String id;

    private int year;
    private int month;
    private String name;
    private String userId;
    private List<MonthlyScoreGame> monthlyScoreGameList;

    public MonthlyScore( @JsonProperty("id") String id, @JsonProperty("year") int year, @JsonProperty("month") int month, @JsonProperty("name") String name, @JsonProperty("userId") String userId, @JsonProperty("monthlyScoreGameList") List<MonthlyScoreGame> monthlyScoreGameList) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.name = name;
        this.userId = userId;
        this.monthlyScoreGameList = monthlyScoreGameList;
    }

    public String getId() {
        return id;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public List<MonthlyScoreGame> getMonthlyScoreGameList() {
        return monthlyScoreGameList;
    }
}
