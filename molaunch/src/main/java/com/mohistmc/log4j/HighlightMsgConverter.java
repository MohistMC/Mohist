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
import net.minecrell.terminalconsole.TerminalConsoleAppender;
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
import org.apache.logging.log4j.util.PropertiesUtil;
import org.jetbrains.annotations.Nullable;

@Plugin(name = "highlightMsg", category = PatternConverter.CATEGORY)
@ConverterKeys({"highlightMsg"})
@PerformanceSensitive("allocation")
public class HighlightMsgConverter extends LogEventPatternConverter {
    public static final String KEEP_FORMATTING_PROPERTY = "terminal.keepMinecraftFormatting";
    private static final String ANSI_RESET = "\u001B[m";
    private static final boolean KEEP_FORMATTING = PropertiesUtil.getProperties().getBooleanProperty(KEEP_FORMATTING_PROPERTY);
    private static final char COLOR_CHAR = '\u00A7'; // §
    private static final String LOOKUP = "0123456789abcdefklmnor";
    private static final String[] ansiCodes = new String[]{
            "\u001B[0;30m", // Black §0
            "\u001B[0;34m", // Dark Blue §1
            "\u001B[0;32m", // Dark Green §2
            "\u001B[0;36m", // Dark Aqua §3
            "\u001B[0;31m", // Dark Red §4
            "\u001B[0;35m", // Dark Purple §5
            "\u001B[0;33m", // Gold §6
            "\u001B[0;37m", // Gray §7
            "\u001B[0;30;1m",  // Dark Gray §8
            "\u001B[0;34;1m",  // Blue §9
            "\u001B[0;32;1m",  // Green §a
            "\u001B[0;36;1m",  // Aqua §b
            "\u001B[0;31;1m",  // Red §c
            "\u001B[0;35;1m",  // Light Purple §d
            "\u001B[0;33;1m",  // Yellow §e
            "\u001B[0;37;1m",  // White §f
            "\u001B[5m",       // Obfuscated §k
            "\u001B[21m",      // Bold §l
            "\u001B[9m",       // Strikethrough §m
            "\u001B[4m",       // Underline §n
            "\u001B[3m",       // Italic §o
            ANSI_RESET,        // Reset §r
    };
    private final boolean ansi;
    private final List<PatternFormatter> formatters;

    /**
     * Construct the converter.
     *
     * @param formatters The pattern formatters to generate the text to highlight
     */
    protected HighlightMsgConverter(List<PatternFormatter> formatters, boolean strip) {
        super("highlightMsg", null);
        this.formatters = formatters;
        this.ansi = !strip;
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
        boolean strip = options.length > 1 && "strip".equals(options[1]);
        return new HighlightMsgConverter(formatters, strip);
    }

    private static void format(String s, StringBuilder result, int start, boolean ansi) {
        int next = s.indexOf(COLOR_CHAR);
        int last = s.length() - 1;
        if (next == -1 || next == last) {
            return;
        }

        result.setLength(start + next);

        int pos = next;
        do {
            int format = LOOKUP.indexOf(Character.toLowerCase(s.charAt(next + 1)));
            if (format != -1) {
                if (pos != next) {
                    result.append(s, pos, next);
                }
                if (ansi) {
                    result.append(ansiCodes[format]);
                }
                pos = next += 2;
            } else {
                next++;
            }

            next = s.indexOf(COLOR_CHAR, next);
        } while (next != -1 && next < last);

        result.append(s, pos, s.length());
        if (ansi) {
            result.append(ANSI_RESET);
        }
    }

    @Override
    public void format(LogEvent event, StringBuilder toAppendTo) {
        int start = toAppendTo.length();
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0, size = formatters.size(); i < size; i++) {
            formatters.get(i).format(event, toAppendTo);
        }

        if (KEEP_FORMATTING || toAppendTo.length() == start) {
            // Skip replacement if disabled or if the content is empty
            return;
        }

        String content = toAppendTo.substring(start);
        format(content, toAppendTo, start, ansi && TerminalConsoleAppender.isAnsiSupported());
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
