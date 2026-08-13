package com.mastermind;

import java.util.Arrays;
import java.util.List;

import static com.mastermind.Assertions.assertEquals;
import static com.mastermind.Assertions.assertFalse;
import static com.mastermind.Assertions.assertNotNull;
import static com.mastermind.Assertions.assertNull;
import static com.mastermind.Assertions.assertThrows;
import static com.mastermind.Assertions.assertTrue;

/**
 * Comprehensive tests for the Mastermind game logic, particularly the scoring algorithm.
 *
 * This is a dependency-free port of the original JUnit 5 test suite. Test methods are
 * discovered and run by {@link TestRunner} via reflection; "setUp" runs before each test.
 */
public class GameLogicTest {
    private GameLogic gameLogic;
    private ColorPalette colorPalette;
    private Color red, blue, green, yellow, purple;

    void setUp() {
        colorPalette = new ColorPalette();
        gameLogic = new GameLogic(4, 10, colorPalette);

        // Get some colors for testing
        red = colorPalette.getColorByName("RED");
        blue = colorPalette.getColorByName("BLUE");
        green = colorPalette.getColorByName("GREEN");
        yellow = colorPalette.getColorByName("YELLOW");
        purple = colorPalette.getColorByName("PURPLE");
    }

    public void testNoMatchingColors() {
        // Secret: RED BLUE GREEN YELLOW
        // Guess: PURPLE PURPLE PURPLE PURPLE
        List<Color> secret = Arrays.asList(red, blue, green, yellow);
        List<Color> guess = Arrays.asList(purple, purple, purple, purple);

        gameLogic.setSecretCode(secret);
        GameLogic.ScoreResult result = gameLogic.scoreGuess(guess);

        assertEquals(0, result.getExactMatches(), "Should have no exact matches");
        assertEquals(0, result.getColorMatches(), "Should have no color matches");
    }

    public void testCorrectColorInCorrectPosition() {
        // Secret: RED BLUE GREEN YELLOW
        // Guess: RED PURPLE PURPLE PURPLE
        List<Color> secret = Arrays.asList(red, blue, green, yellow);
        List<Color> guess = Arrays.asList(red, purple, purple, purple);

        gameLogic.setSecretCode(secret);
        GameLogic.ScoreResult result = gameLogic.scoreGuess(guess);

        assertEquals(1, result.getExactMatches(), "Should have 1 exact match");
        assertEquals(0, result.getColorMatches(), "Should have no color matches");
    }

    public void testCorrectColorInWrongPosition() {
        // Secret: RED BLUE GREEN YELLOW
        // Guess: PURPLE RED PURPLE PURPLE
        List<Color> secret = Arrays.asList(red, blue, green, yellow);
        List<Color> guess = Arrays.asList(purple, red, purple, purple);

        gameLogic.setSecretCode(secret);
        GameLogic.ScoreResult result = gameLogic.scoreGuess(guess);

        assertEquals(0, result.getExactMatches(), "Should have no exact matches");
        assertEquals(1, result.getColorMatches(), "Should have 1 color match");
    }

    public void testPerfectGuess() {
        // Secret: RED BLUE GREEN YELLOW
        // Guess: RED BLUE GREEN YELLOW
        List<Color> secret = Arrays.asList(red, blue, green, yellow);
        List<Color> guess = Arrays.asList(red, blue, green, yellow);

        gameLogic.setSecretCode(secret);
        GameLogic.ScoreResult result = gameLogic.scoreGuess(guess);

        assertEquals(4, result.getExactMatches(), "Should have 4 exact matches");
        assertEquals(0, result.getColorMatches(), "Should have no color matches");
        assertTrue(gameLogic.isWin(result), "Should be a winning guess");
    }

