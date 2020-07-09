package com.destroystokyo.paper.util;

import org.jetbrains.annotations.NotNull;

public class SneakyThrow {

    public static void sneaky(@NotNull Throwable exception) {
        SneakyThrow.<RuntimeException>throwSneaky(exception);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Throwable> void throwSneaky(@NotNull Throwable exception) throws T {
        throw (T) exception;
    }

}