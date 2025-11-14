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
 * @version 1.0
 */
public class Score
{
    private int score;

    /**
     * Constructor for score
     */
    Score() throws IOException
    {
        final Path scorePath;
        scorePath = Paths.get("data", "highscore.txt");

        if (Files.notExists(scorePath))
        {
            Files.createFile(scorePath);
        }
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
        final Path scorePath;
        final String s;

        s = score + "";
        scorePath = Paths.get("data", "highscore.txt");

        //TODO Write score to file
//        Files.write(scorePath, s, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
}
