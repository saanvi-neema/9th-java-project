package com.memorytiles.memorytilesgame;

import javafx.application.Application;
import javafx.stage.Stage;
/*
 * IMPORTANT EXECUTION INFORMATION (PLEASE READ BEFORE RUNNING):
 * This code only works with Java version 18 because
 * we couldn't get JavaFX to work with later versions of Java.
 * This code uses maven to get JavaFX libraries.
 * This code has some set-up issues on VSCode, and works when run using IntelliJ IDE.
 */

/**
 * The FinalProject class serves as the entry point for the Memory Tiles game.
 * It extends javafx.application.Application and initializes the game's UI.
 */
public class FinalProject extends Application {

    /**
     * Starts the JavaFX application by initializing the game UI and asking the user
     * to choose a grid size.
     */
    @Override
    public void start(Stage stage) {
        GameUI gameUI = new GameUI();
        gameUI.askGridSize(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
