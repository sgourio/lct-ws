/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.dao;

import org.lct.game.ws.beans.model.MonthlyScore;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by sgourio on 03/10/15.
 */
public interface MonthlyScoreRepository extends MongoRepository<MonthlyScore, String> {

    public MonthlyScore findByUserIdAndYearAndMonth(String userId, int year, int month);
    public List<MonthlyScore> findByYearAndMonth(int year, int month);
    public MonthlyScore findByYearAndMonthAndUserId(int year, int month, String userId);
}
