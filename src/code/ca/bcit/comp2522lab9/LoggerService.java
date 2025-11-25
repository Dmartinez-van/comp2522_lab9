package ca.bcit.comp2522lab9;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles writing log files for the Country ca.bcit.comp2522lab9.Game.
 *
 * @author David Martinez
 * @author Daniel Do
 * @version 1.0
 */
public class LoggerService
{
    private static final Path              LOG_DIRECTORY         = Paths.get("src", "data", "logs");
    private static final DateTimeFormatter TIME_FORMATTER_MILLI  = DateTimeFormatter.ofPattern("HH:mm:ss:SS");
    private static final DateTimeFormatter TIME_FORMATTER_SECOND = DateTimeFormatter.ofPattern("HH-mm-ss");

    final List<String> guessLogLines;

    /**
     * Constructor for ca.bcit.comp2522lab9.LoggerService
     */
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

    /**
     * Adds a guess and its outcome to the log.
     *
     * @param guess   The guessed country.
     * @param outcome The outcome of the guess.
     */
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

    /**
     * Writes the accumulated guess log to a file.
     * The file is named with the current date and time.
     */
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

        try (final BufferedWriter writer = Files.newBufferedWriter(logFilePath))
        {
            for (final String line : guessLogLines)
            {
                writer.write(line);
                writer.newLine();
            }
        }
        catch (final IOException e)
        {
            throw new RuntimeException(e);
        }

    }
}
