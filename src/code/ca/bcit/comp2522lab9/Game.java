package ca.bcit.comp2522lab9;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * A Guessing ca.bcit.comp2522lab9.Game class.
 * ca.bcit.comp2522lab9.Game loops involves user guessing a randomly picked
 * country name (randomly picked from a text file).
 * If user guesses wrong, game will inform user the
 * length of their guess, and the length of the answer.
 * If user answers correctly, game displays a "Correct!" message
 * and increments their score. Their HIGH score is saved to a high-score file
 * each session.
 *
 * @author David Martinez
 * @author Daniel Do
 * @version 1.0
 */
class Game
{
    public static final int    DEFAULT_SCORE     = 0;
    public static final String DEFAULT_GAME_MODE = "COUNTRY";
    public static final Path   COUNTRY_FILE_PATH = Paths.get("src", "data", "countries.txt");

    private final List<String>     countryList;
    private final HighScoreService score;
    private final LoggerService    logger;
    private final String           gameMode;

    private int     guessCount;
    private boolean gameOn;

    /**
     * ca.bcit.comp2522lab9.Game class constructor.
     * Sets up object with path to country text file.
     */
    public Game()
    {
        this.gameMode   = DEFAULT_GAME_MODE;
        this.guessCount = DEFAULT_SCORE;
        this.gameOn     = true;
        this.score      = new HighScoreService();
        this.logger     = new LoggerService();

        countryList = loadCountries();
    }

    /**
     * Loads countries from the data file into a list.
     *
     * @return List of country names as Strings.
     */
    private List<String> loadCountries()
    {
        try
        {
            if (Files.notExists(COUNTRY_FILE_PATH.getParent()))
            {
                Files.createDirectories(COUNTRY_FILE_PATH.getParent());
            }

            if (Files.notExists(COUNTRY_FILE_PATH))
            {
                Files.createFile(COUNTRY_FILE_PATH);
            }

            final List<String> countryList;

            try (final BufferedReader reader = Files.newBufferedReader(COUNTRY_FILE_PATH))
            {
                countryList = reader.lines().toList();
            }

            if (countryList.isEmpty())
            {
                throw new IllegalStateException("There are no countries in the source data file: " +
                                                COUNTRY_FILE_PATH.toString());
            }

            return countryList;
        }
        catch (final IOException e)
        {
            throw new RuntimeException(e);
        }
    }


    /**
     * Gets a new random country.
     *
     * @return a Country name as String.
     */
    private String getRandomCountry()
    {
        final Random random;
        final String randomCountry;

        random        = new Random();
        randomCountry = countryList.get(random.nextInt(countryList.size()));

        return randomCountry;
    }

    /**
     * Stops game loop.
     */
    private void turnOffGame()
    {
        gameOn = false;
    }

    /**
     * Displays starting message before game loop.
     *
     * @param secretWord the word the user is trying to guess.
     */
    private void displayStartMessage(final String secretWord)
    {
        final StringBuilder startMessageBuilder;

        startMessageBuilder = new StringBuilder();
        startMessageBuilder.append("LUCKY VAULT \u2014 COUNTRY MODE. Type QUIT to exit.\n")
                           .append("Secret word length: ")
                           .append(secretWord.length())
                           .append("\nSecret word: ")
                           .append(secretWord)
                           .append(" (For testing purposes)") // For testing purposes
                           .append("\nCurrent best: ");

        if (score.getHighScore() == DEFAULT_SCORE)
        {
            startMessageBuilder.append("\u2014");
        }
        else
        {
            startMessageBuilder.append(score.getHighScore())
                               .append(" guesses");
        }

        System.out.println(startMessageBuilder);
    }

    /**
     * Checks how many letters in the guess are in the correct position.
     *
     * @param randomCountry       the country to guess
     * @param randomCountryLength the length of the country to guess
     * @param guess               the user's guess
     * @return number of letters in the correct position
     */
    private int checkCorrectLetterPositions(final String randomCountry,
                                            final int randomCountryLength,
                                            final String guess)
    {
        int correctLetters;

        correctLetters = DEFAULT_SCORE;

        for (int i = 0; i < randomCountryLength; i++)
        {
            if (guess.charAt(i) == randomCountry.toLowerCase().charAt(i))
            {
                correctLetters++;
            }
        }

        return correctLetters;
    }

