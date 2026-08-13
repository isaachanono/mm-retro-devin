package com.mastermind;

/**
 * Minimal, dependency-free assertion helpers used by the test suite.
 * Mirrors the subset of JUnit assertions used by the original test suite.
 */
public final class Assertions {

    private Assertions() {
    }

    /**
     * A runnable that is allowed to throw, for use with assertThrows.
     */
    public interface ThrowingRunnable {
        void run() throws Exception;
    }

    public static void assertEquals(Object expected, Object actual, String message) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError(message + " (expected: <" + expected + "> but was: <" + actual + ">)");
        }
    }

    public static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    public static void assertFalse(boolean condition, String message) {
        if (condition) {
            throw new AssertionError(message);
        }
    }

    public static void assertNull(Object value, String message) {
        if (value != null) {
            throw new AssertionError(message + " (was: <" + value + ">)");
        }
    }

    public static void assertNotNull(Object value, String message) {
        if (value == null) {
            throw new AssertionError(message);
        }
    }

    public static void assertThrows(Class<? extends Throwable> expectedType, ThrowingRunnable runnable, String message) {
        try {
            runnable.run();
        } catch (Throwable t) {
            if (!expectedType.isInstance(t)) {
                throw new AssertionError(message + " (expected " + expectedType.getName()
                        + " but got " + t.getClass().getName() + ")");
            }
            return;
        }
        throw new AssertionError(message + " (expected " + expectedType.getName() + " but nothing was thrown)");
    }
}
