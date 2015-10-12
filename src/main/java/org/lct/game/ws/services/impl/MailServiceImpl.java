/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.services.impl;

import org.lct.game.ws.services.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by sgourio on 12/10/15.
 */
public class MailServiceImpl implements MailService{
    private static Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

    public void send(String message, String sender){
        Properties props = new Properties();
        props.put("mail.smtp.host", "127.0.0.1");
        Session session = Session.getDefaultInstance(props, null);
        String msgBody = message;
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(sender));
            msg.setReplyTo(new Address[]{ new InternetAddress(sender)});
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress("sylvain.gourio@lettrecomptetriple.fr", "Sylvain Gourio"));
            msg.setSubject("Réponse Lettre Compte Triple");
            msg.setText("Message sur LCT v4 \r\n"+ msgBody +" \r\n " + sender);
            Transport.send(msg);
            logger.info("Le message de " + sender +" : " + msgBody );
        } catch (Exception e) {
            logger.error("Erreur a l'envoi de mail", e);
            logger.error("Le message de " + sender +" : " + msgBody );
        }
    }
}
