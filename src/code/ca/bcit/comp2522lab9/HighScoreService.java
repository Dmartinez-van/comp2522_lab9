package ca.bcit.comp2522lab9;

import java.io.BufferedReader;
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
    private static final String COUNTRY_MODE_PREFIX = "COUNTRY=";
    private static final Path   SCORE_FILE          = Paths.get("src", "data", "highscore.txt");
    private static final int    DEFAULT_SCORE       = 0;

    private int highScore;

    /**
     * Constructor for score
     */
    HighScoreService()
    {
        try
        {
            // Ensure directory exists
            if (Files.notExists(SCORE_FILE.getParent()))
            {
                Files.createDirectories(SCORE_FILE.getParent());
            }

            // Ensure file exists
            if (Files.notExists(SCORE_FILE))
            {
                Files.createFile(SCORE_FILE);
            }

            final String scoreLine;

            // Use try-with-resources to safely read the file
            try (final BufferedReader reader = Files.newBufferedReader(SCORE_FILE))
            {
                scoreLine = reader.readLine();
            }

            if (scoreLine == null || scoreLine.trim().isEmpty())
            {
                highScore = DEFAULT_SCORE;
            }
            else
            {
                highScore = parseHighScoreString(scoreLine.trim());
            }
        }
        catch (final IOException e)
        {
            throw new RuntimeException(e);
        }
    }


    /**
     * Parses a line from the score file to extract the score.
     *
     * @param line the line read from the score file
     * @return the extracted score, or DEFAULT_SCORE if parsing fails
     */
    private int parseHighScoreString(final String line)
    {
        final String highScoreString;
        final int highScoreValue;

        if (!line.startsWith(COUNTRY_MODE_PREFIX))
        {
            return DEFAULT_SCORE;
        }

        highScoreString = line.substring(COUNTRY_MODE_PREFIX.length()).trim();
        highScoreValue  = Integer.parseInt(highScoreString);

        return highScoreValue;
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
     * Setter for score.
     *
     * @param highScore the new score
     */
    public void writeHighScore(final int highScore)
    {
        try
        {
            final String line;
            line = COUNTRY_MODE_PREFIX + highScore;
            Files.writeString(SCORE_FILE,
                              line,
                              StandardOpenOption.CREATE,
                              StandardOpenOption.TRUNCATE_EXISTING);
        }
        catch (final IOException e)
        {
            throw new RuntimeException(e);
        }

        this.highScore = highScore;
    }
}
