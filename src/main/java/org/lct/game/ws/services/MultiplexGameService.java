/*
 * LCT
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.services;

import org.joda.time.DateTime;
import org.lct.dictionary.beans.Dictionary;
import org.lct.game.ws.beans.model.Game;
import org.lct.game.ws.beans.model.User;
import org.lct.game.ws.beans.model.multiplex.MultiplexGame;
import org.lct.game.ws.beans.model.multiplex.MultiplexPlayerScore;
import org.lct.game.ws.beans.view.MultiplexGameMetaBean;
import org.lct.game.ws.beans.view.MultiplexPlayerTotalScoreBean;
import org.lct.game.ws.beans.view.WordResult;

import java.util.List;

/**
 * Created by sgourio on 31/10/15.
 */
public interface MultiplexGameService {

    public MultiplexGame openGame(Game game, String clubId, String name, int roundTime, User user, DateTime atTime);

    public org.lct.game.ws.beans.view.Round getRound(MultiplexGame multiplexGame, int roundNumber);
    public org.lct.game.ws.beans.view.Round getRound(String multiplexGameId, int roundNumber);

    public MultiplexGameMetaBean getMultiplexGameMetaBean(MultiplexGame multiplexGame);
    public MultiplexGameMetaBean getMultiplexGameMetaBean(String multiplexGameId);

    public MultiplexPlayerScore saveScore(MultiplexPlayerScore multiplexPlayerScore);
    public List<MultiplexPlayerScore> getRoundScore(String multiplexId, int roundNumber);
    public List<MultiplexPlayerTotalScoreBean> getRoundTotalScore(String multiplexId, int roundNumber);
    public MultiplexPlayerScore getPlayerRoundScore(String multiplexId, int roundNumber, String name);
    public MultiplexPlayerScore getPlayerRoundScore(String playerScoreId);

    public WordResult word(String multiplexId, int roundNumber, String wordReference, Dictionary dictionary);

}
