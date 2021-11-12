package com.mohistmc.util;

import java.io.OutputStream;
import java.io.PrintStream;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LoggingPrintStream extends PrintStream {

    private final Logger logger;
    private final Level level;

    public LoggingPrintStream(String name, @NotNull OutputStream out, Level level) {
        super(out);
        this.logger = LogManager.getLogger(name);
        this.level = level;
    }

    @Override
    public void println(@Nullable String s) {
        logger.log(level, s);
    }

    @Override
    public void println(@Nullable Object o) {
        logger.log(level, String.valueOf(o));
    }
}
