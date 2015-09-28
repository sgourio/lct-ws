/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.filters;

import org.lct.gameboard.ws.filters.SimpleCORSFilter;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;

public class WSSimpleCORSFilter extends SimpleCORSFilter {

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest httpRequest = (HttpServletRequest) req;

        String origin  = httpRequest.getHeader("origin");
        response.setHeader("Access-Control-Allow-Origin", HtmlUtils.htmlEscape(origin));
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        response.setHeader("Access-Control-Allow-Headers", "x-requested-with, origin, content-type, accept, Token, Authorization");
        if( httpRequest.getMethod().equals(HttpMethod.OPTIONS.name()) ) {
            response.setStatus(HttpServletResponse.SC_OK);
        }else {
            chain.doFilter(req, res);
        }
	}

	public void init(FilterConfig filterConfig) {}

	public void destroy() {}

}