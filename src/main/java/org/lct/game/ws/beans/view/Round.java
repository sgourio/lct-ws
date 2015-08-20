package org.lct.game.ws.beans.view;

import org.lct.gameboard.ws.beans.model.Tile;
import org.lct.gameboard.ws.beans.view.BoardGame;
import org.lct.gameboard.ws.beans.view.DroppedWord;

import java.util.Date;
import java.util.List;

/**
 * Bean needed to display a round for playing
 * Created by sgourio on 14/08/15.
 */
public class Round {
    private final int roundNumber;
    private final BoardGame boardGame;
    private final List<Tile> draw;
    private final Date startDate;
    private final Date endDate;
    private final DroppedWord lastDroppedWord;

    public Round(int roundNumber, BoardGame boardGame, List<Tile> draw, Date startDate, Date endDate, DroppedWord lastDroppedWord) {
        this.roundNumber = roundNumber;
        this.boardGame = boardGame;
        this.draw = draw;
        this.startDate = (Date) startDate.clone();
        this.endDate = (Date) endDate.clone();
        this.lastDroppedWord = lastDroppedWord;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public BoardGame getBoardGame() {
        return boardGame;
    }

    public List<Tile> getDraw() {
        return draw;
    }

    public Date getStartDate() {
        return (Date) startDate.clone();
    }

    public Date getEndDate() {
        return (Date) endDate.clone();
    }

    public DroppedWord getLastDroppedWord() {
        return lastDroppedWord;
    }
}
