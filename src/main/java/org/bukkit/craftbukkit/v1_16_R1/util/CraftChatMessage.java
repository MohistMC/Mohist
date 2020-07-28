package org.bukkit.craftbukkit.v1_16_R1.util;

import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import org.bukkit.ChatColor;

public final class CraftChatMessage {
    private static final Pattern LINK_PATTERN = Pattern.compile("((?:(?:https?):\\/\\/)?(?:[-\\w_\\.]{2,}\\.[a-z]{2,4}.*?(?=[\\.\\?!,;:]?(?:[" + String.valueOf('§') + " \\n]|$))))");

    private static final Map<Character, Formatting> formatMap;

    static  {
        ImmutableMap.Builder<Character, Formatting> builder = ImmutableMap.builder();
        byte b;
        int i;
        Formatting[] arrayOfFormatting;
        for (i = (arrayOfFormatting = Formatting.values()).length, b = 0; b < i; ) {
            Formatting format = arrayOfFormatting[b];
            builder.put(Character.valueOf(Character.toLowerCase(format.toString().charAt(1))), format);
            b++;
        }
        formatMap = (Map<Character, Formatting>)builder.build();
    }

    public static Formatting getColor(ChatColor color) {
        return formatMap.get(Character.valueOf(color.getChar()));
    }

    public static ChatColor getColor(Formatting format) {
        return ChatColor.getByChar(format.code);
    }

    private static final class StringMessage {
        private static final Pattern INCREMENTAL_PATTERN = Pattern.compile("(" + String.valueOf('§') + "[0-9a-fk-orx])|((?:(?:https?):\\/\\/)?(?:[-\\w_\\.]{2,}\\.[a-z]{2,4}.*?(?=[\\.\\?!,;:]?(?:[" + String.valueOf('§') + " \\n]|$))))|(\\n)", 2);

        private static final Pattern INCREMENTAL_PATTERN_KEEP_NEWLINES = Pattern.compile("(" + String.valueOf('§') + "[0-9a-fk-orx])|((?:(?:https?):\\/\\/)?(?:[-\\w_\\.]{2,}\\.[a-z]{2,4}.*?(?=[\\.\\?!,;:]?(?:[" + String.valueOf('§') + " ]|$))))", 2);

        private final List<Text> list = new ArrayList<>();

        private MutableText currentChatComponent = (MutableText)new LiteralText("");

        private Style modifier = Style.EMPTY;

        private final Text[] output;

        private int currentIndex;

        private StringBuilder hex;

        private final String message;

        private StringMessage(String message, boolean keepNewlines) {
            this.message = message;
            if (message == null) {
                this.output = new Text[] { (Text)this.currentChatComponent };
                return;
            }
            this.list.add(this.currentChatComponent);
            Matcher matcher = (keepNewlines ? INCREMENTAL_PATTERN_KEEP_NEWLINES : INCREMENTAL_PATTERN).matcher(message);
            String match = null;
            boolean needsAdd = false;
            while (matcher.find()) {
                Formatting format;
                char c;
                int groupId = 0;
                do {

                } while ((match = matcher.group(++groupId)) == null);
                int index = matcher.start(groupId);
                if (index > this.currentIndex) {
                    needsAdd = false;
                    appendNewComponent(index);
                }
                switch (groupId) {
                    case 1:
                        c = match.toLowerCase(Locale.ENGLISH).charAt(1);
                        format = (Formatting)formatMap.get(Character.valueOf(c));
                        if (c == 'x') {
                            this.hex = new StringBuilder("#");
                        } else if (this.hex != null) {
                            this.hex.append(c);
                            if (this.hex.length() == 7) {
                                this.modifier = Style.EMPTY.withColor(TextColor.parse(this.hex.toString()));
                                this.hex = null;
                            }
                        } else if (format.isModifier() && format != Formatting.RESET) {
                            switch (format) {
                                case BOLD:
                                    this.modifier = this.modifier.withBold(Boolean.TRUE);
                                    break;
                                case ITALIC:
                                    this.modifier = this.modifier.withItalic(Boolean.TRUE);
                                    break;
                                default:
                                    throw new AssertionError("Unexpected message format");
                            }
                        } else {
                            this.modifier = Style.EMPTY.withColor(format);
                        }
                        needsAdd = true;
                        break;
                    case 2:
                        if (!match.startsWith("http://") && !match.startsWith("https://"))
                            match = "http://" + match;
                        this.modifier = this.modifier.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, match));
                        appendNewComponent(matcher.end(groupId));
                        this.modifier = this.modifier.withClickEvent(null);
                        break;
                    case 3:
                        if (needsAdd)
                            appendNewComponent(index);
                        this.currentChatComponent = null;
                        break;
                }
                this.currentIndex = matcher.end(groupId);
            }
            if (this.currentIndex < message.length() || needsAdd)
                appendNewComponent(message.length());
            this.output = this.list.toArray(new Text[this.list.size()]);
        }

        private void appendNewComponent(int index) {
            MutableText mutableText = (new LiteralText(this.message.substring(this.currentIndex, index))).setStyle(this.modifier);
            this.currentIndex = index;
            if (this.currentChatComponent == null) {
                this.currentChatComponent = (MutableText)new LiteralText("");
                this.list.add(this.currentChatComponent);
            }
            this.currentChatComponent.append((Text)mutableText);
        }

        private Text[] getOutput() { return this.output; }
    }

    public static Text[] fromString(String message) {
        return fromString(message, false);
    }

    public static Text[] fromString(String message, boolean keepNewlines) {
        return (new StringMessage(message, keepNewlines)).getOutput();
    }

    public static Text fromStringOrNull(String message, boolean keepNewlines) {
        return (message == null || message.isEmpty()) ? null : fromString(message, keepNewlines)[0];
    }

    public static String toJSON(Text component) { return Text.Serializer.toJson(component); }

    public static String fromComponent(Text component, Formatting black) {
        return fromComponent(component, Formatting.BLACK);
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

                Style modifier = text.getStyle();
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
                    Style linkModi = modifier.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, match));
                    linkModi.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, match));
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
