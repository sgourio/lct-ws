///*
// * Scrabble Helper Module 2015.
// * Written by Sylvain Gourio
// * sylvain.gourio@gmail.com
// */
//
//package lct.ws.beans.model;
//
//import com.fasterxml.jackson.annotation.JsonProperty;
//import org.mongojack.ObjectId;
//
//import javax.persistence.Id;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
///**
// * Created by sgourio on 21/03/15.
// */
//public final class Game {
//
//    private final String id;
//    private final String name;
//    private final String lang;
//    private final BoardGameTemplate boardGame;
//    private final List<Tile> deck;
//    private final List<Turn> turnList;
//
//
//    public Game(@JsonProperty("_id") String id,
//                @JsonProperty("name") String name,
//                @JsonProperty("lang") String lang,
//                @JsonProperty("boardGame") BoardGameTemplate boardGame,
//                @JsonProperty("deck") List<Tile> deck,
//                @JsonProperty("turnList") List<Turn> turnList
//    ) {
//        this.id = id;
//        this.name = name;
//        this.lang = lang;
//        this.boardGame = boardGame;
//        this.deck = Collections.unmodifiableList(new ArrayList<>(deck));
//        this.turnList = Collections.unmodifiableList(new ArrayList<>(turnList));
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
//    public BoardGameTemplate getBoardGameTemplate() {
//        return boardGame;
//    }
//
//    public List<Tile> getDeck() {
//        return deck;
//    }
//
//    public List<Turn> getTurnList() {
//        return turnList;
//    }
//
//    @Id
//    @ObjectId
//    public String getId() {
//        return id;
//    }
//}