    /**
     * ca.bcit.comp2522lab9.Main game loop logic.
     * Reads and stores the number of countries in data file.
     * Picks a random number which is used to select a random country.
     * Has scanner to read and use user input.
     */
    public void start()
    {
        final String randomCountry;
        final int randomCountryLength;

        randomCountry       = getRandomCountry();
        randomCountryLength = randomCountry.length();

        displayStartMessage(randomCountry);

        try (final Scanner scanner = new Scanner(System.in))
        {
            while (gameOn)
            {
                handleGuessTurn(scanner, randomCountry, randomCountryLength);
            }
        }
    }

    /**
     * Handles a single turn of guessing.
     *
     * @param scanner             the Scanner to read user input
     * @param randomCountry       the country to guess
     * @param randomCountryLength the length of the country to guess
     */
    private void handleGuessTurn(final Scanner scanner,
                                 final String randomCountry,
                                 final int randomCountryLength)
    {
        final String guess;

        System.out.print("Your Guess: ");
        guess = scanner.nextLine().trim().toLowerCase();
        System.out.println();

        if (guess.isEmpty())
        {
            System.out.println("Empty Guess. Try again.");
        }
        else if (guess.equals("quit"))
        {
            System.out.println("Bye!");
            turnOffGame();
        }
        else
        {
            processNonEmptyGuess(randomCountry, randomCountryLength, guess);
        }
    }

    /**
     * Processes a non-empty guess.
     *
     * @param randomCountry       the country to guess
     * @param randomCountryLength the length of the country to guess
     * @param guess               the user's guess
     */
    private void processNonEmptyGuess(final String randomCountry,
                                      final int randomCountryLength,
                                      final String guess)
    {
        guessCount++;

        if (guess.equalsIgnoreCase(randomCountry))
        {
            handleCorrectGuess(randomCountry, guess);
        }
        else
        {
            handleIncorrectGuess(randomCountry, randomCountryLength, guess);
        }
    }

    private void handleIncorrectGuess(final String randomCountry,
                                      final int randomCountryLength,
                                      final String guess)
    {
        final int guessLength;

        guessLength = guess.length();

        if (guessLength == randomCountryLength)
        {
            handleIncorrectGuessCorrectLength(randomCountry, randomCountryLength, guess);
        }
        else
        {
            handleIncorrectGuessIncorrectLength(randomCountryLength, guess, guessLength);
        }
    }

    /**
     * Response builder for incorrect guess with incorrect length.
     */
    private void handleIncorrectGuessIncorrectLength(final int randomCountryLength,
                                                     final String guess,
                                                     final int guessLength)
    {
        final StringBuilder response;
        response = new StringBuilder();

        logger.addGuessLog(guess, "wrong_length");
        response.append("Wrong length (")
                .append(guessLength)
                .append("). Need ")
                .append(randomCountryLength)
                .append(".");

        System.out.println(response);
    }

    /**
     * Response builder for incorrect guess with correct length.
     */
    private void handleIncorrectGuessCorrectLength(final String randomCountry,
                                                   final int randomCountryLength,
                                                   final String guess)
    {
        final StringBuilder response;
        int correctLetters;

        response       = new StringBuilder();
        correctLetters = checkCorrectLetterPositions(randomCountry, randomCountryLength, guess);

        logger.addGuessLog(guess, "matches=" + correctLetters);

        response.append("Not it. ")
                .append(correctLetters)
                .append(" letter(s) correct (right position).");

        System.out.println(response);
    }

    /**
     * Response builder for correct guess.
     */
    private void handleCorrectGuess(final String randomCountry,
                                    final String guess)
    {
        final StringBuilder response;
        response = new StringBuilder();

        response.append("correct in ")
                .append(guessCount);

        logger.addGuessLog(guess, response.toString());
        logger.writeGuessLog();

        response.append(" attempts! Word was: ")
                .append(randomCountry);

        System.out.println(response);

        if (score.getHighScore() > guessCount ||
            score.getHighScore() == DEFAULT_SCORE)
        {
            System.out.println("NEW BEST for " + gameMode + " mode!");
            score.writeHighScore(guessCount);
        }
        turnOffGame();
    }
}
