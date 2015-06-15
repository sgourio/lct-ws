///*
//* Scrabble Helper Module 2015.
//* Written by Sylvain Gourio
//* sylvain.gourio@gmail.com
//*/
//
//package org.lct.game.ws.beans.view;
//
//
//import com.fasterxml.jackson.annotation.JsonProperty;
//import org.lct.game.ws.beans.view.CurrentTurn;
//import org.lct.gameboard.ws.beans.model.BoardGameTemplate;
//import org.lct.gameboard.ws.beans.model.Tile;
//import org.lct.gameboard.ws.beans.view.BoardGame;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
///**
//* Created by sgourio on 22/03/15.
//*/
//public final class CurrentGame {
//
//    private final String id;
//    private final String name;
//    private final String lang;
//    private final List<Tile> deck;
//    private final List<CurrentTurn> turnList;
//
//
//    public CurrentGame(@JsonProperty("id") String id, @JsonProperty("name") String name, @JsonProperty("lang") String lang, @JsonProperty("deck") List<Tile> deck, @JsonProperty("turnList") List<CurrentTurn> turnList) {
//        this.id = id;
//        this.name = name;
//        this.lang = lang;
//        this.deck = deck;
//        this.turnList = Collections.unmodifiableList(new ArrayList<CurrentTurn>(turnList));
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public String getLang() {
//        return lang;
//    }
//
//    public List<Tile> getDeck() {
//        return deck;
//    }
//
//    public List<CurrentTurn> getTurnList() {
//        return turnList;
//    }
//}
