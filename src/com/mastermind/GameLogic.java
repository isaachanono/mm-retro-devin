package com.mastermind;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Core game logic for Mastermind including secret generation and guess scoring.
 * Separated from console I/O to enable independent testing.
 */
public class GameLogic {
    private final int codeLength;
    private final int maxAttempts;
    private final ColorPalette colorPalette;
    private List<Color> secretCode;
    private int attemptsRemaining;

    /**
     * Create a new game with default settings.
     */
    public GameLogic() {
        this(4, 10, new ColorPalette());
    }

    /**
     * Create a new game with custom settings.
     * @param codeLength Number of positions in the secret code
     * @param maxAttempts Maximum number of allowed guesses
     * @param colorPalette The color palette to use
     */
    public GameLogic(int codeLength, int maxAttempts, ColorPalette colorPalette) {
        this.codeLength = codeLength;
        this.maxAttempts = maxAttempts;
        this.colorPalette = colorPalette;
        this.attemptsRemaining = maxAttempts;
    }

    /**
     * Generate a new random secret code.
     * Colors may appear more than once.
     */
    public void generateSecretCode() {
        secretCode = new ArrayList<Color>();
        for (int i = 0; i < codeLength; i++) {
            secretCode.add(colorPalette.getRandomColor());
        }
    }

    /**
     * Get the current secret code.
     * @return The secret code as a list of colors, or null if not generated
     */
    public List<Color> getSecretCode() {
        if (secretCode == null) {
            return null;
        }
        return new ArrayList<Color>(secretCode);
    }

    /**
     * Set the secret code (useful for testing).
     * @param code The secret code to set
     */
    public void setSecretCode(List<Color> code) {
        if (code.size() != codeLength) {
            throw new IllegalArgumentException("Code length must be " + codeLength);
        }
        this.secretCode = new ArrayList<Color>(code);
    }

    /**
     * Get the number of attempts remaining.
     * @return Attempts remaining
     */
    public int getAttemptsRemaining() {
        return attemptsRemaining;
    }

    /**
     * Get the maximum number of attempts.
     * @return Maximum attempts
     */
    public int getMaxAttempts() {
        return maxAttempts;
    }

    /**
     * Get the code length.
     * @return Code length
     */
    public int getCodeLength() {
        return codeLength;
    }

    /**
     * Get the color palette.
     * @return The color palette
     */
    public ColorPalette getColorPalette() {
        return colorPalette;
    }

    /**
     * Score a guess against the secret code.
     * @param guess The player's guess
     * @return A ScoreResult containing exact matches and color-only matches
     */
    public ScoreResult scoreGuess(List<Color> guess) {
        if (guess.size() != codeLength) {
            throw new IllegalArgumentException("Guess must be exactly " + codeLength + " colors");
        }

        int exactMatches = 0;
        int colorMatches = 0;

        // First pass: count exact matches (correct color in correct position)
        boolean[] secretUsed = new boolean[codeLength];
        boolean[] guessUsed = new boolean[codeLength];

        for (int i = 0; i < codeLength; i++) {
            if (guess.get(i).equals(secretCode.get(i))) {
                exactMatches++;
                secretUsed[i] = true;
                guessUsed[i] = true;
            }
        }

        // Second pass: count color matches (correct color in wrong position)
        // Build frequency maps for unused colors
        Map<Color, Integer> secretColorCounts = new HashMap<Color, Integer>();
        Map<Color, Integer> guessColorCounts = new HashMap<Color, Integer>();

        for (int i = 0; i < codeLength; i++) {
            if (!secretUsed[i]) {
                Color color = secretCode.get(i);
                secretColorCounts.put(color, secretColorCounts.getOrDefault(color, 0) + 1);
            }
            if (!guessUsed[i]) {
                Color color = guess.get(i);
                guessColorCounts.put(color, guessColorCounts.getOrDefault(color, 0) + 1);
            }
        }

        // Count color matches by taking the minimum of each color's frequency
        for (Map.Entry<Color, Integer> entry : secretColorCounts.entrySet()) {
            Color color = entry.getKey();
            int secretCount = entry.getValue();
            int guessCount = guessColorCounts.getOrDefault(color, 0);
            colorMatches += Math.min(secretCount, guessCount);
        }

        // Decrement attempts
        attemptsRemaining--;

        return new ScoreResult(exactMatches, colorMatches);
    }

    /**
     * Check if the game is won (all positions matched).
     * @param result The score result from a guess
     * @return true if the guess was perfect
     */
    public boolean isWin(ScoreResult result) {
        return result.getExactMatches() == codeLength;
    }

    /**
     * Check if the game is lost (no attempts remaining).
     * @return true if no attempts remain
     */
    public boolean isLoss() {
        return attemptsRemaining <= 0;
    }

    /**
     * Reset the game for a new round.
     */
    public void reset() {
        attemptsRemaining = maxAttempts;
        secretCode = null;
    }

    /**
     * Represents the result of scoring a guess.
     */
    public static class ScoreResult {
        private final int exactMatches;
        private final int colorMatches;

        public ScoreResult(int exactMatches, int colorMatches) {
            this.exactMatches = exactMatches;
            this.colorMatches = colorMatches;
        }

        public int getExactMatches() {
            return exactMatches;
        }

        public int getColorMatches() {
            return colorMatches;
        }

        @Override
        public String toString() {
            return "Exact: " + exactMatches + ", Color only: " + colorMatches;
        }
    }
}
