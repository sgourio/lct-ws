/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.services.exceptions;

/**
 * Created by sgourio on 27/05/15.
 */
public class IncompleteGameException extends IllegalArgumentException {

    public IncompleteGameException(String s) {
        super(s);
    }
}
