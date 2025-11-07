import java.io.File;
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

    public Game()
    {
        final Path countriesPath;
        countriesPath = Paths.get("src", "data", "countries.txt");

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
        }
        catch (final IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void start()
    {
        final int countriesLength;
        final Random rand;
        final String randomCountry;
        final int randomCountryLength;
        final Scanner scanner;
        int guessCount = 1;

        countriesLength     = countries.size();
        rand                = new Random();
        randomCountry       = countries.get(rand.nextInt(countriesLength));
        randomCountryLength = randomCountry.length();
        scanner             = new Scanner(System.in);

        System.out.println("Secret word length: " + randomCountryLength);
        System.out.println("Current best: —");
        while (true)
        {
            final String guess;
            final StringBuilder response;
            final int guessLength;

            response = new StringBuilder();

            System.out.println("LUCKY VAULT — COUNTRY MODE. Type QUIT to exit.");
            guess       = scanner.nextLine().trim().toLowerCase();
            guessLength = guess.length();

            if (guess.equals("quit"))
            {
                response.append("Correct in ")
                    .append(guessCount)
                    .append(" attempts! Word was: ")
                    .append(randomCountry);

                System.out.println(response.toString());
                break;
            }
            else
            {
                if (guessLength == randomCountryLength)
                {
                    int correctLetters;
                    correctLetters = 0;

                    for (int i = 0; i < randomCountryLength; i++)
                    {
                        if (guess.charAt(i) == randomCountry.charAt(i))
                        {
                            correctLetters++;
                        }
                    }

                    response.append("Not it. ")
                        .append(correctLetters)
                        .append(" letter(s) correct (right position).");
                    System.out.println(response.toString());
                }
                else
                {
                    guessCount++;
                    response.append("Wrong length (")
                        .append(guessLength)
                        .append("). Need ")
                        .append(randomCountryLength)
                        .append(".");
                    System.out.println(response.toString());
                }
            }
        }
    }
}
