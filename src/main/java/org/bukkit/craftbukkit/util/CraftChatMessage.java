package org.bukkit.craftbukkit.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.ITextComponent;
import org.bukkit.ChatColor;

public final class CraftChatMessage {

    private static final Pattern LINK_PATTERN = Pattern.compile("((?:(?:https?):\\/\\/)?(?:[-\\w_\\.]{2,}\\.[a-z]{2,4}.*?(?=[\\.\\?!,;:]?(?:[" + String.valueOf(org.bukkit.ChatColor.COLOR_CHAR) + " \\n]|$))))");
    private static final Map<Character, TextFormatting> formatMap;

    static {
        Builder<Character, TextFormatting> builder = ImmutableMap.builder();
        for (TextFormatting format : TextFormatting.values()) {
            builder.put(Character.toLowerCase(format.toString().charAt(1)), format);
        }
        formatMap = builder.build();
    }

    public static TextFormatting getColor(ChatColor color) {
        return formatMap.get(color.getChar());
    }

    public static ChatColor getColor(TextFormatting format) {
        return ChatColor.getByChar(format.formattingCode);
    }

    private static class StringMessage {
        private static final Pattern INCREMENTAL_PATTERN = Pattern.compile("(" + String.valueOf(org.bukkit.ChatColor.COLOR_CHAR) + "[0-9a-fk-or])|(\\n)|((?:(?:https?):\\/\\/)?(?:[-\\w_\\.]{2,}\\.[a-z]{2,4}.*?(?=[\\.\\?!,;:]?(?:[" + String.valueOf(org.bukkit.ChatColor.COLOR_CHAR) + " \\n]|$))))", Pattern.CASE_INSENSITIVE);

        private final List<ITextComponent> list = new ArrayList<ITextComponent>();
        private ITextComponent currentChatComponent = new StringTextComponent("");
        private Style modifier = new Style();
        private final ITextComponent[] output;
        private int currentIndex;
        private final String message;

