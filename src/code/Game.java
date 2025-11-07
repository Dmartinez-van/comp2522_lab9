import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
 * @version 1.0
 */
class Game
{
    public Game()
    {

    }

    public static void start()
    {
        Path countriesPath;
        final java.util.List<String> countries;

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

        for (final String country : countries)
        {
            System.out.println(country);
        }
    }

}
