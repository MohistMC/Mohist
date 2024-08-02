package com.mohistmc.util;

public class StackTraceUtil {

    public static String getCallerMethodInfo(StackTraceElement[] elements) {
        if (elements.length > 2) {
            StackTraceElement caller = elements[2];
            return String.format("%s.%s(%s:%d)",
                    caller.getClassName(),
                    caller.getMethodName(),
                    caller.getFileName(),
                    caller.getLineNumber());
        } else {
            return "null";
        }
    }
}
