import java.util.Scanner;

/**
 * Runner class
 *
 * @author David Martinez
 * @version 1.0
 */
public class Main
{
    /**
     * Runner method
     * @param args unused
     */
    public static void main(final String[] args)
    {
        final Scanner scanner;
        scanner = new Scanner(System.in);

        final Game game;
        game = new Game();
        game.start();
    }
}
