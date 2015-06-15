///*
//* Scrabble Helper Module 2015.
//* Written by Sylvain Gourio
//* sylvain.gourio@gmail.com
//*/
//
//package org.lct.game.ws.beans.view;
//
//
//import org.lct.game.ws.beans.model.Draw;
//import org.lct.gameboard.ws.beans.model.Tile;
//import org.lct.gameboard.ws.beans.view.BoardGame;
//import org.lct.gameboard.ws.beans.view.DroppedWord;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
///**
//* Created by sgourio on 22/03/15.
//*/
//public final class CurrentTurn {
//
//    private final int turnNumber;
//    private final List<Tile> draw;
//    private final List<Tile> deck;
//    private final DroppedWord bestWord;
//    private final int previousScore;
//    private final BoardGame boardGame;
//
//    public CurrentTurn(int turnNumber, List<Tile> draw, List<Tile> deck, DroppedWord bestWord, int previousScore, BoardGame boardGame) {
//        this.turnNumber = turnNumber;
//        this.draw = Collections.unmodifiableList(new ArrayList<Tile>(draw));
//        this.deck = Collections.unmodifiableList(new ArrayList<Tile>(deck));
//        this.bestWord = bestWord;
//        this.previousScore = previousScore;
//        this.boardGame = boardGame;
//    }
//
//    public int getTurnNumber() {
//        return turnNumber;
//    }
//
//    public List<Tile> getDraw() {
//        return draw;
//    }
//
//    public List<Tile> getDeck() {
//        return deck;
//    }
//
//    public DroppedWord getBestWord() {
//        return bestWord;
//    }
//
//    public int getPreviousScore() {
//        return previousScore;
//    }
//
//    public BoardGame getBoardGame() {
//        return boardGame;
//    }
//}
