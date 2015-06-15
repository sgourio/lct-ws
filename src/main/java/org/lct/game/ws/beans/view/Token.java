/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.beans.view;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Token {
    String token;

	public Token(@JsonProperty("token") String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}
}