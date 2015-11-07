/*
 * LCT
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.services;

import org.lct.dictionary.beans.Dictionary;
import org.lct.game.ws.beans.view.Round;
import org.lct.game.ws.beans.view.WordResult;

/**
 * Created by sgourio on 03/11/15.
 */
public interface WordService {

    public WordResult putWord(Round round, String wordReference, Dictionary dictionary);
}
