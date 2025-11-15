import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Keeps track of a score.
 * Can save value to text file.
 *
 * @author David Martinez
 * @version 1.0
 */
public class HighScoreService
{
    public static final String COUNTRY_MODE_PREFIX = "COUNTRY=";

    private int highScore;

    /**
     * Constructor for score
     */
    HighScoreService() throws IOException
    {
        final Path scorePath;
        scorePath = Paths.get("src", "data", "highscore.txt");

        try
        {
            if (Files.notExists(scorePath.getParent()))
            {
                Files.createDirectories(scorePath.getParent());
            }

            if (Files.notExists(scorePath))
            {
                Files.createFile(scorePath);
            }

            final String line = Files.readString(scorePath).trim();
            highScore = fetchHighScore(line);
        }
        catch (final IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private int fetchHighScore(final String line)
    {
        final String highScoreString;

        if (!line.startsWith(COUNTRY_MODE_PREFIX)) {
            return 0;
        }

        highScoreString = line.substring(COUNTRY_MODE_PREFIX.length()).trim();
        return Integer.parseInt(highScoreString);
    }

    /**
     * Getter for score
     * @return score
     */
    public int getHighScore()
    {
        return highScore;
    }

    /**
     * Setter for score
     * @param highScore the new score
     */
    public void setHighScore(final int highScore)
    {
        this.highScore = highScore;
    }

    /**
     * Saves current score to high score file
     * @throws IOException if file saving has an error
     */
    public void saveScore(int score) throws IOException
    {
        final Path scorePath;
        final String s;

        s = score + "";
        scorePath = Paths.get("data", "highscore.txt");

        //TODO Write score to file
//        Files.write(scorePath, s, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
}
