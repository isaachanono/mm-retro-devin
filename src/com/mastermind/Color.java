package com.mastermind;

/**
 * Represents a color in the Mastermind game with a name and ANSI color code for console display.
 */
public class Color {
    private final String name;
    private final String ansiCode;

    public Color(String name, String ansiCode) {
        this.name = name;
        this.ansiCode = ansiCode;
    }

    public String getName() {
        return name;
    }

    public String getAnsiCode() {
        return ansiCode;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Color color = (Color) obj;
        return name.equals(color.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
