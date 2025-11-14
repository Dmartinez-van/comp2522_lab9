import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * A Guessing Game class.
 * Game loops involves user guessing a randomly picked
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
    private final List<String> countries;
    private final Score        score;

    private boolean gameOn;
    private int guessCount;

    /**
     * Game class constructor.
     * Sets up object with path to country text file.
     */
    public Game() throws IOException
    {
        final Score score;
        final Path countriesPath;

        countriesPath = Paths.get("src", "data", "countries.txt");

        this.gameOn = true; // enables game loop
        this.score = new Score();

        try
        {
            if (Files.notExists(countriesPath.getParent()))
            {
                Files.createDirectories(countriesPath.getParent());
            }

            if (Files.notExists(countriesPath))
            {
                Files.createFile(countriesPath);
            }

            countries = Files.readAllLines(countriesPath);

            checkCountryListEmpty(countriesPath);
        }
        catch (final IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Validate that the countries list contains entries.
     * Throws an unchecked exception so callers don't run the game with invalid data.
     */
    private void checkCountryListEmpty(final Path countriesPath)
    {
        if (countries.isEmpty())
        {
            throw new IllegalStateException("There are no countries in the source data file: " +
                                            countriesPath.toString());
        }
    }

    /**
     * Gets a new random country.
     *
     * @return a Country name as String.
     */
    private String getRandomCountry()
    {
        final Random rand;

        rand = new Random();

        return countries.get(rand.nextInt(countries.size()));
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
        System.out.println("Secret word length: " + secretWord.length());
        System.out.println("Secret Word: " + secretWord); // For testing purposes
        System.out.println("Current best: —"); // TODO needs to read from highScore.txt if it exists.
    }

    private int checkCorrectLetterPositions(final String randomCountry,
                                            final int randomCountryLength,
                                            final String guess)
    {
        final int noMatches;
        int correctLetters;

        noMatches = 0;
        correctLetters = noMatches;

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
     * Main game loop logic.
     * Reads and stores the number of countries in data file.
     * Picks a random number which is used to select a random country.
     * Has scanner to read and use user input.
     * Keeps track of a guess count
     */
    public void start()
    {
        final String randomCountry;
        final int randomCountryLength;
        final Scanner scanner;

        scanner             = new Scanner(System.in); // defaults to UTF-8 charset
        randomCountry       = getRandomCountry();
        randomCountryLength = randomCountry.length();

        displayStartMessage(randomCountry);

        while (gameOn)
        {
            final String guess;
            final StringBuilder response;
            final int guessLength;

            response = new StringBuilder();

            System.out.println("LUCKY VAULT — COUNTRY MODE. Type QUIT to exit.");
            System.out.print("Your Guess: ");
            guess       = scanner.nextLine().trim().toLowerCase();
            guessLength = guess.length();
            System.out.println();

            // Empty or Invalid guess
            if (guess.isEmpty())
            {
                System.out.println("Empty Guess. Try again.");
            }

            // Quit
            if (guess.equalsIgnoreCase("quit"))
            {
                System.out.println("Bye!");
                turnOffGame();
            }

            // Valid guess made
            incrementGuessCount();

            // Correct guess
            if (guess.equalsIgnoreCase(randomCountry))
            {
                response.append("Correct in ")
                        .append(guessCount)
                        .append(" attempts! Word was: ")
                        .append(randomCountry);

                System.out.println(response);

                // TODO Need to check if new best was achieved and display msg if it is.
                turnOffGame();
            }
            else // Incorrect guess
            {
                // Correct guess length. Check and display any correct letter positions
                if (guessLength == randomCountryLength)
                {
                    int correctLetters;

                    correctLetters = checkCorrectLetterPositions(randomCountry,randomCountryLength, guess);

                    response.append("Not it. ")
                        .append(correctLetters)
                        .append(" letter(s) correct (right position).");
                    System.out.println(response);
                }
                else // Incorrect guess length
                {
                    response.append("Wrong length, try again. You guessed with (")
                        .append(guessLength)
                        .append(") word length. Answer has ")
                        .append(randomCountryLength)
                        .append(" length.");
                    System.out.println(response);
                }
            }
        }
    }
}
