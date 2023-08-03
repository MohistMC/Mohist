package com.mohistmc.api.color;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mgazul by MohistMC
 * @date 2023/8/2 23:32:24
 */

public class GradientPattern implements IPattern {
    Pattern pattern;

    public GradientPattern() {
        this.pattern = Pattern.compile("<GRADIENT:([0-9A-Fa-f]{6})>(.*?)</GRADIENT:([0-9A-Fa-f]{6})>");
    }

    public String process(String string) {
        final Matcher matcher = this.pattern.matcher(string);
        while (matcher.find()) {
            final String start = matcher.group(1);
            final String end = matcher.group(3);
            final String content = matcher.group(2);
            string = string.replace(matcher.group(), ColorsAPI.color(content, new Color(Integer.parseInt(start, 16)), new Color(Integer.parseInt(end, 16))));
        }
        return string;
    }
}
