import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles writing log files for the Country Game.
 *
 * @author David Martinez
 * @author Daniel Do
 * @version 1.0
 */
public class LoggerService
{
    public static final Path LOG_DIRECTORY = Paths.get("src", "data", "logs");
    private static final DateTimeFormatter TIME_FORMATTER_MILLI = DateTimeFormatter.ofPattern("HH:mm:ss:SS");
    private static final DateTimeFormatter TIME_FORMATTER_SECOND = DateTimeFormatter.ofPattern("HH-mm-ss");

    final List<String> guessLogLines;

    LoggerService()
    {
        guessLogLines = new ArrayList<>();

        try
        {
            if (Files.notExists(LOG_DIRECTORY.getParent()))
            {
                Files.createDirectories(LOG_DIRECTORY.getParent());
            }

            if (Files.notExists(LOG_DIRECTORY))
            {
                Files.createDirectories(LOG_DIRECTORY);
            }
        }
        catch (final IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void addGuessLog(final String guess,
                            final String outcome)
    {
        final StringBuilder logBuilder;
        final LocalDate nowDate;
        final LocalTime nowTime;
        final String formattedTime;

        logBuilder    = new StringBuilder();
        nowDate       = LocalDate.now();
        nowTime       = LocalTime.now();
        formattedTime = nowTime.format(TIME_FORMATTER_MILLI);

        logBuilder.append(nowDate);
        logBuilder.append(" ");
        logBuilder.append(formattedTime);
        logBuilder.append(" | ");
        logBuilder.append(guess);
        logBuilder.append(" | ");
        logBuilder.append(outcome);

        guessLogLines.add(logBuilder.toString());
    }

    public void writeGuessLog()
    {
        if (guessLogLines.isEmpty())
        {
            return;
        }

        final LocalDate nowDate;
        final LocalTime nowTime;
        final String formattedTime;
        final StringBuilder logNameBuilder;
        final Path logFilePath;

        nowDate       = LocalDate.now();
        nowTime       = LocalTime.now();
        formattedTime = nowTime.format(TIME_FORMATTER_SECOND);

        logNameBuilder = new StringBuilder();
        logNameBuilder.append(nowDate);
        logNameBuilder.append("_");
        logNameBuilder.append(formattedTime);
        logNameBuilder.append("_COUNTRY.txt");
        logFilePath = LOG_DIRECTORY.resolve(logNameBuilder.toString());

        try
        {
            Files.write(logFilePath, guessLogLines);
        }
        catch (final IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
