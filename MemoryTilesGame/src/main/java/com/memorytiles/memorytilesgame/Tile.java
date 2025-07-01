package com.memorytiles.memorytilesgame;

import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * The Tile class represents an individual tile in the Memory Tiles game grid.
 * Each tile has a fixed size, row and column position, and manages its appearance
 * and user click actions with the game UI.
 */
public class Tile {
    private static final int SIZE = 100;

    private int row;
    private int col;
    private Rectangle rect;
    private GameUI gameUI;

    public Tile(int row, int col, GameUI gameUI) {
        this.row = row;
        this.col = col;
        this.gameUI = gameUI;

        rect = new Rectangle(SIZE, SIZE);
        rect.setFill(Color.LIGHTGRAY);
        rect.setStroke(Color.BLACK);
    }

    /**
     * Returns the node representing the tile.
     *
     */
    public Rectangle getNode() {
        return rect;
    }

    /**
     * Changes the tile's color to the specified color, creating a flash effect.
     *
     */
    public void flash(Color color) {
        rect.setFill(color);
    }

    /**
     * Resets the tile's color back to the default light gray, removing any flash effect.
     */
    public void unflash() {
        rect.setFill(Color.LIGHTGRAY);
    }

    /**
     * Enables mouse click events on the tile, notifying the GameUI when clicked.
     * Only primary (left) mouse button clicks are handled.
     */
    public void enableClick() {
        rect.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                gameUI.onTileClicked(this);
            }
        });
    }

    /**
     * Disables mouse click events on the tile.
     */
    public void disableClick() {
        rect.setOnMouseClicked(null);
    }

    /**
     * Resets the tile's color to the default light gray.
     */
    public void resetColor() {
        rect.setFill(Color.LIGHTGRAY);
    }

    /**
     * Sets the tile's color to red to indicate an error.
     */
    public void showError() {
        rect.setFill(Color.RED);
    }

    /**
     * Checks if this tile is equal to another object.
     * Two tiles are equal if they have the same row and column.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tile) {
            Tile other = (Tile) obj;
            return this.row == other.row && this.col == other.col;
        }
        return false;
    }
}