        private StringMessage(String message,  boolean keepNewlines) {
            this.message = message;
            if (message == null) {
                output = new ITextComponent[] { currentChatComponent };
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
                    TextFormatting format = formatMap.get(match.toLowerCase(java.util.Locale.ENGLISH).charAt(1));
                    if (format == TextFormatting.RESET) {
                        modifier = new Style();
                    } else if (format.isFancyStyling()) {
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
                            modifier.setUnderlined(Boolean.TRUE);
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
                        currentChatComponent.appendSibling(new StringTextComponent("\n"));
                    } else {
                        currentChatComponent = null;
                    }
                    break;
                case 3:
                    if ( !( match.startsWith( "http://" ) || match.startsWith( "https://" ) ) ) {
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

            output = list.toArray(new ITextComponent[list.size()]);
        }

        private void appendNewComponent(int index) {
            if (index <= currentIndex) {
                return;
            }
            ITextComponent addition = new StringTextComponent(message.substring(currentIndex, index)).setStyle(modifier);
            currentIndex = index;
            modifier = modifier.createShallowCopy();
            if (currentChatComponent == null) {
                currentChatComponent = new StringTextComponent("");
                list.add(currentChatComponent);
            }
            currentChatComponent.appendSibling(addition);
        }

        private ITextComponent[] getOutput() {
            return output;
        }
    }

    public static ITextComponent wrapOrNull(String message) {
        return (message == null || message.isEmpty()) ? null : new StringTextComponent(message);
    }

    public static ITextComponent wrapOrEmpty(String message) {
        return (message == null) ? new StringTextComponent("") : new StringTextComponent(message);
    }

    public static ITextComponent fromStringOrNull(String message) {
        return fromStringOrNull(message, false);
    }

    public static ITextComponent fromStringOrNull(String message, boolean keepNewlines) {
        return (message == null || message.isEmpty()) ? null : fromString(message, keepNewlines)[0];
    }

    public static ITextComponent[] fromString(String message) {
        return fromString(message, false);
    }

    public static ITextComponent[] fromString(String message, boolean keepNewlines) {
        return new StringMessage(message, keepNewlines).getOutput();
    }

    public static String fromComponent(ITextComponent component) {
        return fromComponent(component, TextFormatting.BLACK);
    }

    public static String toJSON(ITextComponent component) {
        return ITextComponent.Serializer.toJson(component);
    }

    public static String fromComponent(ITextComponent component, TextFormatting defaultColor) {
        if (component == null) return "";
        StringBuilder out = new StringBuilder();

        for (ITextComponent c : (Iterable<ITextComponent>) component) {
            Style modi = c.getStyle();
            out.append(modi.getColor() == null ? defaultColor : modi.getColor());
            if (modi.getBold()) {
                out.append(TextFormatting.BOLD);
            }
            if (modi.getItalic()) {
                out.append(TextFormatting.ITALIC);
            }
            if (modi.getUnderlined()) {
                out.append(TextFormatting.UNDERLINE);
            }
            if (modi.getStrikethrough()) {
                out.append(TextFormatting.STRIKETHROUGH);
            }
            if (modi.getObfuscated()) {
                out.append(TextFormatting.OBFUSCATED);
            }
            out.append(c.getUnformattedComponentText());
        }
        return out.toString().replaceFirst("^(" + defaultColor + ")*", "");
    }

    public static ITextComponent fixComponent(ITextComponent component) {
        Matcher matcher = LINK_PATTERN.matcher("");
        return fixComponent(component, matcher);
    }

    private static ITextComponent fixComponent(ITextComponent component, Matcher matcher) {
        if (component instanceof StringTextComponent) {
            StringTextComponent text = ((StringTextComponent) component);
            String msg = text.getUnformattedComponentText();
            if (matcher.reset(msg).find()) {
                matcher.reset();

                Style modifier = text.getStyle() != null ?
                        text.getStyle() : new Style();
                List<ITextComponent> extras = new ArrayList<ITextComponent>();
                List<ITextComponent> extrasOld = new ArrayList<ITextComponent>(text.getSiblings());
                component = text = new StringTextComponent("");

                int pos = 0;
                while (matcher.find()) {
                    String match = matcher.group();

                    if ( !( match.startsWith( "http://" ) || match.startsWith( "https://" ) ) ) {
                        match = "http://" + match;
                    }

                    StringTextComponent prev = new StringTextComponent(msg.substring(pos, matcher.start()));
                    prev.setStyle(modifier);
                    extras.add(prev);

                    StringTextComponent link = new StringTextComponent(matcher.group());
                    Style linkModi = modifier.createShallowCopy();
                    linkModi.setClickEvent(new ClickEvent(Action.OPEN_URL, match));
                    link.setStyle(linkModi);
                    extras.add(link);

                    pos = matcher.end();
                }

                StringTextComponent prev = new StringTextComponent(msg.substring(pos));
                prev.setStyle(modifier);
                extras.add(prev);
                extras.addAll(extrasOld);

                for (ITextComponent c : extras) {
                    text.appendSibling(c);
                }
            }
        }

        List<ITextComponent> extras = component.getSiblings();
        for (int i = 0; i < extras.size(); i++) {
            ITextComponent comp = extras.get(i);
            if (comp.getStyle() != null && comp.getStyle().getClickEvent() == null) {
                extras.set(i, fixComponent(comp, matcher));
            }
        }

        if (component instanceof TranslationTextComponent) {
            Object[] subs = ((TranslationTextComponent) component).getFormatArgs();
            for (int i = 0; i < subs.length; i++) {
                Object comp = subs[i];
                if (comp instanceof ITextComponent) {
                    ITextComponent c = (ITextComponent) comp;
                    if (c.getStyle() != null && c.getStyle().getClickEvent() == null) {
                        subs[i] = fixComponent(c, matcher);
                    }
                } else if (comp instanceof String && matcher.reset((String)comp).find()) {
                    subs[i] = fixComponent(new StringTextComponent((String) comp), matcher);
                }
            }
        }

        return component;
    }

    private CraftChatMessage() {
    }
}
