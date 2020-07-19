package red.mohist.console.log4j;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.pattern.*;
import org.apache.logging.log4j.util.PerformanceSensitive;
import red.mohist.configuration.MohistConfig;
import red.mohist.util.ANSIColorUtils;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Mgazul
 * @date 2020/3/31 13:08
 */
@Plugin(name = "hTime", category = PatternConverter.CATEGORY)
@ConverterKeys({"hTime"})
@PerformanceSensitive("allocation")
public class HighlightTimeConverter extends LogEventPatternConverter {
    private static final String ANSI_RESET = "\u001B[39;0m";
    private static final String ANSI_ERROR = getError();
    private static final String ANSI_WARN = getWarn();
    private static final String ANSI_INFO = getInfo();
    private static final String ANSI_FATAL = getFatal();
    private static final String ANSI_TRACE = getTrace();

    private final List<PatternFormatter> formatters;

    /**
     * Construct the converter.
     *
     * @param formatters The pattern formatters to generate the text to highlight
     */
    protected HighlightTimeConverter(List<PatternFormatter> formatters) {
        super("hTime", null);
        this.formatters = formatters;
    }

    /**
     * Gets a new instance of the {@link HighlightTimeConverter} with the
     * specified options.
     *
     * @param config  The current configuration
     * @param options The pattern options
     * @return The new instance
     */
    @Nullable
    public static HighlightTimeConverter newInstance(Configuration config, String[] options) {
        if (options.length != 1) {
            LOGGER.error("Incorrect number of options on highlightTime. Expected 1 received " + options.length);
            return null;
        }
        if (options[0] == null) {
            LOGGER.error("No pattern supplied on highlightTime");
            return null;
        }

        PatternParser parser = PatternLayout.createPatternParser(config);
        List<PatternFormatter> formatters = parser.parse(options[0]);
        return new HighlightTimeConverter(formatters);
    }

    public static String getError() {
        String cc = MohistConfig.getHighlight("consolecolor.error-time", "b");
        return ANSIColorUtils.getColor(cc, "\u001B[36;1m");
    }

    public static String getWarn() {
        String cc = MohistConfig.getHighlight("consolecolor.warn-time:", "b");
        return ANSIColorUtils.getColor(cc, "\u001B[36;1m");
    }

    public static String getInfo() {
        String cc = MohistConfig.getHighlight("consolecolor.info-time:", "b");
        return ANSIColorUtils.getColor(cc, "\u001B[36;1m");
    }

    public static String getFatal() {
        String cc = MohistConfig.getHighlight("consolecolor.fatal-time:", "b");
        return ANSIColorUtils.getColor(cc, "\u001B[36;1m");
    }

    public static String getTrace() {
        String cc = MohistConfig.getHighlight("consolecolor.trace-time:", "b");
        return ANSIColorUtils.getColor(cc, "\u001B[36;1m");
    }

    @Override
    public void format(LogEvent event, StringBuilder toAppendTo) {
        Level level = event.getLevel();
        if (level.isMoreSpecificThan(Level.ERROR)) {
            format(ANSI_ERROR, event, toAppendTo);
            return;
        } else if (level.isMoreSpecificThan(Level.WARN)) {
            format(ANSI_WARN, event, toAppendTo);
            return;
        } else if (level.isMoreSpecificThan(Level.INFO)) {
            format(ANSI_INFO, event, toAppendTo);
            return;
        } else if (level.isMoreSpecificThan(Level.FATAL)) {
            format(ANSI_FATAL, event, toAppendTo);
            return;
        } else if (level.isMoreSpecificThan(Level.TRACE)) {
            format(ANSI_TRACE, event, toAppendTo);
            return;
        }

        //noinspection ForLoopReplaceableByForEach
        for (int i = 0, size = formatters.size(); i < size; i++) {
            formatters.get(i).format(event, toAppendTo);
        }
    }

    private void format(String style, LogEvent event, StringBuilder toAppendTo) {
        int start = toAppendTo.length();
        toAppendTo.append(style);
        int end = toAppendTo.length();

        //noinspection ForLoopReplaceableByForEach
        for (int i = 0, size = formatters.size(); i < size; i++) {
            formatters.get(i).format(event, toAppendTo);
        }

        if (toAppendTo.length() == end) {
            // No content so we don't need to append the ANSI escape code
            toAppendTo.setLength(start);
        } else {
            // Append reset code after the line
            toAppendTo.append(ANSI_RESET);
        }
    }

    @Override
    public boolean handlesThrowable() {
        for (final PatternFormatter formatter : formatters) {
            if (formatter.handlesThrowable()) {
                return true;
            }
        }
        return false;
    }
}
