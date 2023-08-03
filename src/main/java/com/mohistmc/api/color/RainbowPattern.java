package com.mohistmc.api.color;

import java.util.regex.Matcher;

/**
 * @author Mgazul by MohistMC
 * @date 2023/8/3 0:53:40
 */
public class RainbowPattern implements IPattern {
    java.util.regex.Pattern pattern;

    public RainbowPattern() {
        this.pattern = java.util.regex.Pattern.compile("<RAINBOW([0-9]{1,3})>(.*?)</RAINBOW>");
    }

    @Override
    public String process(String string) {
        final Matcher matcher = this.pattern.matcher(string);
        while (matcher.find()) {
            final String saturation = matcher.group(1);
            final String content = matcher.group(2);
            string = string.replace(matcher.group(), ColorsAPI.rainbow(content, Float.parseFloat(saturation)));
        }
        return string;
    }
}
