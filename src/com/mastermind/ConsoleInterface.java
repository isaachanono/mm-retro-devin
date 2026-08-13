package com.mastermind;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Console interface for the Mastermind game.
 * Handles user input and output, including input validation.
 */
public class ConsoleInterface {
    private final Scanner scanner;
    private final GameLogic gameLogic;
    private static final String RESET = "\u001B[0m";

    public ConsoleInterface(GameLogic gameLogic) {
        this.scanner = new Scanner(System.in);
        this.gameLogic = gameLogic;
    }

    /**
     * Display the welcome message and game instructions.
     */
    public void displayWelcome() {
        System.out.println("==============================================");
        System.out.println("           Welcome to Mastermind!");
        System.out.println("==============================================");
        System.out.println();
        System.out.println("Guess the secret code of " + gameLogic.getCodeLength() + " colors.");
        System.out.println("You have " + gameLogic.getMaxAttempts() + " attempts.");
        System.out.println();
        System.out.println("Available colors:");
        displayAvailableColors();
        System.out.println();
        System.out.println("Enter your guess as color names separated by spaces.");
        System.out.println("Example: RED BLUE GREEN PURPLE");
        System.out.println();
        System.out.println("After each guess, you'll see:");
        System.out.println("  - Exact matches: correct color in correct position");
        System.out.println("  - Color matches: correct color in wrong position");
        System.out.println();
        System.out.println("==============================================");
        System.out.println();
    }

    /**
     * Display the available colors with their ANSI codes.
     */
    private void displayAvailableColors() {
        List<Color> colors = gameLogic.getColorPalette().getColors();
        for (Color color : colors) {
            System.out.print(color.getAnsiCode() + color.getName() + RESET + "  ");
        }
        System.out.println();
    }

    /**
     * Get a guess from the user with validation.
     * @return The validated guess as a list of colors
     */
    public List<Color> getGuess() {
        while (true) {
            System.out.print("Enter your guess (" + gameLogic.getAttemptsRemaining() + " attempts remaining): ");
            String input = scanner.nextLine().trim();

            try {
                List<Color> guess = parseGuess(input);
                if (guess.size() != gameLogic.getCodeLength()) {
                    System.out.println("Error: Please enter exactly " + gameLogic.getCodeLength() + " colors.");
                    continue;
                }
                return guess;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Available colors: " + getAvailableColorNames());
            }
        }
    }

    /**
     * Parse a guess string into a list of colors.
     * @param input The user's input string
     * @return A list of colors
     * @throws IllegalArgumentException if the input contains invalid color names
     */
    private List<Color> parseGuess(String input) {
        if (input.isEmpty()) {
            throw new IllegalArgumentException("Guess cannot be empty.");
        }

        String[] parts = input.split("\\s+");
        List<Color> guess = new ArrayList<Color>();
        ColorPalette palette = gameLogic.getColorPalette();

        for (String part : parts) {
            Color color = palette.getColorByName(part);
            if (color == null) {
                throw new IllegalArgumentException("Invalid color: " + part);
            }
            guess.add(color);
        }

        return guess;
    }

    /**
     * Get a comma-separated string of available color names.
     * @return String of available color names
     */
    private String getAvailableColorNames() {
        List<Color> colors = gameLogic.getColorPalette().getColors();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < colors.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(colors.get(i).getName());
        }
        return sb.toString();
    }

    /**
     * Display the result of a guess.
     * @param guess The player's guess
     * @param result The scoring result
     */
    public void displayResult(List<Color> guess, GameLogic.ScoreResult result) {
        System.out.print("Your guess: ");
        for (Color color : guess) {
            System.out.print(color.getAnsiCode() + color.getName() + RESET + " ");
        }
        System.out.println();
        System.out.println("Result: " + result);
        System.out.println();
    }

    /**
     * Display a win message.
     * @param attemptsUsed Number of attempts used to win
     */
    public void displayWin(int attemptsUsed) {
        System.out.println("==============================================");
        System.out.println("           Congratulations! You won!");
        System.out.println("==============================================");
        System.out.println("You guessed the code in " + attemptsUsed + " attempts.");
        System.out.println();
    }

    /**
     * Display a loss message with the secret code.
     */
    public void displayLoss() {
        System.out.println("==============================================");
        System.out.println("           Game Over - You lost!");
        System.out.println("==============================================");
        System.out.print("The secret code was: ");
        for (Color color : gameLogic.getSecretCode()) {
            System.out.print(color.getAnsiCode() + color.getName() + RESET + " ");
        }
        System.out.println();
        System.out.println();
    }

    /**
     * Ask the player if they want to play again.
     * @return true if they want to play again, false otherwise
     */
    public boolean askPlayAgain() {
        while (true) {
            System.out.print("Play again? (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            } else {
                System.out.println("Please enter 'y' or 'n'.");
            }
        }
    }

    /**
     * Close the scanner.
     */
    public void close() {
        scanner.close();
    }
}
