/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.filters;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.joda.time.DateTime;
import org.lct.game.ws.beans.view.Token;

import java.text.ParseException;

public final class AuthUtils {

    private static final JWSHeader JWT_HEADER = new JWSHeader(JWSAlgorithm.HS256);
	private static final String TOKEN_SECRET = "lefabuleuxdestindameliepoulain-audrey-tautou";
	public static final String AUTH_HEADER_KEY = "Authorization";
	
	public static String getSubject(String authHeader) throws ParseException, JOSEException {
		return decodeToken(authHeader).getSubject();
	}
	
	public static ReadOnlyJWTClaimsSet decodeToken(String authHeader) throws ParseException, JOSEException {
		SignedJWT signedJWT = SignedJWT.parse(getSerializedToken(authHeader));
		if (signedJWT.verify(new MACVerifier(TOKEN_SECRET))) {
			return signedJWT.getJWTClaimsSet();
		} else {
			throw new JOSEException("Signature verification failed");
		}
	}
	
	public static Token createToken(String host, String sub) throws JOSEException {
		JWTClaimsSet claim = new JWTClaimsSet();
		claim.setSubject(sub);
		claim.setIssuer(host);
		claim.setIssueTime(DateTime.now().toDate());
		claim.setExpirationTime(DateTime.now().plusDays(365).toDate());
		
		JWSSigner signer = new MACSigner(TOKEN_SECRET);
		SignedJWT jwt = new SignedJWT(JWT_HEADER, claim);
		jwt.sign(signer);
		
		return new Token(jwt.serialize());
	}
	
	public static String getSerializedToken(String authHeader) {
		return authHeader.split(" ")[1];
	}
}