    public void testDuplicateColorsInSecret() {
        // Secret: RED RED BLUE GREEN
        // Guess: RED PURPLE PURPLE PURPLE
        List<Color> secret = Arrays.asList(red, red, blue, green);
        List<Color> guess = Arrays.asList(red, purple, purple, purple);

        gameLogic.setSecretCode(secret);
        GameLogic.ScoreResult result = gameLogic.scoreGuess(guess);

        assertEquals(1, result.getExactMatches(), "Should have 1 exact match");
        assertEquals(0, result.getColorMatches(), "Should have no color matches");
    }

    public void testDuplicateColorsInGuess() {
        // Secret: RED BLUE GREEN YELLOW
        // Guess: RED RED PURPLE PURPLE
        List<Color> secret = Arrays.asList(red, blue, green, yellow);
        List<Color> guess = Arrays.asList(red, red, purple, purple);

        gameLogic.setSecretCode(secret);
        GameLogic.ScoreResult result = gameLogic.scoreGuess(guess);

        assertEquals(1, result.getExactMatches(), "Should have 1 exact match");
        assertEquals(0, result.getColorMatches(), "Should have no color matches (extra RED doesn't count)");
    }

    public void testDuplicateColorsInBothSecretAndGuess() {
        // Secret: RED RED BLUE GREEN
        // Guess: RED RED PURPLE PURPLE
        List<Color> secret = Arrays.asList(red, red, blue, green);
        List<Color> guess = Arrays.asList(red, red, purple, purple);

        gameLogic.setSecretCode(secret);
        GameLogic.ScoreResult result = gameLogic.scoreGuess(guess);

        assertEquals(2, result.getExactMatches(), "Should have 2 exact matches");
        assertEquals(0, result.getColorMatches(), "Should have no color matches");
    }

    public void testDuplicateColorsNotDoubleCounted() {
        // Secret: RED RED BLUE GREEN
        // Guess: RED PURPLE PURPLE PURPLE
        // Only 1 RED in guess, but 2 REDs in secret
        // Should only count 1 match total
        List<Color> secret = Arrays.asList(red, red, blue, green);
        List<Color> guess = Arrays.asList(red, purple, purple, purple);

        gameLogic.setSecretCode(secret);
        GameLogic.ScoreResult result = gameLogic.scoreGuess(guess);

        assertEquals(1, result.getExactMatches(), "Should have 1 exact match");
        assertEquals(0, result.getColorMatches(), "Should have no color matches");
    }

    public void testDuplicateColorsInGuessNotDoubleCounted() {
        // Secret: RED BLUE GREEN YELLOW
        // Guess: RED RED PURPLE PURPLE
        // 2 REDs in guess, but only 1 RED in secret
        // Should only count 1 match total
        List<Color> secret = Arrays.asList(red, blue, green, yellow);
        List<Color> guess = Arrays.asList(red, red, purple, purple);

        gameLogic.setSecretCode(secret);
        GameLogic.ScoreResult result = gameLogic.scoreGuess(guess);

        assertEquals(1, result.getExactMatches(), "Should have 1 exact match");
        assertEquals(0, result.getColorMatches(), "Should have no color matches");
    }

    public void testComplexDuplicateScenario() {
        // Secret: RED RED BLUE BLUE
        // Guess: RED BLUE RED BLUE
        // All colors match, but positions are mixed
        List<Color> secret = Arrays.asList(red, red, blue, blue);
        List<Color> guess = Arrays.asList(red, blue, red, blue);

        gameLogic.setSecretCode(secret);
        GameLogic.ScoreResult result = gameLogic.scoreGuess(guess);

        assertEquals(2, result.getExactMatches(), "Should have 2 exact matches (positions 0 and 3)");
        assertEquals(2, result.getColorMatches(), "Should have 2 color matches (positions 1 and 2)");
    }

