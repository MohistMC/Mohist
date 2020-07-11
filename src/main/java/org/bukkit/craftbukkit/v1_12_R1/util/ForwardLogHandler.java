package org.bukkit.craftbukkit.v1_12_R1.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import red.mohist.Mohist;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class ForwardLogHandler extends ConsoleHandler {
    private final Map<String, Logger> cachedLoggers = new ConcurrentHashMap<>();

    private Logger getLogger(String name) {
        Logger logger = cachedLoggers.get(name);
        if (logger == null) {
            logger = LogManager.getLogger(name);
            cachedLoggers.put(name, logger);
        }

        return logger;
    }

    @Override
    public void publish(LogRecord record) {
        Logger logger = Mohist.LOGGER;
        Throwable exception = record.getThrown();
        Level level = record.getLevel();
        String message = getFormatter().formatMessage(record);

        if (level == Level.SEVERE) {
            logger.error(message, exception);
        } else if (level == Level.WARNING) {
            logger.warn(message, exception);
        } else if (level == Level.INFO) {
            logger.info(message, exception);
        } else if (level == Level.CONFIG) {
            logger.debug(message, exception);
        } else {
            logger.trace(message, exception);
        }
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }
}
