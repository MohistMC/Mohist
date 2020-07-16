package org.bukkit.craftbukkit.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.ClickEvent.Action;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.bukkit.ChatColor;

public final class CraftChatMessage {

    private static final Pattern LINK_PATTERN = Pattern.compile("((?:(?:https?):\\/\\/)?(?:[-\\w_\\.]{2,}\\.[a-z]{2,4}.*?(?=[\\.\\?!,;:]?(?:[" + String.valueOf(org.bukkit.ChatColor.COLOR_CHAR) + " \\n]|$))))");
    private static final Map<Character, Formatting> formatMap;

    static {
        Builder<Character, Formatting> builder = ImmutableMap.builder();
        for (Formatting format : Formatting.values()) {
            builder.put(Character.toLowerCase(format.toString().charAt(1)), format);
        }
        formatMap = builder.build();
    }

    public static Formatting getColor(ChatColor color) {
        return formatMap.get(color.getChar());
    }

    public static ChatColor getColor(Formatting format) {
        return ChatColor.getByChar(format.code);
    }

    private static final class StringMessage {
        private static final Pattern INCREMENTAL_PATTERN = Pattern.compile("(" + String.valueOf(org.bukkit.ChatColor.COLOR_CHAR) + "[0-9a-fk-or])|(\\n)|((?:(?:https?):\\/\\/)?(?:[-\\w_\\.]{2,}\\.[a-z]{2,4}.*?(?=[\\.\\?!,;:]?(?:[" + String.valueOf(org.bukkit.ChatColor.COLOR_CHAR) + " \\n]|$))))", Pattern.CASE_INSENSITIVE);

        private final List<Text> list = new ArrayList<Text>();
        private Text currentChatComponent = new LiteralText("");
        private Style modifier = new Style();
        private final Text[] output;
        private int currentIndex;
        private final String message;

        private StringMessage(String message, boolean keepNewlines) {
            this.message = message;
            if (message == null) {
                output = new Text[]{currentChatComponent};
                return;
            }
            list.add(currentChatComponent);

            Matcher matcher = INCREMENTAL_PATTERN.matcher(message);
            String match = null;
            while (matcher.find()) {
                int groupId = 0;
                while ((match = matcher.group(++groupId)) == null) {
                    // NOOP
                }
                appendNewComponent(matcher.start(groupId));
                switch (groupId) {
                case 1:
                    Formatting format = formatMap.get(match.toLowerCase(java.util.Locale.ENGLISH).charAt(1));
                    if (format == Formatting.RESET) {
                        modifier = new Style();
                    } else if (format.isModifier()) {
                        switch (format) {
                        case BOLD:
                            modifier.setBold(Boolean.TRUE);
                            break;
                        case ITALIC:
                            modifier.setItalic(Boolean.TRUE);
                            break;
                        case STRIKETHROUGH:
                            modifier.setStrikethrough(Boolean.TRUE);
                            break;
                        case UNDERLINE:
                            modifier.setUnderline(Boolean.TRUE);
                            break;
                        case OBFUSCATED:
                            modifier.setObfuscated(Boolean.TRUE);
                            break;
                        default:
                            throw new AssertionError("Unexpected message format");
                        }
                    } else { // Color resets formatting
                        modifier = new Style().setColor(format);
                    }
                    break;
                case 2:
                    if (keepNewlines) {
                        currentChatComponent.append(new LiteralText("\n"));
                    } else {
                        currentChatComponent = null;
                    }
                    break;
                case 3:
                    if (!(match.startsWith("http://") || match.startsWith("https://"))) {
                        match = "http://" + match;
                    }
                    modifier.setClickEvent(new ClickEvent(Action.OPEN_URL, match));
                    appendNewComponent(matcher.end(groupId));
                    modifier.setClickEvent((ClickEvent) null);
                }
                currentIndex = matcher.end(groupId);
            }

            if (currentIndex < message.length()) {
                appendNewComponent(message.length());
            }

            output = list.toArray(new Text[list.size()]);
        }

        private void appendNewComponent(int index) {
            if (index <= currentIndex) {
                return;
            }
            Text addition = new LiteralText(message.substring(currentIndex, index)).setStyle(modifier);
            currentIndex = index;
            modifier = modifier.deepCopy();
            if (currentChatComponent == null) {
                currentChatComponent = new LiteralText("");
                list.add(currentChatComponent);
            }
            currentChatComponent.append(addition);
        }

