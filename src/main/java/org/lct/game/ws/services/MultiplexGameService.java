/*
 * LCT
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.services;

import org.joda.time.DateTime;
import org.lct.game.ws.beans.model.Game;
import org.lct.game.ws.beans.model.User;
import org.lct.game.ws.beans.model.multiplex.MultiplexGame;
import org.lct.game.ws.beans.view.MultiplexGameMetaBean;

/**
 * Created by sgourio on 31/10/15.
 */
public interface MultiplexGameService {

    public MultiplexGame openGame(Game game, String name, int roundTime, User user, DateTime atTime);

    public org.lct.game.ws.beans.view.Round getRound(MultiplexGame multiplexGame, int roundNumber);
    public org.lct.game.ws.beans.view.Round getRound(String multiplexGameId, int roundNumber);

    public MultiplexGameMetaBean getMultiplexGameMetaBean(MultiplexGame multiplexGame);
    public MultiplexGameMetaBean getMultiplexGameMetaBean(String multiplexGameId);

}