    public void testMixedExactAndColorMatches() {
        // Secret: RED BLUE GREEN YELLOW
        // Guess: RED YELLOW BLUE PURPLE
        // Position 0: exact match (RED)
        // Position 1: color match (BLUE appears in secret at position 1, but YELLOW is at position 1 in guess)
        // Position 2: color match (GREEN appears in secret at position 2, but BLUE is at position 2 in guess)
        // Position 3: color match (YELLOW appears in secret at position 3, but YELLOW is at position 1 in guess)
        List<Color> secret = Arrays.asList(red, blue, green, yellow);
        List<Color> guess = Arrays.asList(red, yellow, blue, purple);

        gameLogic.setSecretCode(secret);
        GameLogic.ScoreResult result = gameLogic.scoreGuess(guess);

        assertEquals(1, result.getExactMatches(), "Should have 1 exact match");
        assertEquals(2, result.getColorMatches(), "Should have 2 color matches");
    }

    public void testAllColorMatchesNoExactMatches() {
        // Secret: RED BLUE GREEN YELLOW
        // Guess: BLUE RED YELLOW GREEN
        // All colors present but all in wrong positions
        List<Color> secret = Arrays.asList(red, blue, green, yellow);
        List<Color> guess = Arrays.asList(blue, red, yellow, green);

        gameLogic.setSecretCode(secret);
        GameLogic.ScoreResult result = gameLogic.scoreGuess(guess);

        assertEquals(0, result.getExactMatches(), "Should have no exact matches");
        assertEquals(4, result.getColorMatches(), "Should have 4 color matches");
    }

    public void testAttemptsDecremented() {
        List<Color> secret = Arrays.asList(red, blue, green, yellow);
        List<Color> guess = Arrays.asList(purple, purple, purple, purple);

        gameLogic.setSecretCode(secret);
        int initialAttempts = gameLogic.getAttemptsRemaining();

        gameLogic.scoreGuess(guess);

        assertEquals(initialAttempts - 1, gameLogic.getAttemptsRemaining(),
                "Attempts should be decremented by 1");
    }

    public void testInvalidGuessLength() {
        List<Color> secret = Arrays.asList(red, blue, green, yellow);
        final List<Color> shortGuess = Arrays.asList(red, blue, green);

        gameLogic.setSecretCode(secret);

        assertThrows(IllegalArgumentException.class, new Assertions.ThrowingRunnable() {
            @Override
            public void run() {
                gameLogic.scoreGuess(shortGuess);
            }
        }, "Should throw IllegalArgumentException for wrong guess length");
    }

    public void testInvalidSecretCodeLength() {
        final List<Color> invalidSecret = Arrays.asList(red, blue, green);

        assertThrows(IllegalArgumentException.class, new Assertions.ThrowingRunnable() {
            @Override
            public void run() {
                gameLogic.setSecretCode(invalidSecret);
            }
        }, "Should throw IllegalArgumentException for wrong secret code length");
    }

    public void testGameLoss() {
        List<Color> secret = Arrays.asList(red, blue, green, yellow);
        List<Color> guess = Arrays.asList(purple, purple, purple, purple);

        gameLogic.setSecretCode(secret);

        // Use all attempts
        for (int i = 0; i < 10; i++) {
            gameLogic.scoreGuess(guess);
        }

        assertTrue(gameLogic.isLoss(), "Game should be lost after all attempts used");
    }

    public void testReset() {
        List<Color> secret = Arrays.asList(red, blue, green, yellow);
        List<Color> guess = Arrays.asList(purple, purple, purple, purple);

        gameLogic.setSecretCode(secret);
        gameLogic.scoreGuess(guess);

        gameLogic.reset();

        assertEquals(10, gameLogic.getAttemptsRemaining(), "Attempts should be reset to max");
        assertNull(gameLogic.getSecretCode(), "Secret code should be cleared");
    }

    public void testGenerateSecretCode() {
        gameLogic.generateSecretCode();
        List<Color> secret = gameLogic.getSecretCode();

        assertNotNull(secret, "Secret code should be generated");
        assertEquals(4, secret.size(), "Secret code should have 4 colors");
    }

    public void testColorPaletteIntegration() {
        ColorPalette palette = gameLogic.getColorPalette();
        assertNotNull(palette, "Color palette should be accessible");
        assertTrue(palette.size() > 0, "Color palette should have colors");
    }
}
