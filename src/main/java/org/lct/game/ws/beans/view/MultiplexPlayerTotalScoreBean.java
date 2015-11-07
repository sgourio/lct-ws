/*
 * LCT
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.beans.view;

/**
 * Created by sgourio on 03/11/15.
 */
public class MultiplexPlayerTotalScoreBean {
    private final String name;
    private final int total;

    public MultiplexPlayerTotalScoreBean(String name, int total) {
        this.name = name;
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public int getTotal() {
        return total;
    }
}
