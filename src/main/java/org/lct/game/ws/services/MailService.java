/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.services;

/**
 * Created by sgourio on 12/10/15.
 */
public interface MailService {

    /**
     * Send message to LCT
     * @param message
     * @param sender
     */
    public void send(String message, String sender);


    /**
     * Send an invite
     * @param fromName
     * @param emailTo
     */
    public void invite(String fromName, String emailTo);
}
