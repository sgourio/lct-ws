/*
 * LCT
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.dao;

import org.lct.game.ws.beans.model.multiplex.MultiplexGame;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by sgourio on 31/10/15.
 */
public interface MultiplexRepository extends MongoRepository<MultiplexGame, String> {

}
