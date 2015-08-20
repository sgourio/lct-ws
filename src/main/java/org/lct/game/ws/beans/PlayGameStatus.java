package org.lct.game.ws.beans;

/**
 * Constant for PlayGame status
 * opened : users can subscribe but the game is not started
 * running : the game is actualy running
 * ended : the game is ended
 * Created by sgourio on 19/08/15.
 */
public enum PlayGameStatus {

    opened("opened"),
    running("running"),
    ended("ended")
    ;

    private String id;

    PlayGameStatus(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
