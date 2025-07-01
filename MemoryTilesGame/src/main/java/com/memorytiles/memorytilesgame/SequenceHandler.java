package com.memorytiles.memorytilesgame;

import javafx.animation.PauseTransition;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.*;

/**
 * The SequenceHandler class manages the sequence logic for the Memory Tiles game.
 * It generates tile sequences, assigns colors and notes, validates player input,
 * and starts the tile flashing and sound effects.
 */
public class SequenceHandler {
    private TileManager tileManager;
    private List<Tile> tileSequence = new ArrayList<>();
    private List<Color> colorSequence = new ArrayList<>();
    private List<String> noteSequence = new ArrayList<>();

    private int currentIndex = 0;
    private GameUI gameUI;
    private SoundPlayer soundPlayer;

    public SequenceHandler(TileManager tileManager, GameUI gameUI, SoundPlayer soundPlayer) {
        this.tileManager = tileManager;
        this.gameUI = gameUI;
        this.soundPlayer = soundPlayer;
    }

    /**
     * Adds a new tile, color, and sound to the current sequence.
     * Makes sure no immediate repetition of the last tile.
     */
    public void addToSequence() {
        Tile newTile;
        do {
            newTile = tileManager.getRandomTile();
        } while (!tileSequence.isEmpty() && newTile.equals(tileSequence.get(tileSequence.size() - 1)));

        Color color = getRandomColor();
        String note = soundPlayer.getRandomNote();

        tileSequence.add(newTile);
        colorSequence.add(color);
        noteSequence.add(note);

        currentIndex = 0;
    }

    /**
     * Plays the current tile sequence with tile flashing and sound.
     * Lets the UI know once the sequence has finished.
     */
    public void playSequence() {
        for (int i = 0; i < tileSequence.size(); i++) {
            final int index = i;
            PauseTransition delay = new PauseTransition(Duration.seconds(i * 1.0));
            delay.setOnFinished(e -> {
                Tile tile = tileSequence.get(index);
                Color color = colorSequence.get(index);
                String note = noteSequence.get(index);

                tile.flash(color);
                soundPlayer.playNote(note);

                PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
                pause.setOnFinished(e2 -> tile.unflash());
                pause.play();
            });
            delay.play();
        }

        PauseTransition finalPause = new PauseTransition(Duration.seconds(tileSequence.size()));
        finalPause.setOnFinished(e -> gameUI.onSequenceFinished());
        finalPause.play();
    }

    /**
     * Checks if the clicked tile matches the expected tile in the sequence.
     * Flashes tiles and updates sequence progress.
     *
     */
    public boolean verifyClick(Tile clickedTile) {
        if (clickedTile.equals(tileSequence.get(currentIndex))) {
            clickedTile.flash(colorSequence.get(currentIndex));
            PauseTransition unflash = new PauseTransition(Duration.seconds(0.3));
            unflash.setOnFinished(e -> clickedTile.unflash());
            unflash.play();

            currentIndex++;
            return true;
        }
        return false;
    }

    /**
     * Checks whether the player has successfully completed the current sequence.
     *
     */
    public boolean isSequenceComplete() {
        return currentIndex >= tileSequence.size();
    }

    /**
     * Resets the sequence, clearing all tiles, colors, and sound notes.
     * Also resets sequence index.
     */
    public void reset() {
        tileSequence.clear();
        colorSequence.clear();
        noteSequence.clear();
        currentIndex = 0;
    }

    /**
     * Gets the musical note associated with a given tile in the sequence.
     *
     */
    public String getNoteForTile(Tile tile) {
        int index = tileSequence.indexOf(tile);
        if (index >= 0 && index < noteSequence.size()) {
            return noteSequence.get(index);
        }
        return null;
    }

    /**
     * Selects and returns a random color from the list.
     * Assigns a visual flash color to tiles in the sequence.
     *
     */
    private Color getRandomColor() {
        List<Color> colors = Arrays.asList(
                Color.LIGHTGOLDENRODYELLOW, Color.PURPLE, Color.BLANCHEDALMOND,
                Color.LIGHTCYAN, Color.BLUEVIOLET, Color.MAGENTA, Color.LIGHTPINK,
                Color.AQUA, Color.BEIGE, Color.LAVENDER, Color.LIGHTBLUE,
                Color.LIGHTCORAL, Color.LIGHTGREEN, Color.LIGHTSKYBLUE
        );
        Random rand = new Random();
        return colors.get(rand.nextInt(colors.size()));
    }
}
