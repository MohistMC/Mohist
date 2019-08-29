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

package red.mohist.console.log4j;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.annotation.Nullable;
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
import red.mohist.util.FileUtil;

@Plugin(name = "highlightMsg", category = PatternConverter.CATEGORY)
@ConverterKeys({ "highlightMsg" })
@PerformanceSensitive("allocation")
public class HighlightMsgConverter extends LogEventPatternConverter
{
    private static final String ANSI_RESET = "\u001B[39;0m";
    private static final String ANSI_ERROR = "\u001B" + geterror();
    private static final String ANSI_WARN = "\u001B" + getwarn();

    private final List<PatternFormatter> formatters;

    /**
     * Construct the converter.
     *
     * @param formatters The pattern formatters to generate the text to highlight
     */
    protected HighlightMsgConverter(List<PatternFormatter> formatters)
    {
        super("highlightMsg", null);
        this.formatters = formatters;
    }

    @Override
    public void format(LogEvent event, StringBuilder toAppendTo)
    {
        Level level = event.getLevel();
        if (level.isMoreSpecificThan(Level.ERROR))
        {
            format(ANSI_ERROR, event, toAppendTo);
            return;
        }
        else if (level.isMoreSpecificThan(Level.WARN))
        {
            format(ANSI_WARN, event, toAppendTo);
            return;
        }

        //noinspection ForLoopReplaceableByForEach
        for (int i = 0, size = formatters.size(); i < size; i++)
        {
            formatters.get(i).format(event, toAppendTo);
        }
    }

    private void format(String style, LogEvent event, StringBuilder toAppendTo)
    {
        int start = toAppendTo.length();
        toAppendTo.append(style);
        int end = toAppendTo.length();

        //noinspection ForLoopReplaceableByForEach
        for (int i = 0, size = formatters.size(); i < size; i++)
        {
            formatters.get(i).format(event, toAppendTo);
        }

        if (toAppendTo.length() == end)
        {
            // No content so we don't need to append the ANSI escape code
            toAppendTo.setLength(start);
        }
        else
        {
            // Append reset code after the line
            toAppendTo.append(ANSI_RESET);
        }
    }

    @Override
    public boolean handlesThrowable()
    {
        for (final PatternFormatter formatter : formatters)
        {
            if (formatter.handlesThrowable())
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets a new instance of the {@link HighlightLevelConverter} with the
     * specified options.
     *
     * @param config The current configuration
     * @param options The pattern options
     * @return The new instance
     */
    @Nullable
    public static HighlightLevelConverter newInstance(Configuration config, String[] options)
    {
        if (options.length != 1)
        {
            LOGGER.error("Incorrect number of options on highlightError. Expected 1 received " + options.length);
            return null;
        }
        if (options[0] == null)
        {
            LOGGER.error("No pattern supplied on highlightError");
            return null;
        }

        PatternParser parser = PatternLayout.createPatternParser(config);
        List<PatternFormatter> formatters = parser.parse(options[0]);
        return new HighlightLevelConverter(formatters);
    }

    public static String geterror() {
        try {
            File f = new File("mohist.yml");
            String s = FileUtil.readContent(f, "UTF-8");
            if(s.contains("error-msg: ")){
                String string = s.substring(s.indexOf("error-msg: "));
                String s1 = string.substring(string.indexOf(":") + 1).substring(2, 8);
                return s1.substring(0, 6);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "[31;1m";
    }

    public static String getwarn() {
        try {
            File f = new File("mohist.yml");
            String s = FileUtil.readContent(f, "UTF-8");
            if(s.contains("warn-msg: ")){
                String string = s.substring(s.indexOf("warn-msg: "));
                String s1 = string.substring(string.indexOf(":") + 1).substring(2, 8);
                return s1.substring(0, 6);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "[33;1m";
    }
}
