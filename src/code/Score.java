import java.io.IOException;
import java.net.URI;
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
public class Score
{
    private int score;

    Score()
    {

    }

    /**
     * Getter for score
     * @return score
     */
    public int getScore()
    {
        return score;
    }

    /**
     * Setter for score
     * @param score the new score
     */
    public void setScore(final int score)
    {
        this.score = score;
    }

    /**
     * Saves current score to high score file
     * @throws IOException if file saving has an error
     */
    public void saveScore() throws IOException
    {
        final String highScorePath;
        final Path path;

        highScorePath = "../output/high-score.txt";
        path = Paths.get(URI.create(highScorePath));

        if (Files.notExists(path))
        {
            Files.createFile(path);
        }
    }
}
