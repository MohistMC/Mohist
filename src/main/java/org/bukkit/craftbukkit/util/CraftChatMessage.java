package org.bukkit.craftbukkit.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;
import org.bukkit.ChatColor;

public final class CraftChatMessage {

    private static final Pattern LINK_PATTERN = Pattern.compile("((?:(?:https?):\\/\\/)?(?:[-\\w_\\.]{2,}\\.[a-z]{2,4}.*?(?=[\\.\\?!,;:]?(?:[" + String.valueOf(ChatColor.COLOR_CHAR) + " \\n]|$))))");
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

    private static final class StringMessage {
        private static final Pattern INCREMENTAL_PATTERN = Pattern.compile("(" + String.valueOf(ChatColor.COLOR_CHAR) + "[0-9a-fk-orx])|((?:(?:https?):\\/\\/)?(?:[-\\w_\\.]{2,}\\.[a-z]{2,4}.*?(?=[\\.\\?!,;:]?(?:[" + String.valueOf(ChatColor.COLOR_CHAR) + " \\n]|$))))|(\\n)", Pattern.CASE_INSENSITIVE);
        // Separate pattern with no group 3, new lines are part of previous string
        private static final Pattern INCREMENTAL_PATTERN_KEEP_NEWLINES = Pattern.compile("(" + String.valueOf(ChatColor.COLOR_CHAR) + "[0-9a-fk-orx])|((?:(?:https?):\\/\\/)?(?:[-\\w_\\.]{2,}\\.[a-z]{2,4}.*?(?=[\\.\\?!,;:]?(?:[" + String.valueOf(ChatColor.COLOR_CHAR) + " ]|$))))", Pattern.CASE_INSENSITIVE);
        // ChatColor.b does not explicitly reset, its more of empty
        private static final Style RESET = Style.field_240709_b_.func_240713_a_(false).func_240722_b_(false).setUnderlined(false).setStrikethrough(false).setObfuscated(false);

        private final List<ITextComponent> list = new ArrayList<ITextComponent>();
        private IFormattableTextComponent currentChatComponent = new StringTextComponent("");
        private Style modifier = Style.field_240709_b_;
        private final ITextComponent[] output;
        private int currentIndex;
        private StringBuilder hex;
        private final String message;

        private StringMessage(String message, boolean keepNewlines) {
            this.message = message;
            if (message == null) {
                output = new ITextComponent[]{currentChatComponent};
                return;
            }
            list.add(currentChatComponent);

            Matcher matcher = (keepNewlines ? INCREMENTAL_PATTERN_KEEP_NEWLINES : INCREMENTAL_PATTERN).matcher(message);
            String match = null;
            boolean needsAdd = false;
            while (matcher.find()) {
                int groupId = 0;
                while ((match = matcher.group(++groupId)) == null) {
                    // NOOP
                }
                int index = matcher.start(groupId);
                if (index > currentIndex) {
                    needsAdd = false;
                    appendNewComponent(index);
                }
                switch (groupId) {
                case 1:
                    char c = match.toLowerCase(java.util.Locale.ENGLISH).charAt(1);
                    TextFormatting format = formatMap.get(c);

                    if (c == 'x') {
                        hex = new StringBuilder("#");
                    } else if (hex != null) {
                        hex.append(c);

                        if (hex.length() == 7) {
                            modifier = modifier.func_240718_a_(Color.func_240745_a_(hex.toString()));
                            hex = null;
                        }
                    } else if (format.isFancyStyling() && format != TextFormatting.RESET) {
                        switch (format) {
                        case BOLD:
                            modifier = modifier.func_240713_a_(Boolean.TRUE);
                            break;
                        case ITALIC:
                            modifier = modifier.func_240722_b_(Boolean.TRUE);
                            break;
                        case STRIKETHROUGH:
                            modifier = modifier.setStrikethrough(Boolean.TRUE);
                            break;
                        case UNDERLINE:
                            modifier = modifier.setUnderlined(Boolean.TRUE);
                            break;
                        case OBFUSCATED:
                            modifier = modifier.setObfuscated(Boolean.TRUE);
                            break;
                        default:
                            throw new AssertionError("Unexpected message format");
                        }
                    } else { // Color resets formatting
                        modifier = RESET.func_240712_a_(format);
                    }
                    needsAdd = true;
                    break;
                case 2:
                    if (!(match.startsWith("http://") || match.startsWith("https://"))) {
                        match = "http://" + match;
                    }
                    modifier = modifier.func_240715_a_(new ClickEvent(Action.OPEN_URL, match));
                    appendNewComponent(matcher.end(groupId));
                    modifier = modifier.func_240715_a_((ClickEvent) null);
                    break;
                case 3:
                    if (needsAdd) {
                        appendNewComponent(index);
                    }
                    currentChatComponent = null;
                    break;
                }
                currentIndex = matcher.end(groupId);
            }

            if (currentIndex < message.length() || needsAdd) {
                appendNewComponent(message.length());
            }

            output = list.toArray(new ITextComponent[list.size()]);
        }

