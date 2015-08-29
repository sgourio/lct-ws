package org.lct.game.ws.services;

import org.lct.game.ws.beans.model.ConnectedUserBean;
import org.lct.game.ws.beans.model.User;
import org.lct.game.ws.beans.model.gaming.PlayGame;
import org.lct.game.ws.beans.view.PlayGameMetaBean;
import org.lct.game.ws.beans.view.Round;

import java.util.List;

/**
 * Created by sgourio on 11/08/15.
 */
public interface EventService {

    public void registrerUser(User user);

    public void joinGame(PlayGame playGame);

    public void publishTimer(PlayGame playGame, long countDown);

    public void publishRound(PlayGame playGame, Round round);

    public void publishMetaData(PlayGameMetaBean playGameMetaBean);
}
