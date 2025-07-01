package com.memorytiles.memorytilesgame;

/**
 * The ScoreHelper class manages the score and player name for the Memory Tiles game.
 * It contains methods to increment, reset, and get the player's score and name.
 */
public class ScoreHelper {
    private int score;
    private String playerName;

    public ScoreHelper(String playerName) {
        this.playerName = playerName;
        this.score = 0;
    }

    /**
     * Increments the current score by one.
     */
    public void increment() {
        score++;
    }

    /**
     * Resets the score to zero.
     */
    public void reset() {
        score = 0;
    }

    /**
     * Returns the current score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Returns the player's name.
     */
    public String getPlayerName() {
        return playerName;
    }
}