        private void appendNewComponent(int index) {
            ITextComponent addition = new StringTextComponent(message.substring(currentIndex, index)).func_230530_a_(modifier);
            currentIndex = index;
            if (currentChatComponent == null) {
                currentChatComponent = new StringTextComponent("");
                list.add(currentChatComponent);
            }
            currentChatComponent.func_230529_a_(addition);
        }

        private ITextComponent[] getOutput() {
            return output;
        }
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

    public static String toJSON(ITextComponent component) {
        return ITextComponent.Serializer.toJson(component);
    }

    public static String fromComponent(ITextComponent component) {
        if (component == null) return "";
        StringBuilder out = new StringBuilder();

        boolean hadFormat = false;
        for (ITextComponent c : (Iterable<ITextComponent>) component) {
            Style modi = c.getStyle();
            Color color = modi.func_240711_a_();
            if (!c.getString().isEmpty() || color != null) {
                if (color != null) {
                    if (color.format != null) {
                        out.append(color.format);
                    } else {
                        out.append(ChatColor.COLOR_CHAR).append("x");
                        for (char magic : color.func_240747_b_().substring(1).toCharArray()) {
                            out.append(ChatColor.COLOR_CHAR).append(magic);
                        }
                    }
                    hadFormat = true;
                } else if (hadFormat) {
                    out.append(ChatColor.RESET);
                    hadFormat = false;
                }
            }
            if (modi.getBold()) {
                out.append(TextFormatting.BOLD);
                hadFormat = true;
            }
            if (modi.getItalic()) {
                out.append(TextFormatting.ITALIC);
                hadFormat = true;
            }
            if (modi.getUnderlined()) {
                out.append(TextFormatting.UNDERLINE);
                hadFormat = true;
            }
            if (modi.getStrikethrough()) {
                out.append(TextFormatting.STRIKETHROUGH);
                hadFormat = true;
            }
            if (modi.getObfuscated()) {
                out.append(TextFormatting.OBFUSCATED);
                hadFormat = true;
            }
            c.func_230533_b_((x) -> {
                out.append(x);
                return Optional.empty();
            });
        }
        return out.toString();
    }

    public static ITextComponent fixComponent(ITextComponent component) {
        Matcher matcher = LINK_PATTERN.matcher("");
        return fixComponent(component, matcher);
    }

    private static ITextComponent fixComponent(ITextComponent component, Matcher matcher) {
        if (component instanceof StringTextComponent) {
            StringTextComponent text = ((StringTextComponent) component);
            String msg = text.getText();
            if (matcher.reset(msg).find()) {
                matcher.reset();

                Style modifier = text.getStyle();
                List<ITextComponent> extras = new ArrayList<ITextComponent>();
                List<ITextComponent> extrasOld = new ArrayList<ITextComponent>(text.getSiblings());
                component = text = new StringTextComponent("");

                int pos = 0;
                while (matcher.find()) {
                    String match = matcher.group();

                    if (!(match.startsWith("http://") || match.startsWith("https://"))) {
                        match = "http://" + match;
                    }

                    StringTextComponent prev = new StringTextComponent(msg.substring(pos, matcher.start()));
                    prev.func_230530_a_(modifier);
                    extras.add(prev);

                    StringTextComponent link = new StringTextComponent(matcher.group());
                    Style linkModi = modifier.func_240715_a_(new ClickEvent(Action.OPEN_URL, match));
                    link.func_230530_a_(linkModi);
                    extras.add(link);

                    pos = matcher.end();
                }

                StringTextComponent prev = new StringTextComponent(msg.substring(pos));
                prev.func_230530_a_(modifier);
                extras.add(prev);
                extras.addAll(extrasOld);

                for (ITextComponent c : extras) {
                    text.func_230529_a_(c);
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
                } else if (comp instanceof String && matcher.reset((String) comp).find()) {
                    subs[i] = fixComponent(new StringTextComponent((String) comp), matcher);
                }
            }
        }

        return component;
    }

    private CraftChatMessage() {
    }
}
