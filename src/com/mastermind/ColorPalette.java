package com.mastermind;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the available colors for the Mastermind game.
 * Easily extensible by adding new colors to the color matrix.
 */
public class ColorPalette {
    private final List<Color> colors;

    public ColorPalette() {
        this.colors = new ArrayList<Color>();
        initializeDefaultColors();
    }

    /**
     * Initialize the default color palette.
     * Starts with 3 basic colors (red, blue, green) and their combinations.
     * This matrix can be easily extended with additional colors.
     */
    private void initializeDefaultColors() {
        // Basic colors
        addColor("RED", "\u001B[31m");
        addColor("BLUE", "\u001B[34m");
        addColor("GREEN", "\u001B[32m");

        // Pairings
        addColor("PURPLE", "\u001B[35m");    // Red + Blue
        addColor("YELLOW", "\u001B[33m");    // Red + Green
        addColor("CYAN", "\u001B[36m");      // Blue + Green

        // Triplet
        addColor("WHITE", "\u001B[37m");     // Red + Blue + Green
    }

    /**
     * Add a new color to the palette.
     * @param name The display name of the color
     * @param ansiCode The ANSI escape code for console coloring
     */
    public void addColor(String name, String ansiCode) {
        colors.add(new Color(name, ansiCode));
    }

    /**
     * Get all available colors.
     * @return List of all colors in the palette
     */
    public List<Color> getColors() {
        return new ArrayList<Color>(colors);
    }

    /**
     * Get a color by name (case-insensitive).
     * @param name The name of the color to find
     * @return The color if found, null otherwise
     */
    public Color getColorByName(String name) {
        for (Color color : colors) {
            if (color.getName().equalsIgnoreCase(name)) {
                return color;
            }
        }
        return null;
    }

    /**
     * Get the number of available colors.
     * @return The count of colors in the palette
     */
    public int size() {
        return colors.size();
    }

    /**
     * Get a random color from the palette.
     * @return A random color
     */
    public Color getRandomColor() {
        int index = (int) (Math.random() * colors.size());
        return colors.get(index);
    }
}
