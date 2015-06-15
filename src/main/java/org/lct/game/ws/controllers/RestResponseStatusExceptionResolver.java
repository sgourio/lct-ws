/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.controllers;

import org.lct.game.ws.services.exceptions.IncompleteGameException;
import org.lct.game.ws.services.exceptions.InvalidRoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RestResponseStatusExceptionResolver extends AbstractHandlerExceptionResolver {
 
    @Override
    protected ModelAndView doResolveException
      (HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            if (ex instanceof IncompleteGameException || ex instanceof InvalidRoundException) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.addHeader("reason", ex.getMessage());
                return new ModelAndView();
            }

        } catch (Exception handlerException) {
            logger.warn("Handling of [" + ex.getClass().getName() + "] resulted in Exception", handlerException);
        }
        return null;
    }
}