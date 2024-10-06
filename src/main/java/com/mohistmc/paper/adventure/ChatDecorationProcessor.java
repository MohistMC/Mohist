package com.mohistmc.paper.adventure;

import com.mohistmc.paper.event.player.AsyncChatCommandDecorateEvent;
import com.mohistmc.paper.event.player.AsyncChatDecorateEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.minecraft.Optionull;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.ChatDecorator;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R1.util.LazyPlayerSet;
import org.bukkit.event.Event;
import org.bukkit.event.player.AsyncPlayerChatPreviewEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;


import static com.mohistmc.paper.adventure.ChatProcessor.DEFAULT_LEGACY_FORMAT;
import static com.mohistmc.paper.adventure.ChatProcessor.canYouHearMe;
import static com.mohistmc.paper.adventure.ChatProcessor.displayName;
import static net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection;

@DefaultQualifier(NonNull.class)
public final class ChatDecorationProcessor {

    private static final String DISPLAY_NAME_TAG = "---paper_dn---";
    private static final Pattern DISPLAY_NAME_PATTERN = Pattern.compile("%(1\\$)?s");
    private static final String CONTENT_TAG = "---paper_content---";
    private static final Pattern CONTENT_PATTERN = Pattern.compile("%(2\\$)?s");

    final MinecraftServer server;
    final @Nullable ServerPlayer player;
    final @Nullable CommandSourceStack commandSourceStack;
    final Component originalMessage;

    public ChatDecorationProcessor(final MinecraftServer server, final @Nullable ServerPlayer player, final @Nullable CommandSourceStack commandSourceStack, final net.minecraft.network.chat.Component originalMessage) {
        this.server = server;
        this.player = player;
        this.commandSourceStack = commandSourceStack;
        this.originalMessage = PaperAdventure.asAdventure(originalMessage);
    }

    public CompletableFuture<ChatDecorator.Result> process() {
        return CompletableFuture.supplyAsync(() -> {
            ChatDecorator.Result result = new ChatDecorator.ModernResult(this.originalMessage, true, false);
            if (listenToLegacy()) {
                result = this.processLegacy(result);
            }
            return this.processModern(result);
        }, this.server.chatExecutor);
    }

    @SuppressWarnings("deprecation")
    private static boolean listenToLegacy() {
        return canYouHearMe(AsyncPlayerChatPreviewEvent.getHandlerList());
    }

    @SuppressWarnings("deprecation")
    private ChatDecorator.Result processLegacy(final ChatDecorator.Result input) {
        if (this.player != null) {
            final CraftPlayer player = this.player.getBukkitEntity();
            final String originalMessage = legacySection().serialize(this.originalMessage);
            final AsyncPlayerChatPreviewEvent event = new AsyncPlayerChatPreviewEvent(true, player, originalMessage, new LazyPlayerSet(this.server));
            this.post(event);

            final boolean isDefaultFormat = DEFAULT_LEGACY_FORMAT.equals(event.getFormat());
            if (event.isCancelled() || (isDefaultFormat && originalMessage.equals(event.getMessage()))) {
                return input;
            } else {
                final Component message = legacySection().deserialize(event.getMessage());
                final Component component = isDefaultFormat ? message : legacyFormat(event.getFormat(), ((CraftPlayer) event.getPlayer()), legacySection().deserialize(event.getMessage()));
                return legacy(component, event.getFormat(), new ChatDecorator.MessagePair(message, event.getMessage()), isDefaultFormat);
            }
        }
        return input;
    }

    private ChatDecorator.Result processModern(final ChatDecorator.Result input) {
        final @Nullable CraftPlayer player = Optionull.map(this.player, ServerPlayer::getBukkitEntity);

        final Component initialResult = input.message().component();
        final AsyncChatDecorateEvent event;
        if (this.commandSourceStack != null) {
            // TODO more command decorate context
            event = new AsyncChatCommandDecorateEvent(true, player, this.originalMessage, initialResult);
        } else {
            event = new AsyncChatDecorateEvent(true, player, this.originalMessage, initialResult);
        }
        this.post(event);
        if (!event.isCancelled() && !event.result().equals(initialResult)) {
            if (input instanceof ChatDecorator.LegacyResult legacyResult) {
                if (legacyResult.hasNoFormatting()) {
                    /*
                    The MessagePair in the decoration result may be different at this point. This is because the legacy
                    decoration system requires the same modifications be made to the message, so we can't have the initial
                    message value for the legacy chat events be changed by the modern decorate event.
                     */
                    return noFormatting(event.result(), legacyResult.format(), legacyResult.message().legacyMessage());
                } else {
                    final Component formatted = legacyFormat(legacyResult.format(), player, event.result());
                    return withFormatting(formatted, legacyResult.format(), event.result(), legacyResult.message().legacyMessage());
                }
            } else {
                return new ChatDecorator.ModernResult(event.result(), true, false);
            }
        }
        return input;
    }

    private void post(final Event event) {
        this.server.server.getPluginManager().callEvent(event);
    }

    private static Component legacyFormat(final String format, final @Nullable CraftPlayer player, final Component message) {
        final List<TagResolver.Single> args = new ArrayList<>(player != null ? 2 : 1);
        if (player != null) {
            args.add(Placeholder.component(DISPLAY_NAME_TAG, displayName(player)));
        }
        args.add(Placeholder.component(CONTENT_TAG, message));
        String miniMsg = MiniMessage.miniMessage().serialize(legacySection().deserialize(format));
        miniMsg = DISPLAY_NAME_PATTERN.matcher(miniMsg).replaceFirst("<" + DISPLAY_NAME_TAG + ">");
        miniMsg = CONTENT_PATTERN.matcher(miniMsg).replaceFirst("<" + CONTENT_TAG + ">");
        return MiniMessage.miniMessage().deserialize(miniMsg, TagResolver.resolver(args));
    }

    public static ChatDecorator.LegacyResult legacy(final Component maybeFormatted, final String format, final ChatDecorator.MessagePair message, final boolean hasNoFormatting) {
        return new ChatDecorator.LegacyResult(maybeFormatted, format, message, hasNoFormatting, false);
    }

    public static ChatDecorator.LegacyResult noFormatting(final Component component, final String format, final String legacyMessage) {
        return new ChatDecorator.LegacyResult(component, format, new ChatDecorator.MessagePair(component, legacyMessage), true, true);
    }

    public static ChatDecorator.LegacyResult withFormatting(final Component formatted, final String format, final Component message, final String legacyMessage) {
        return new ChatDecorator.LegacyResult(formatted, format, new ChatDecorator.MessagePair(message, legacyMessage), false, true);
    }
}
