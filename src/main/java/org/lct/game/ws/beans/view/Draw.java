///*
// * Scrabble Helper Module 2015.
// * Written by Sylvain Gourio
// * sylvain.gourio@gmail.com
// */
//
//package org.lct.game.ws.beans.model;
//
//import com.fasterxml.jackson.annotation.JsonProperty;
//import org.lct.gameboard.ws.beans.model.Tile;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
///**
// * Created by sgourio on 30/03/15.
// */
//public class Draw {
//    private final List<Tile> tileList;
//    private final String serialized;
//    private final List<Tile> previousTileList;
//    private final List<Tile> newTileList;
//
//    public Draw(@JsonProperty("previousTileList") List<Tile> previousTileList,
//                @JsonProperty("newTileList") List<Tile> newTileList) {
//        this.previousTileList = Collections.unmodifiableList(new ArrayList<Tile>(previousTileList));
//        this.newTileList = Collections.unmodifiableList(new ArrayList<Tile>(newTileList));
//        List<Tile> totalTileList = new ArrayList<Tile>(previousTileList);
//        totalTileList.addAll(newTileList);
//        this.tileList = Collections.unmodifiableList(new ArrayList<Tile>(totalTileList));
//
//        String val = "";
//        if( previousTileList.size() > 0 ) {
//            for (Tile tile : previousTileList) {
//                val += tile.getValue();
//            }
//            val += "+";
//        }
//        for( Tile tile : newTileList){
//            val += tile.getValue();
//        }
//        this.serialized = val;
//    }
//
//
//    public List<Tile> getTileList() {
//        return tileList;
//    }
//
//    public String getSerialized() {
//        return serialized;
//    }
//
//    public List<Tile> getPreviousTileList() {
//        return previousTileList;
//    }
//
//    public List<Tile> getNewTileList() {
//        return newTileList;
//    }
//}
