import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Keeps track of a score.
 * Can save value to text file.
 *
 * @author David Martinez
 * @author Daniel Do
 * @version 1.0
 */
public class HighScoreService
{
    public static final String COUNTRY_MODE_PREFIX = "COUNTRY=";
    public static final Path SCORE_FILE = Paths.get("src", "data", "highscore.txt");
    private static final int DEFAULT_SCORE = 0;

    private int highScore;

    /**
     * Constructor for score
     */
    HighScoreService() throws IOException
    {
        try
        {
            if (Files.notExists(SCORE_FILE.getParent()))
            {
                Files.createDirectories(SCORE_FILE.getParent());
            }

            if (Files.notExists(SCORE_FILE))
            {
                Files.createFile(SCORE_FILE);
            }

            final String line = Files.readString(SCORE_FILE).trim();
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

        if (!line.startsWith(COUNTRY_MODE_PREFIX))
        {
            return DEFAULT_SCORE;
        }

        highScoreString = line.substring(COUNTRY_MODE_PREFIX.length()).trim();
        return Integer.parseInt(highScoreString);
    }

    /**
     * Getter for score
     *
     * @return score
     */
    public int getHighScore()
    {
        return highScore;
    }

    /**
     * Setter for score
     *
     * @param highScore the new score
     */
    public void setHighScore(final int highScore)
    {
        if (this.highScore < highScore)
        {
            return;
        }

        try
        {
            if (Files.notExists(SCORE_FILE.getParent()))
            {
                Files.createDirectories(SCORE_FILE.getParent());
            }

            final String line;
            line = COUNTRY_MODE_PREFIX + highScore;
            Files.writeString(SCORE_FILE, line, StandardOpenOption.CREATE);
        }
        catch (final IOException e)
        {
            throw new RuntimeException(e);
        }

        this.highScore = highScore;
    }
}
