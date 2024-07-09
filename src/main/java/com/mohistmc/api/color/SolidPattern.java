package com.mohistmc.api.color;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mgazul by MohistMC
 * @date 2023/8/2 23:27:40
 */
public class SolidPattern implements IPattern {
    Pattern pattern;

    public SolidPattern() {
        this.pattern = Pattern.compile("<SOLID:([0-9A-Fa-f]{6})>");
    }

    public String process(String string) {
        final Matcher matcher = this.pattern.matcher(string);
        while (matcher.find()) {
            final String color = matcher.group(1);
            string = string.replace(matcher.group(), ColorsAPI.getColor(color) + "");
        }
        return string;
    }
}