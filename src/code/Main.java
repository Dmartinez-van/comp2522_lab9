import java.io.IOException;

/**
 * Runner class
 *
 * @author David Martinez
 * @author Daniel Do
 * @version 1.0
 */
public class Main
{
    /**
     * Runner method to create and start a game
     * @param args unused
     * @throws IOException if error occurs while writing score file
     */
    public static void main(final String[] args) throws IOException
    {
        final Game game;
        game = new Game();
        game.start();
    }
}
