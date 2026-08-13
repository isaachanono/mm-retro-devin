package com.mastermind;

import java.util.List;

/**
 * Main entry point for the Mastermind console game.
 * Orchestrates the game loop and coordinates between game logic and console interface.
 */
public class MastermindGame {
    private final GameLogic gameLogic;
    private final ConsoleInterface consoleInterface;

    public MastermindGame() {
        // Use default settings: 4 positions, 10 attempts, default color palette
        this.gameLogic = new GameLogic();
        this.consoleInterface = new ConsoleInterface(gameLogic);
    }

    public MastermindGame(int codeLength, int maxAttempts, ColorPalette colorPalette) {
        this.gameLogic = new GameLogic(codeLength, maxAttempts, colorPalette);
        this.consoleInterface = new ConsoleInterface(gameLogic);
    }

    /**
     * Run the main game loop.
     */
    public void run() {
        consoleInterface.displayWelcome();

        boolean playAgain = true;
        while (playAgain) {
            playGame();
            playAgain = consoleInterface.askPlayAgain();
        }

        consoleInterface.close();
        System.out.println("Thanks for playing Mastermind!");
    }

    /**
     * Play a single game session.
     */
    private void playGame() {
        gameLogic.reset();
        gameLogic.generateSecretCode();

        boolean gameWon = false;
        int attemptsUsed = 0;

        while (!gameLogic.isLoss() && !gameWon) {
            List<Color> guess = consoleInterface.getGuess();
            GameLogic.ScoreResult result = gameLogic.scoreGuess(guess);
            attemptsUsed++;

            consoleInterface.displayResult(guess, result);

            if (gameLogic.isWin(result)) {
                gameWon = true;
                consoleInterface.displayWin(attemptsUsed);
            }
        }

        if (!gameWon) {
            consoleInterface.displayLoss();
        }
    }

    /**
     * Main method - entry point for the application.
     */
    public static void main(String[] args) {
        MastermindGame game = new MastermindGame();
        game.run();
    }
}
