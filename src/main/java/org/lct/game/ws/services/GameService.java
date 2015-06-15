/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.services;


import org.lct.game.ws.beans.model.Game;
import org.lct.game.ws.services.exceptions.IncompleteGameException;

/**
 * Created by sgourio on 22/03/15.
 */
public interface GameService {

    public void add(Game game) throws RuntimeException;
    public void save(Game game) throws RuntimeException;
//    /**
//     * Load BDD game to modelView CurrentGame
//     * @param game
//     * @return
//     */
//    public CurrentGame load(Game game);

//    /**
//     * Load a serializedWord
//     * @param boardGame
//     * @param draw
//     * @param serializedWord
//     * @param wordScore
//     * @return
//     */
//    public DroppedWord loadWord(BoardGame boardGame, Draw draw, String serializedWord, int wordScore);
//
//    public String toReference(boolean horizontal, int row, int column);
//    public int toColumn(String reference);
//    public int toRow(String reference);
//    public boolean isHorizontal(String reference);
//
//    public List<Tile> pioche(List<Tile> deck, int nb);
//    public boolean isTirageValide(List<Tile> tirage, int nbCoups, List<Tile> pioche);
//    public boolean isOnlyVoyelle(List<Tile> lettreList);
//    public boolean isOnlyConsonne(List<Tile> lettreList);
//
//    public DroppedWord selectBestWord(BoardGame boardGame, Set<DroppedWord> droppedWordSet);
}
