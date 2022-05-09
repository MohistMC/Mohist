/*
 * MohistMC
 * Copyright (C) 2019-2022.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

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
