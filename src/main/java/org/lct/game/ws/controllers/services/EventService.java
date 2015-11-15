package org.lct.game.ws.controllers.services;

import org.lct.game.ws.beans.model.User;
import org.lct.game.ws.beans.model.gaming.PlayGame;
import org.lct.game.ws.beans.model.gaming.PlayerGame;
import org.lct.game.ws.beans.model.multiplex.MultiplexGame;
import org.lct.game.ws.beans.view.GameScore;
import org.lct.game.ws.beans.view.MultiplexGameMetaBean;
import org.lct.game.ws.beans.view.PlayGameMetaBean;
import org.lct.game.ws.beans.view.Round;

import java.util.List;

/**
 * Created by sgourio on 11/08/15.
 */
public interface EventService {

    public void registrerUser(User user);

    public void publishPlayers(PlayGame playGame, List<PlayerGame> players);

    public void publishTimer(PlayGame playGame, long countDown);

    public void publishRound(PlayGame playGame, Round round);

    public void publishMetaData(PlayGame playGame);

    public void publishScores(PlayGame playGame, GameScore gameScore);

    public void addChatMessage(User user, String message, String chatId);

    public void publishChat(String chatId);

    public void publishMultiplexMetaData(MultiplexGameMetaBean multiplexGameMetaBean);

    public void publishMultiplexRound(String multiplexGameId, org.lct.game.ws.beans.view.Round round);

    public void displayToMultiplex(String multiplexGameId, String message);
}
