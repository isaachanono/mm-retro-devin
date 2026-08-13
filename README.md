# Mastermind (Java 8 / Constrained Environment Edition)

A lightweight console-based implementation of the classic Mastermind game in Java.

This is a port of the original `devin-mastermind` project, adapted to run in
constrained Java environments: **Java 8 only, no Maven, no Gradle, no
third-party dependencies, no package manager**. It builds directly with
`javac` and runs directly with `java`.

## Game Concept

The application generates a hidden sequence of colored marbles. The player
repeatedly guesses the sequence and receives feedback describing how accurate
the guess was.

A guess reports:
- **Exact matches**: How many marbles have the correct color in the correct position
- **Color matches**: How many additional marbles have the correct color but are in the wrong position

The player wins by guessing the entire sequence correctly. If they do not
succeed within the allowed number of attempts, the game ends and reveals the
secret sequence.

## Default Settings

- **Code length**: 4 marble positions
- **Available colors**: 7 colors (RED, BLUE, GREEN, PURPLE, YELLOW, CYAN, WHITE)
- **Max attempts**: 10 guesses
- **Color repetition**: Colors may appear more than once in the secret

## Requirements

- **Java 8 or newer** (`java` and `javac` on your `PATH`)
- No Maven, no Gradle, no npm, no other package manager, no external
  dependencies of any kind

Verify your environment before building:

```bat
java -version
javac -version
```

## Project Structure

```
devin-mastermind-retro/
├── build.bat
├── run.bat
├── test.bat
├── README.md
├── src/
│   └── com/
│       └── mastermind/
│           ├── Color.java
│           ├── ColorPalette.java
│           ├── GameLogic.java
│           ├── ConsoleInterface.java
│           └── MastermindGame.java
└── test/
    └── com/
        └── mastermind/
            ├── Assertions.java     (dependency-free assertion helpers)
            ├── TestRunner.java     (reflection-based test runner)
            └── GameLogicTest.java  (ported test suite)
```

## Building

```bat
build.bat
```

This compiles all sources under `src\` with `javac` into `build\classes`.

## Running the Game

```bat
run.bat
```

This builds the project (if needed) and launches the game with
`java -cp build\classes com.mastermind.MastermindGame`.

## Running Tests

```bat
test.bat
```

This compiles the sources and the test suite, then runs all tests via a
small reflection-based test runner (no JUnit or any other framework
required). It prints `PASS`/`FAIL` per test and exits with a non-zero code
if any test fails.

## How to Play

1. Start the application with `run.bat`
2. You'll see the available colors and basic instructions
3. Enter your guess as color names separated by spaces (e.g. `RED BLUE GREEN PURPLE`)
4. Receive feedback showing exact matches and color-only matches
5. See how many attempts remain
6. Continue until you guess correctly or exhaust your attempts
7. See the win/loss message and the secret code when the game ends

## Architecture

Same separation of concerns as the original project:

- **Color.java**: Represents a single color with name and ANSI code
- **ColorPalette.java**: Manages the available colors and provides random selection
- **GameLogic.java**: Core game logic including secret generation and scoring (testable independently)
- **ConsoleInterface.java**: Handles all user input/output and validation
- **MastermindGame.java**: Main entry point that orchestrates the game loop

## Differences from the Original Project

- Build/test tooling replaced: Maven + JUnit 5 → plain `javac`/`java` +
  a small custom reflection-based test runner (`test/com/mastermind/TestRunner.java`,
  `test/com/mastermind/Assertions.java`).
- Java language level lowered from 11 to 8: `List.of(...)` replaced with
  `Arrays.asList(...)`, and local-variable type inference (`var`) replaced
  with explicit types. No other behavior changes were required — the game
  rules, scoring algorithm, input validation, attempt handling, and
  win/loss/reveal behavior are identical to the original.
