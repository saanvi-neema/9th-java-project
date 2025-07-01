package com.memorytiles.memorytilesgame;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Optional;

/**
 * The GameUI class manages the UI for the Memory Tiles game.
 * It creates the setup screen, game board layout, user clicks, and game logic.
 * This class has objects of other classes like TileManager, SequenceHandler,
 * ScoreHelper, and SoundPlayer to make the game work.
 */
public class GameUI {
    private int rows = 3;
    private int columns = 3;
    private int lives = 3;
    private int initialLives = 3;

    private final VBox root = new VBox(10);
    private final GridPane grid = new GridPane();
    private final Label statusLabel = new Label("Welcome to Memory Tiles Game!");
    private final Label scoreLabel = new Label("Score: 0");
    private final Label livesLabel = new Label();  // Updated

    private TileManager tileManager;
    private SequenceHandler sequenceHandler;
    private ScoreHelper scoreHelper;
    private SoundPlayer soundPlayer;

    /**
     * Asks the user to input their name and grid size, then starts the game.
     *
     */
    public void askGridSize(Stage stage) {
        TextField nameInput = new TextField();
        TextField rowsInput = new TextField("3");
        TextField colsInput = new TextField("3");
        TextField livesInput = new TextField("3");
        Label messageLabel = new Label();
        Button startButton = new Button("Start Game");

        VBox inputPane = new VBox(10,
                new Label("Enter Your Name:"), nameInput,
                new Label("Enter Rows:"), rowsInput,
                new Label("Enter Columns:"), colsInput,
                new Label("Enter Lives (0 or more):"), livesInput,
                startButton, messageLabel);
        inputPane.setAlignment(Pos.CENTER);
        inputPane.setStyle("-fx-padding: 20px;");
        inputPane.setMinWidth(300);

        startButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        messageLabel.setStyle("-fx-text-fill: red;");

        Scene inputScene = new Scene(inputPane, 300, 330);
        stage.setScene(inputScene);
        stage.setTitle("Memory Tiles Setup");
        stage.show();

        startButton.setOnAction(e -> {
            try {
                rows = Integer.parseInt(rowsInput.getText());
                columns = Integer.parseInt(colsInput.getText());
                if (rows < 2 || columns < 2) {
                    messageLabel.setText("Minimum size is 2x2.");
                    return;
                }
                if (rows > 7 || columns > 7) {
                    messageLabel.setText("Maximum size is 7x7.");
                    return;
                }
            } catch (NumberFormatException ex) {
                rows = 3;
                columns = 3;
                messageLabel.setText("Invalid input. Using default 3x3.");
            }

            try {
                lives = Integer.parseInt(livesInput.getText());
                if (lives < 0) {
                    messageLabel.setText("Lives must be 0 or more.");
                    return;
                }
            } catch (NumberFormatException ex) {
                lives = 3;
                messageLabel.setText("Invalid input. Using default of 3 lives.");
            }

            initialLives = lives;
            String name = nameInput.getText().trim();
            if (name.isEmpty()) name = "Player";

            startGame(stage, name);
        });
    }

    /**
     * Initializes the main game UI with the given player name,
     * sets up the tile grid, UI components, and starts the first level.
     *
     */
    private void startGame(Stage stage, String playerName) {
        root.getChildren().clear();
        grid.getChildren().clear();

        root.setAlignment(Pos.CENTER);
        grid.setAlignment(Pos.CENTER);

        scoreHelper = new ScoreHelper(playerName);
        soundPlayer = new SoundPlayer();
        tileManager = new TileManager(rows, columns, this, grid);
        sequenceHandler = new SequenceHandler(tileManager, this, soundPlayer);

        livesLabel.setText("Lives: " + lives);  // Set correct lives label

        Label playerLabel = new Label("Player: " + playerName);
        playerLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        scoreLabel.setStyle("-fx-font-size: 14px;");
        livesLabel.setStyle("-fx-font-size: 14px;");
        statusLabel.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");

        root.getChildren().addAll(playerLabel, scoreLabel, livesLabel, statusLabel, grid);

        Button quitButton = new Button("Quit");
        quitButton.setOnAction(e -> Platform.exit());
        root.getChildren().add(quitButton);

        Scene gameScene = new Scene(root, columns * 100 + 100, rows * 100 + 200);
        stage.setScene(gameScene);
        stage.setTitle("Memory Tiles Game");
        stage.show();

        startLevel();
    }

    /**
     * Starts a new level by resetting all the tiles, generating a new sequence,
     * and resetting the display.
     */
    public void startLevel() {
        tileManager.resetTiles();
        sequenceHandler.addToSequence();
        statusLabel.setText("Watch the sequence!");
        tileManager.disableClicks();
        sequenceHandler.playSequence();
    }

    /**
     * Method called after the sequence finishes playing,
     * asking the user to repeat the sequence.
     */
    public void onSequenceFinished() {
        statusLabel.setText("Repeat the sequence!");
        tileManager.enableClicks();
    }

    /**
     * Handles the logic when a tile is clicked by the user.
     * Checks if the clicked tile is correct and updates the game score accordingly.
     *
     */
    public void onTileClicked(Tile tile) {
        boolean correct = sequenceHandler.verifyClick(tile);
        if (correct) {
            String note = sequenceHandler.getNoteForTile(tile);
            if (note != null) {
                soundPlayer.playNote(note);
            }

            scoreHelper.increment();
            scoreLabel.setText("Score: " + scoreHelper.getScore());

            if (sequenceHandler.isSequenceComplete()) {
                tileManager.disableClicks();
                statusLabel.setText("Correct! Moving to next level...");

                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                pause.setOnFinished(e -> startLevel());
                pause.play();
            }
        } else {
            soundPlayer.playWrong();
            tile.showError();
            tileManager.disableClicks();
            statusLabel.setText("Wrong tile!");

            if (lives > 0) {
                lives--;
            }

            livesLabel.setText("Lives: " + lives);
            if (lives <= 0) {
                statusLabel.setText("Game Over");
                PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
                pause.setOnFinished(e -> Platform.runLater(this::showGameOverDialog));
                pause.play();
            } else {
                statusLabel.setText("Wrong tile! Try again.");

                PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
                pause.setOnFinished(e -> tileManager.enableClicks());
                pause.play();
            }
        }
    }

    /**
     * Displays the Game Over dialog when the player clicks an incorrect tile.
     * Asks the user to play again or quit the game. Handles the action selected.
     */
    private void showGameOverDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Game Over");
        alert.setHeaderText("Incorrect Tile");
        alert.setContentText(scoreHelper.getPlayerName() + ", your score: " + scoreHelper.getScore());

        ButtonType playAgain = new ButtonType("Play Again");
        ButtonType quit = new ButtonType("Quit");

        alert.getButtonTypes().setAll(playAgain, quit);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == playAgain) {
            soundPlayer.playLetsGo();
            try {
                Thread.sleep(750);
            } catch (Exception e) {
            }

            scoreHelper.reset();
            scoreLabel.setText("Score: 0");
            sequenceHandler.reset();
            lives = initialLives;
            livesLabel.setText("Lives: " + lives);
            startLevel();
        } else {
            soundPlayer.playGoodbye();
            try {
                Thread.sleep(500);
            } catch (Exception e) {
            }
            Platform.exit();
        }
    }
}
