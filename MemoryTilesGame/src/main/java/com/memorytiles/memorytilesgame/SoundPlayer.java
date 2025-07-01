package com.memorytiles.memorytilesgame;

import javafx.scene.media.AudioClip;
import java.util.HashMap;
import java.util.Map;

/**
 * The SoundPlayer class handles loading and playing sound effects and musical notes
 * for the Memory Tiles game. It supports playing sounds like wrong, letsgo, and goodbye,
 * as well as individual musical note sounds.
 */
public class SoundPlayer {
    private AudioClip wrongSound;
    private AudioClip letsgoSound;
    private AudioClip goodbyeSound;

    private Map<String, AudioClip> noteSounds;

    public SoundPlayer() {
        try {
            wrongSound = new AudioClip(getClass().getResource("/wrong.wav").toExternalForm());
            letsgoSound = new AudioClip(getClass().getResource("/letsgo.wav").toExternalForm());
            goodbyeSound = new AudioClip(getClass().getResource("/goodbye.wav").toExternalForm());

            noteSounds = new HashMap<>();
            for (char c = 'a'; c <= 'g'; c++) {
                String name = String.valueOf(c);
                noteSounds.put(name, new AudioClip(getClass().getResource("/" + name + ".wav").toExternalForm()));
            }
        } catch (Exception e) {
            System.out.println("Sound files not found or failed to load.");
        }
    }


    /**
     * Plays the sound indicating a wrong move or error.
     */
    public void playWrong() {
        if (wrongSound != null) {
            wrongSound.play();
        }
    }

    /**
     * Plays the sound indicating start of a fresh game.
     */
    public void playLetsGo() {
        if (letsgoSound != null) {
            letsgoSound.play();
        }
    }

    /**
     * Plays the sound indicating the end or goodbye.
     */
    public void playGoodbye() {
        if (goodbyeSound != null) {
            goodbyeSound.play();
        }
    }

    /**
     * Plays the sound associated with the given musical note.
     *
     */
    public void playNote(String note) {
        AudioClip clip = noteSounds.get(note);
        if (clip != null) {
            clip.play();
        }
    }

    /**
     * Returns a random musical note character between 'a' and 'g'.
     *
     */
    public String getRandomNote() {
        int index = (int)(Math.random() * 7); // 0 to 6
        return String.valueOf((char)('a' + index)); // 'a' to 'g'
    }
}
