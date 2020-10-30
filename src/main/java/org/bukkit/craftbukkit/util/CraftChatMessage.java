package org.bukkit.craftbukkit.util;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

public final class CraftChatMessage {
    private static class StringMessage {
        private static final Map<Character, net.minecraft.util.EnumChatFormatting> formatMap;
        private static final Pattern INCREMENTAL_PATTERN = Pattern.compile("(" + String.valueOf(org.bukkit.ChatColor.COLOR_CHAR) + "[0-9a-fk-or])|(\\n)|((?:[a-z0-9]{2,}:\\/\\/)?(?:(?:[0-9]{1,3}\\.){3}[0-9]{1,3}|(?:[-\\w_\\.]{1,}\\.[a-z]{2,}?))(?::[0-9]{1,5})?.*?(?=[!\"\u00A7 \n]|$))", Pattern.CASE_INSENSITIVE);

        static {
            Builder<Character, net.minecraft.util.EnumChatFormatting> builder = ImmutableMap.builder();
            for (net.minecraft.util.EnumChatFormatting format : net.minecraft.util.EnumChatFormatting.values()) {
                builder.put(Character.toLowerCase(format.getFormattingCode()), format);
            }
            formatMap = builder.build();
        }

        private final List<net.minecraft.util.IChatComponent> list = new ArrayList<net.minecraft.util.IChatComponent>();
        private net.minecraft.util.IChatComponent currentChatComponent = new net.minecraft.util.ChatComponentTranslation(""); // Cauldron - ChatComponentText -> ChatComponentTranslation. fixes forge compatibility
        private net.minecraft.util.ChatStyle modifier = new net.minecraft.util.ChatStyle();
        private final net.minecraft.util.IChatComponent[] output;
        private int currentIndex;
        private final String message;

        private StringMessage(String message) {
            this.message = message;
            if (message == null) {
                output = new net.minecraft.util.IChatComponent[] { currentChatComponent };
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
                    net.minecraft.util.EnumChatFormatting format = formatMap.get(match.toLowerCase().charAt(1));
                    if (format == net.minecraft.util.EnumChatFormatting.RESET) {
                        modifier = new net.minecraft.util.ChatStyle();
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
                        modifier = new net.minecraft.util.ChatStyle().setColor(format);
                    }
                    break;
                case 2:
                    currentChatComponent = null;
                    break;
                case 3:
                    if (match.indexOf("://") < 0) {
                        match = "http://" + match;
                    }
                    modifier.setChatClickEvent(new net.minecraft.event.ClickEvent(net.minecraft.event.ClickEvent.Action.OPEN_URL, match)); // Should be setChatClickable
                    appendNewComponent(matcher.end(groupId));
                    modifier.setChatClickEvent((net.minecraft.event.ClickEvent) null);
                }
                currentIndex = matcher.end(groupId);
            }

            if (currentIndex < message.length()) {
                appendNewComponent(message.length());
            }

            output = list.toArray(new net.minecraft.util.IChatComponent[0]);
        }

        private void appendNewComponent(int index) {
            if (index <= currentIndex) {
                return;
            }
            net.minecraft.util.IChatComponent addition = new net.minecraft.util.ChatComponentText(message.substring(currentIndex, index)).setChatStyle(modifier);
            currentIndex = index;
            modifier = modifier.createShallowCopy();
            if (currentChatComponent == null) {
                currentChatComponent = new net.minecraft.util.ChatComponentText("");
                list.add(currentChatComponent);
            }
            currentChatComponent.appendSibling(addition);
        }

        private net.minecraft.util.IChatComponent[] getOutput() {
            return output;
        }
    }

    public static net.minecraft.util.IChatComponent[] fromString(String message) {
        return new StringMessage(message).getOutput();
    }

    private CraftChatMessage() {
    }
}
