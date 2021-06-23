package io.papermc.paper.adventure;

import io.papermc.paper.chat.ChatComposer;
import io.papermc.paper.event.player.AbstractChatEvent;
import io.papermc.paper.event.player.AsyncChatEvent;
import io.papermc.paper.event.player.ChatEvent;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.ClickEvent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.util.LazyPlayerSet;
import org.bukkit.craftbukkit.v1_16_R3.util.Waitable;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public final class ChatProcessor {
    // <-- copied from adventure-text-serializer-legacy
    private static final Pattern DEFAULT_URL_PATTERN = Pattern.compile("(?:(https?)://)?([-\\w_.]+\\.\\w{2,})(/\\S*)?");
    private static final Pattern URL_SCHEME_PATTERN = Pattern.compile("^[a-z][a-z0-9\\-.]*:");
    private static final TextReplacementConfig URL_REPLACEMENT_CONFIG = TextReplacementConfig.builder()
            .match(DEFAULT_URL_PATTERN)
            .replacement(url -> {
                String clickUrl = url.content();
                if (!URL_SCHEME_PATTERN.matcher(clickUrl).find()) {
                    clickUrl = "http://" + clickUrl;
                }
                return url.clickEvent(ClickEvent.openUrl(clickUrl));
            })
            .build();
    // copied from adventure-text-serializer-legacy -->
    final MinecraftServer server;
    final PlayerEntity player;
    final String message;
    final boolean async;

    public ChatProcessor(final MinecraftServer server, final PlayerEntity player, final String message, final boolean async) {
        this.server = server;
        this.player = player;
        this.message = message;
        this.async = async;
    }

    @SuppressWarnings("CodeBlock2Expr")
    public void process() {
        this.processingLegacyFirst(
                // continuing from AsyncPlayerChatEvent (without PlayerChatEvent)
                event -> {
                    this.processModern(
                            legacyComposer(event.getFormat(), legacyDisplayName((CraftPlayer) event.getPlayer()), event.getMessage()),
                            event.getRecipients(),
                            PaperAdventure.LEGACY_SECTION_UXRC.deserialize(event.getMessage()),
                            event.isCancelled()
                    );
                },
                // continuing from AsyncPlayerChatEvent and PlayerChatEvent
                event -> {
                    this.processModern(
                            legacyComposer(event.getFormat(), legacyDisplayName((CraftPlayer) event.getPlayer()), event.getMessage()),
                            event.getRecipients(),
                            PaperAdventure.LEGACY_SECTION_UXRC.deserialize(event.getMessage()),
                            event.isCancelled()
                    );
                },
                // no legacy events called, all nice and fresh!
                () -> {
                    this.processModern(
                            ChatComposer.DEFAULT,
                            new LazyPlayerSet(this.server),
                            Component.text(this.message).replaceText(URL_REPLACEMENT_CONFIG),
                            false
                    );
                }
        );
    }

    @SuppressWarnings("deprecation")
    private void processingLegacyFirst(
            final Consumer<AsyncPlayerChatEvent> continueAfterAsync,
            final Consumer<PlayerChatEvent> continueAfterAsyncAndSync,
            final Runnable modernOnly
    ) {
        final boolean listenersOnAsyncEvent = anyListeners(AsyncPlayerChatEvent.getHandlerList());
        final boolean listenersOnSyncEvent = anyListeners(PlayerChatEvent.getHandlerList());
        if (listenersOnAsyncEvent || listenersOnSyncEvent) {
            final CraftPlayer player = (CraftPlayer) this.player.getBukkitEntity();
            final AsyncPlayerChatEvent ae = new AsyncPlayerChatEvent(this.async, player, this.message, new LazyPlayerSet(this.server));
            post(ae);
            if (listenersOnSyncEvent) {
                final PlayerChatEvent se = new PlayerChatEvent(player, ae.getMessage(), ae.getFormat(), ae.getRecipients());
                se.setCancelled(ae.isCancelled()); // propagate cancelled state
                this.queueIfAsyncOrRunImmediately(new Waitable<Void>() {
                    @Override
                    protected Void evaluate() {
                        post(se);
                        return null;
                    }
                });
                continueAfterAsyncAndSync.accept(se);
            } else {
                continueAfterAsync.accept(ae);
            }
        } else {
            modernOnly.run();
        }
    }

    private void processModern(final ChatComposer composer, final Set<Player> recipients, final Component message, final boolean cancelled) {
        final AsyncChatEvent ae = this.createAsync(composer, recipients, message);
        ae.setCancelled(cancelled); // propagate cancelled state
        post(ae);
        final boolean listenersOnSyncEvent = anyListeners(ChatEvent.getHandlerList());
        if (listenersOnSyncEvent) {
            this.continueWithSyncFromWhereAsyncLeftOff(ae);
        } else {
            this.complete(ae);
        }
    }

    private void continueWithSyncFromWhereAsyncLeftOff(final AsyncChatEvent ae) {
        this.queueIfAsyncOrRunImmediately(new Waitable<Void>() {
            @Override
            protected Void evaluate() {
                final ChatEvent se = ChatProcessor.this.createSync(ae.composer(), ae.recipients(), ae.message());
                se.setCancelled(ae.isCancelled()); // propagate cancelled state
                post(se);
                ChatProcessor.this.complete(se);
                return null;
            }
        });
    }

    private void complete(final AbstractChatEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final CraftPlayer player = (CraftPlayer) this.player.getBukkitEntity();

        final Component message = event.composer().composeChat(
                event.getPlayer(),
                displayName(player),
                event.message()
        );

        this.server.console.sendMessage(message);

        if (((LazyPlayerSet) event.recipients()).isLazy()) {
            final ITextComponent vanilla = PaperAdventure.asVanilla(message);
            for (final ServerPlayerEntity recipient : this.server.getPlayerList().players) {
                recipient.sendMessage(vanilla, this.player.getUUID());
            }
        } else {
            for (final Player recipient : event.recipients()) {
                recipient.sendMessage(player, message, MessageType.CHAT);
            }
        }
    }

    private AsyncChatEvent createAsync(final ChatComposer composer, final Set<Player> recipients, final Component message) {
        return new AsyncChatEvent(this.async, (CraftPlayer)this.player.getBukkitEntity(), recipients, composer, message);
    }

    private ChatEvent createSync(final ChatComposer composer, final Set<Player> recipients, final Component message) {
        return new ChatEvent((CraftPlayer)this.player.getBukkitEntity(), recipients, composer, message);
    }

    private static String legacyDisplayName(final CraftPlayer player) {
        return player.getDisplayName();
    }

    private static Component displayName(final CraftPlayer player) {
        return player.displayName();
    }

    private static ChatComposer legacyComposer(final String format, final String legacyDisplayName, final String legacyMessage) {
        return (player, displayName, message) -> PaperAdventure.LEGACY_SECTION_UXRC.deserialize(String.format(format, legacyDisplayName, legacyMessage)).replaceText(URL_REPLACEMENT_CONFIG);
    }

    private void queueIfAsyncOrRunImmediately(final Waitable<Void> waitable) {
        if (this.async) {
            this.server.processQueue.add(waitable);
        } else {
            waitable.run();
        }
        try {
            waitable.get();
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt(); // tag, you're it
        } catch (final ExecutionException e) {
            throw new RuntimeException("Exception processing chat", e.getCause());
        }
    }

    private static void post(final Event event) {
        Bukkit.getPluginManager().callEvent(event);
    }

    private static boolean anyListeners(final HandlerList handlers) {
        return handlers.getRegisteredListeners().length > 0;
    }
}
