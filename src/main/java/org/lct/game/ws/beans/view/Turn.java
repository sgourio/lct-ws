///*
//* Scrabble Helper Module 2015.
//* Written by Sylvain Gourio
//* sylvain.gourio@gmail.com
//*/
//
//package org.lct.game.ws.beans.model;
//
//import com.fasterxml.jackson.annotation.JsonProperty;
//import org.lct.game.ws.beans.model.Draw;
//
///**
//* Created by sgourio on 21/03/15.
//*/
//public class Turn {
//    private final int turnNumber;
//    private final Draw drawing;
//    private final String result;
//    private final int turnScore;
//
//    public Turn(@JsonProperty("turnNumber") int turnNumber,
//                @JsonProperty("drawing") Draw drawing,
//                @JsonProperty("result") String result,
//                @JsonProperty("turnScore") int turnScore) {
//        this.turnNumber = turnNumber;
//        this.drawing = drawing;
//        this.result = result;
//        this.turnScore = turnScore;
//    }
//
//    public int getTurnNumber() {
//        return turnNumber;
//    }
//
//    public Draw getDrawing() {
//        return drawing;
//    }
//
//    public String getResult() {
//        return result;
//    }
//
//    public int getTurnScore() {
//        return turnScore;
//    }
//}
