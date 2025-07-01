package com.memorytiles.memorytilesgame;

import javafx.scene.layout.GridPane;
import java.util.ArrayList;
import java.util.List;

/**
 * The TileManager class manages a grid of Tile objects
 * for the Memory Tiles game. It handles tile creation, enabling/disabling
 * clicks, resetting tile colors, and provides access to tiles.
 */
public class TileManager {
    private int rows;
    private int cols;
    private List<Tile> tiles = new ArrayList<>();
    private GameUI gameUI;

    public TileManager(int rows, int cols, GameUI gameUI, GridPane gridPane) {
        this.rows = rows;
        this.cols = cols;
        this.gameUI = gameUI;
        createTiles(gridPane);
    }

    /**
     * Creates Tile objects for the grid and adds them to the provided GridPane.
     *
     */
    private void createTiles(GridPane gridPane) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Tile tile = new Tile(r, c, gameUI);
                tiles.add(tile);
                gridPane.add(tile.getNode(), c, r);
            }
        }
    }

    /**
     * Resets the color of all tiles to their default state.
     */
    public void resetTiles() {
        for (Tile tile : tiles) {
            tile.resetColor();
        }
    }

    /**
     * Enables mouse click handling for all tiles.
     */
    public void enableClicks() {
        for (Tile tile : tiles) {
            tile.enableClick();
        }
    }

    /**
     * Disables mouse click handling for all tiles.
     */
    public void disableClicks() {
        for (Tile tile : tiles) {
            tile.disableClick();
        }
    }

    /**
     * Returns the list of all tiles.
     */
    public List<Tile> getTiles() {
        return tiles;
    }

    /**
     * Returns a randomly selected tile from the tiles.
     */
    public Tile getRandomTile() {
        int index = (int) (Math.random() * tiles.size());
        return tiles.get(index);
    }
}