        private Text[] getOutput() {
            return output;
        }
    }

    public static Text wrapOrNull(String message) {
        return (message == null || message.isEmpty()) ? null : new LiteralText(message);
    }

    public static Text wrapOrEmpty(String message) {
        return (message == null) ? new LiteralText("") : new LiteralText(message);
    }

    public static Text fromStringOrNull(String message) {
        return fromStringOrNull(message, false);
    }

    public static Text fromStringOrNull(String message, boolean keepNewlines) {
        return (message == null || message.isEmpty()) ? null : fromString(message, keepNewlines)[0];
    }

    public static Text[] fromString(String message) {
        return fromString(message, false);
    }

    public static Text[] fromString(String message, boolean keepNewlines) {
        return new StringMessage(message, keepNewlines).getOutput();
    }

    public static String fromComponent(Text component) {
        return fromComponent(component, Formatting.BLACK);
    }

    public static String toJSON(Text component) {
        return Text.Serializer.toJson(component);
    }

    public static String fromComponent(Text component, Formatting defaultColor) {
        if (component == null) return "";
        StringBuilder out = new StringBuilder();

        for (Text c : (Iterable<Text>) component) {
            Style modi = c.getStyle();
            out.append(modi.getColor() == null ? defaultColor : modi.getColor());
            if (modi.isBold()) {
                out.append(Formatting.BOLD);
            }
            if (modi.isItalic()) {
                out.append(Formatting.ITALIC);
            }
            if (modi.isUnderlined()) {
                out.append(Formatting.UNDERLINE);
            }
            if (modi.isStrikethrough()) {
                out.append(Formatting.STRIKETHROUGH);
            }
            if (modi.isObfuscated()) {
                out.append(Formatting.OBFUSCATED);
            }
            out.append(c.asString());
        }
        return out.toString().replaceFirst("^(" + defaultColor + ")*", "");
    }

    public static Text fixComponent(Text component) {
        Matcher matcher = LINK_PATTERN.matcher("");
        return fixComponent(component, matcher);
    }

    private static Text fixComponent(Text component, Matcher matcher) {
        if (component instanceof LiteralText) {
            LiteralText text = ((LiteralText) component);
            String msg = text.asString();
            if (matcher.reset(msg).find()) {
                matcher.reset();

                Style modifier = text.getStyle() != null ? text.getStyle() : new Style();
                List<Text> extras = new ArrayList<Text>();
                List<Text> extrasOld = new ArrayList<Text>(text.getSiblings());
                component = text = new LiteralText("");

                int pos = 0;
                while (matcher.find()) {
                    String match = matcher.group();

                    if (!(match.startsWith("http://") || match.startsWith("https://"))) {
                        match = "http://" + match;
                    }

                    LiteralText prev = new LiteralText(msg.substring(pos, matcher.start()));
                    prev.setStyle(modifier);
                    extras.add(prev);

                    LiteralText link = new LiteralText(matcher.group());
                    Style linkModi = modifier.deepCopy();
                    linkModi.setClickEvent(new ClickEvent(Action.OPEN_URL, match));
                    link.setStyle(linkModi);
                    extras.add(link);

                    pos = matcher.end();
                }

                LiteralText prev = new LiteralText(msg.substring(pos));
                prev.setStyle(modifier);
                extras.add(prev);
                extras.addAll(extrasOld);

                for (Text c : extras) {
                    text.append(c);
                }
            }
        }

        List<Text> extras = component.getSiblings();
        for (int i = 0; i < extras.size(); i++) {
            Text comp = extras.get(i);
            if (comp.getStyle() != null && comp.getStyle().getClickEvent() == null) {
                extras.set(i, fixComponent(comp, matcher));
            }
        }

        if (component instanceof TranslatableText) {
            Object[] subs = ((TranslatableText) component).getArgs();
            for (int i = 0; i < subs.length; i++) {
                Object comp = subs[i];
                if (comp instanceof Text) {
                    Text c = (Text) comp;
                    if (c.getStyle() != null && c.getStyle().getClickEvent() == null) {
                        subs[i] = fixComponent(c, matcher);
                    }
                } else if (comp instanceof String && matcher.reset((String) comp).find()) {
                    subs[i] = fixComponent(new LiteralText((String) comp), matcher);
                }
            }
        }

        return component;
    }

    private CraftChatMessage() {
    }
}
