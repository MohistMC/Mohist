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

import com.google.common.base.Functions;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.io.Writer;
import java.util.function.Function;
import javax.annotation.Nullable;
import jline.TerminalFactory;
import jline.console.ConsoleReader;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.util.Booleans;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.fusesource.jansi.AnsiConsole;

import static jline.TerminalFactory.OFF;
import static jline.console.ConsoleReader.RESET_LINE;

@Plugin(name = "TerminalConsole", category = "Core", elementType = "appender", printObject = true)
public class TerminalConsoleAppender extends AbstractAppender
{

    public static final String PROPERTY_PREFIX = "terminal";
    private static final boolean ENABLE_JLINE = PropertiesUtil.getProperties().getBooleanProperty("jline.enable", true);

    private static final PrintStream out = System.out;

    private static boolean initialized;
    private static ConsoleReader reader;

    public static ConsoleReader getReader()
    {
        return reader;
    }

    private static Function<String, String> formatter = Functions.identity();

    public static void setFormatter(Function<String, String> format)
    {
        formatter = format != null ? format : Functions.identity();
    }

    protected TerminalConsoleAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions)
    {
        super(name, filter, layout, ignoreExceptions);
    }

    @PluginFactory
    @Nullable
    public static TerminalConsoleAppender createAppender(@PluginAttribute("name") String name, @PluginElement("Filters") Filter filter, @PluginElement("Layout") Layout<? extends Serializable> layout, @PluginAttribute(value = "ignoreExceptions", defaultBoolean = true) String ignore)
    {
        if (name == null)
        {
            LOGGER.error("No name provided for TerminalConsoleAppender");
            return null;
        }
        if (layout == null)
        {
            layout = PatternLayout.newBuilder().build();
        }

        boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);

        // This is handled by jline
        System.setProperty("log4j.skipJansi", "true");
        return new TerminalConsoleAppender(name, filter, layout, ignoreExceptions);
    }

    @Override
    public void start()
    {
        // Initialize the reader if that hasn't happened yet
        if (!initialized && reader == null)
        {
            initialized = true;

            if (ENABLE_JLINE)
            {
                final boolean hasConsole = System.console() != null;
                if (hasConsole)
                {
                    try
                    {
                        AnsiConsole.systemInstall();
                        reader = new ConsoleReader();
                        reader.setExpandEvents(false);
                    }
                    catch (Exception e)
                    {
                        LOGGER.warn("Failed to initialize terminal. Falling back to default.", e);
                    }
                }

                if (reader == null)
                {
                    // Eclipse doesn't support colors and characters like \r so enabling jline2 on it will
                    // just cause a lot of issues with empty lines and weird characters.
                    // Enable jline2 only on IntelliJ IDEA to prevent that.
                    // Also see: https://bugs.eclipse.org/bugs/show_bug.cgi?id=76936

                    if (hasConsole || System.getProperty("java.class.path").contains("idea_rt.jar"))
                    {
                        // Disable advanced jline features
                        TerminalFactory.configure(OFF);
                        TerminalFactory.reset();

                        try
                        {
                            reader = new ConsoleReader();
                            reader.setExpandEvents(false);
                        }
                        catch (Exception e)
                        {
                            LOGGER.warn("Failed to initialize fallback terminal. Falling back to standard output console.", e);
                        }
                    }
                    else
                    {
                        LOGGER.warn("Disabling terminal, you're running in an unsupported environment.");
                    }
                }
            }
        }

        super.start();
    }

    @Override
    public void append(LogEvent event)
    {
        if (reader != null)
        {
            try
            {
                Writer out = reader.getOutput();
                out.write(RESET_LINE);
                out.write(formatEvent(event));

                reader.drawLine();
                reader.flush();
            }
            catch (IOException ignored)
            {
            }
        }
        else
        {
            out.print(formatEvent(event));
        }
    }

    protected String formatEvent(LogEvent event)
    {
        return formatter.apply(getLayout().toSerializable(event).toString());
    }

}