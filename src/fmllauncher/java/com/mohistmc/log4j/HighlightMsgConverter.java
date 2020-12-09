/*
 * TerminalConsoleAppender
 * Copyright (c) 2017 Minecrell <https://github.com/Minecrell>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.mohistmc.log4j;

import java.util.List;
import javax.annotation.Nullable;
import net.minecrell.terminalconsole.TerminalConsoleAppender;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternConverter;
import org.apache.logging.log4j.core.pattern.PatternFormatter;
import org.apache.logging.log4j.core.pattern.PatternParser;
import org.apache.logging.log4j.util.PerformanceSensitive;

@Plugin(name = "highlightMsg", category = PatternConverter.CATEGORY)
@ConverterKeys({"highlightMsg"})
@PerformanceSensitive("allocation")
public class HighlightMsgConverter extends LogEventPatternConverter {
    private static final String ANSI_RESET = "\u001B[39;0m";
    private static final String ANSI_ERROR = "\u001B[31;1m";
    private static final String ANSI_WARN = "\u001B[33;1m";
    private static final String ANSI_INFO = "\u001B[39;0m";
    private static final String ANSI_FATAL = "\u001B[31;1m";
    private static final String ANSI_TRACE = "\u001B[31;1m";

    private final List<PatternFormatter> formatters;

    /**
     * Construct the converter.
     *
     * @param formatters The pattern formatters to generate the text to highlight
     */
    protected HighlightMsgConverter(List<PatternFormatter> formatters) {
        super("highlightMsg", null);
        this.formatters = formatters;
    }

    /**
     * Gets a new instance of the {@link HighlightMsgConverter} with the
     * specified options.
     *
     * @param config  The current configuration
     * @param options The pattern options
     * @return The new instance
     */
    @Nullable
    public static HighlightMsgConverter newInstance(Configuration config, String[] options) {
        if (options.length != 1) {
            LOGGER.error("Incorrect number of options on highlightMsg. Expected 1 received " + options.length);
            return null;
        }
        if (options[0] == null) {
            LOGGER.error("No pattern supplied on highlightMsg");
            return null;
        }

        PatternParser parser = PatternLayout.createPatternParser(config);
        List<PatternFormatter> formatters = parser.parse(options[0]);
        return new HighlightMsgConverter(formatters);
    }

    @Override
    public void format(LogEvent event, StringBuilder toAppendTo) {
        if (TerminalConsoleAppender.isAnsiSupported()) {
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
