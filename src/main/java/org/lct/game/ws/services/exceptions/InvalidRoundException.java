/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.services.exceptions;

/**
 * Created by sgourio on 30/05/15.
 */
public class InvalidRoundException extends RuntimeException {

    public InvalidRoundException(String message) {
        super(message);
    }
}
