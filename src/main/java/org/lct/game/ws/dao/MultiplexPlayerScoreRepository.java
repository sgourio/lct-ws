/*
 * LCT
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.dao;

import org.lct.game.ws.beans.model.multiplex.MultiplexPlayerScore;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by sgourio on 03/11/15.
 */
public interface MultiplexPlayerScoreRepository extends MongoRepository<MultiplexPlayerScore, String> {

    public List<MultiplexPlayerScore> findByMultiplexIdAndRoundNumber(String multiplexId, int roundNumber);
    public List<MultiplexPlayerScore> findByMultiplexIdAndRoundNumberLessThanEqualOrderByNameAsc(String multiplexId, int roundNumber);
    public MultiplexPlayerScore findOneByMultiplexIdAndRoundNumberAndName(String multiplexId, int roundNumber, String name);


}
