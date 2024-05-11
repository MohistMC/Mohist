package com.mohistmc.paper.adventure;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.netty.util.AttributeKey;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import net.kyori.adventure.translation.Translator;
import net.kyori.adventure.util.Codec;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.locale.Language;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.WrittenBookItem;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_20_R1.command.VanillaCommandWrapper;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PaperAdventure {
    private static final Pattern LOCALIZATION_PATTERN = Pattern.compile("%(?:(\\d+)\\$)?s");
    public static final ComponentFlattener FLATTENER = ComponentFlattener.basic().toBuilder()
            .complexMapper(TranslatableComponent.class, (translatable, consumer) -> {
                if (!Language.getInstance().has(translatable.key())) {
                    for (final Translator source : GlobalTranslator.translator().sources()) {
                        if (source instanceof TranslationRegistry registry && registry.contains(translatable.key())) {
                            consumer.accept(GlobalTranslator.render(translatable, Locale.US));
                            return;
                        }
                    }
                }
                final @Nullable String fallback = translatable.fallback();
                final @NotNull String translated = Language.getInstance().getOrDefault(translatable.key(), fallback != null ? fallback : translatable.key());

                final Matcher matcher = LOCALIZATION_PATTERN.matcher(translated);
                final List<Component> args = translatable.args();
                int argPosition = 0;
                int lastIdx = 0;
                while (matcher.find()) {
                    // append prior
                    if (lastIdx < matcher.start()) {
                        consumer.accept(Component.text(translated.substring(lastIdx, matcher.start())));
                    }
                    lastIdx = matcher.end();

                    final @Nullable String argIdx = matcher.group(1);
                    // calculate argument position
                    if (argIdx != null) {
                        try {
                            final int idx = Integer.parseInt(argIdx) - 1;
                            if (idx < args.size()) {
                                consumer.accept(args.get(idx));
                            }
                        } catch (final NumberFormatException ex) {
                            // ignore, drop the format placeholder
                        }
                    } else {
                        final int idx = argPosition++;
                        if (idx < args.size()) {
                            consumer.accept(args.get(idx));
                        }
                    }
                }

                // append tail
                if (lastIdx < translated.length()) {
                    consumer.accept(Component.text(translated.substring(lastIdx)));
                }
            })
            .build();
    public static final AttributeKey<Locale> LOCALE_ATTRIBUTE = AttributeKey.valueOf("adventure:locale"); // init after FLATTENER because classloading triggered here might create a logger
    @Deprecated
    public static final PlainComponentSerializer PLAIN = PlainComponentSerializer.builder().flattener(FLATTENER).build();

    private static final Codec<CompoundTag, String, IOException, IOException> NBT_CODEC = new Codec<CompoundTag, String, IOException, IOException>() {
        @Override
        public @NotNull CompoundTag decode(final @NotNull String encoded) throws IOException {
            try {
                return TagParser.parseTag(encoded);
            } catch (final CommandSyntaxException e) {
                throw new IOException(e);
            }
        }

        @Override
        public @NotNull String encode(final @NotNull CompoundTag decoded) {
            return decoded.toString();
        }
    };
    public static final ComponentSerializer<Component, Component, net.minecraft.network.chat.Component> WRAPPER_AWARE_SERIALIZER = new WrapperAwareSerializer();

    private PaperAdventure() {
    }

    // Key

    public static ResourceLocation asVanilla(final Key key) {
        return new ResourceLocation(key.namespace(), key.value());
    }

    public static ResourceLocation asVanillaNullable(final Key key) {
        if (key == null) {
            return null;
        }
        return asVanilla(key);
    }

    // Component

    public static Component asAdventure(final net.minecraft.network.chat.Component component) {
        return component == null ? Component.empty() : GsonComponentSerializer.gson().serializer().fromJson(net.minecraft.network.chat.Component.Serializer.toJsonTree(component), Component.class);
    }

    public static ArrayList<Component> asAdventure(final List<net.minecraft.network.chat.Component> vanillas) {
        final ArrayList<Component> adventures = new ArrayList<>(vanillas.size());
        for (final net.minecraft.network.chat.Component vanilla : vanillas) {
            adventures.add(asAdventure(vanilla));
        }
        return adventures;
    }

    public static ArrayList<Component> asAdventureFromJson(final List<String> jsonStrings) {
        final ArrayList<Component> adventures = new ArrayList<>(jsonStrings.size());
        for (final String json : jsonStrings) {
            adventures.add(GsonComponentSerializer.gson().deserialize(json));
        }
        return adventures;
    }

    public static List<String> asJson(final List<? extends Component> adventures) {
        final List<String> jsons = new ArrayList<>(adventures.size());
        for (final Component component : adventures) {
            jsons.add(GsonComponentSerializer.gson().serialize(component));
        }
        return jsons;
    }

    public static net.minecraft.network.chat.Component asVanilla(final Component component) {
        if (component == null) return null;
        if (true) return new AdventureComponent(component);
        return net.minecraft.network.chat.Component.Serializer.fromJson(GsonComponentSerializer.gson().serializer().toJsonTree(component));
    }

    public static List<net.minecraft.network.chat.Component> asVanilla(final List<Component> adventures) {
        final List<net.minecraft.network.chat.Component> vanillas = new ArrayList<>(adventures.size());
        for (final Component adventure : adventures) {
            vanillas.add(asVanilla(adventure));
        }
        return vanillas;
    }

    public static String asJsonString(final Component component, final Locale locale) {
        return GsonComponentSerializer.gson().serialize(translated(component, locale));
    }

    public static String asJsonString(final net.minecraft.network.chat.Component component, final Locale locale) {
        if (component instanceof AdventureComponent) {
            return asJsonString(((AdventureComponent) component).adventure, locale);
        }
        return net.minecraft.network.chat.Component.Serializer.toJson(component);
    }

    public static String asPlain(final Component component, final Locale locale) {
        return PlainTextComponentSerializer.plainText().serialize(translated(component, locale));
    }

    private static Component translated(final Component component, final Locale locale) {
        return GlobalTranslator.render(
                component,
                // play it safe
                locale != null
                        ? locale
                        : Locale.US
        );
    }

    public static Component resolveWithContext(final @NotNull Component component, final @Nullable CommandSender context, final @Nullable org.bukkit.entity.Entity scoreboardSubject, final boolean bypassPermissions) throws IOException {
        final CommandSourceStack css = context != null ? VanillaCommandWrapper.getListener(context) : null;
        Boolean previous = null;
        if (css != null && bypassPermissions) {
            previous = css.bypassSelectorPermissions;
            css.bypassSelectorPermissions = true;
        }
        try {
            return asAdventure(ComponentUtils.updateForEntity(css, asVanilla(component), scoreboardSubject == null ? null : ((CraftEntity) scoreboardSubject).getHandle(), 0));
        } catch (CommandSyntaxException e) {
            throw new IOException(e);
        } finally {
            if (css != null && previous != null) {
                css.bypassSelectorPermissions = previous;
            }
        }
    }

    // BossBar

    public static BossEvent.BossBarColor asVanilla(final BossBar.Color color) {
        return switch (color) {
            case PINK -> BossEvent.BossBarColor.PINK;
            case BLUE -> BossEvent.BossBarColor.BLUE;
            case RED -> BossEvent.BossBarColor.RED;
            case GREEN -> BossEvent.BossBarColor.GREEN;
            case YELLOW -> BossEvent.BossBarColor.YELLOW;
            case PURPLE -> BossEvent.BossBarColor.PURPLE;
            case WHITE -> BossEvent.BossBarColor.WHITE;
        };
    }

    public static BossBar.Color asAdventure(final BossEvent.BossBarColor color) {
        return switch (color) {
            case PINK -> BossBar.Color.PINK;
            case BLUE -> BossBar.Color.BLUE;
            case RED -> BossBar.Color.RED;
            case GREEN -> BossBar.Color.GREEN;
            case YELLOW -> BossBar.Color.YELLOW;
            case PURPLE -> BossBar.Color.PURPLE;
            case WHITE -> BossBar.Color.WHITE;
        };
    }

    public static BossEvent.BossBarOverlay asVanilla(final BossBar.Overlay overlay) {
        return switch (overlay) {
            case PROGRESS -> BossEvent.BossBarOverlay.PROGRESS;
            case NOTCHED_6 -> BossEvent.BossBarOverlay.NOTCHED_6;
            case NOTCHED_10 -> BossEvent.BossBarOverlay.NOTCHED_10;
            case NOTCHED_12 -> BossEvent.BossBarOverlay.NOTCHED_12;
            case NOTCHED_20 -> BossEvent.BossBarOverlay.NOTCHED_20;
        };
    }

    public static BossBar.Overlay asAdventure(final BossEvent.BossBarOverlay overlay) {
        return switch (overlay) {
            case PROGRESS -> BossBar.Overlay.PROGRESS;
            case NOTCHED_6 -> BossBar.Overlay.NOTCHED_6;
            case NOTCHED_10 -> BossBar.Overlay.NOTCHED_10;
            case NOTCHED_12 -> BossBar.Overlay.NOTCHED_12;
            case NOTCHED_20 -> BossBar.Overlay.NOTCHED_20;
        };
    }

    public static void setFlag(final BossBar bar, final BossBar.Flag flag, final boolean value) {
        if (value) {
            bar.addFlag(flag);
        } else {
            bar.removeFlag(flag);
        }
    }

    // Book

    public static ItemStack asItemStack(final Book book, final Locale locale) {
        final ItemStack item = new ItemStack(net.minecraft.world.item.Items.WRITTEN_BOOK, 1);
        final CompoundTag tag = item.getOrCreateTag();
        tag.putString(WrittenBookItem.TAG_TITLE, validateField(asPlain(book.title(), locale), WrittenBookItem.TITLE_MAX_LENGTH, WrittenBookItem.TAG_TITLE));
        tag.putString(WrittenBookItem.TAG_AUTHOR, asPlain(book.author(), locale));
        final ListTag pages = new ListTag();
        if (book.pages().size() > WrittenBookItem.MAX_PAGES) {
            throw new IllegalArgumentException("Book provided had " + book.pages().size() + " pages, but is only allowed a maximum of " + WrittenBookItem.MAX_PAGES);
        }
        for (final Component page : book.pages()) {
            pages.add(StringTag.valueOf(validateField(asJsonString(page, locale), WrittenBookItem.PAGE_LENGTH, "page")));
        }
        tag.put(WrittenBookItem.TAG_PAGES, pages);
        return item;
    }

    private static String validateField(final String content, final int length, final String name) {
        if (content == null) {
            return content;
        }

        final int actual = content.length();
        if (actual > length) {
            throw new IllegalArgumentException("Field '" + name + "' has a maximum length of " + length + " but was passed '" + content + "', which was " + actual + " characters long.");
        }
        return content;
    }

    // Sounds

    public static SoundSource asVanilla(final Sound.Source source) {
        return switch (source) {
            case MASTER -> SoundSource.MASTER;
            case MUSIC -> SoundSource.MUSIC;
            case RECORD -> SoundSource.RECORDS;
            case WEATHER -> SoundSource.WEATHER;
            case BLOCK -> SoundSource.BLOCKS;
            case HOSTILE -> SoundSource.HOSTILE;
            case NEUTRAL -> SoundSource.NEUTRAL;
            case PLAYER -> SoundSource.PLAYERS;
            case AMBIENT -> SoundSource.AMBIENT;
            case VOICE -> SoundSource.VOICE;
        };
    }

    public static @Nullable SoundSource asVanillaNullable(final @Nullable Sound.Source source) {
        if (source == null) {
            return null;
        }
        return asVanilla(source);
    }

    public static Packet<?> asSoundPacket(final Sound sound, final double x, final double y, final double z, final long seed, @Nullable BiConsumer<Packet<?>, Float> packetConsumer) {
        final ResourceLocation name = asVanilla(sound.name());
        final Optional<SoundEvent> soundEvent = BuiltInRegistries.SOUND_EVENT.getOptional(name);
        final SoundSource source = asVanilla(sound.source());

        final Holder<SoundEvent> soundEventHolder = soundEvent.map(BuiltInRegistries.SOUND_EVENT::wrapAsHolder).orElseGet(() -> Holder.direct(SoundEvent.createVariableRangeEvent(name)));
        final Packet<?> packet = new ClientboundSoundPacket(soundEventHolder, source, x, y, z, sound.volume(), sound.pitch(), seed);
        if (packetConsumer != null) {
            packetConsumer.accept(packet, soundEventHolder.value().getRange(sound.volume()));
        }
        return packet;
    }

    public static Packet<?> asSoundPacket(final Sound sound, final Entity emitter, final long seed, @Nullable BiConsumer<Packet<?>, Float> packetConsumer) {
        final ResourceLocation name = asVanilla(sound.name());
        final Optional<SoundEvent> soundEvent = BuiltInRegistries.SOUND_EVENT.getOptional(name);
        final SoundSource source = asVanilla(sound.source());

        final Holder<SoundEvent> soundEventHolder = soundEvent.map(BuiltInRegistries.SOUND_EVENT::wrapAsHolder).orElseGet(() -> Holder.direct(SoundEvent.createVariableRangeEvent(name)));
        final Packet<?> packet = new ClientboundSoundEntityPacket(soundEventHolder, source, emitter, sound.volume(), sound.pitch(), seed);
        if (packetConsumer != null) {
            packetConsumer.accept(packet, soundEventHolder.value().getRange(sound.volume()));
        }
        return packet;
    }

    // NBT

    public static @Nullable BinaryTagHolder asBinaryTagHolder(final @Nullable CompoundTag tag) {
        if (tag == null) {
            return null;
        }
        try {
            return BinaryTagHolder.encode(tag, NBT_CODEC);
        } catch (final IOException e) {
            return null;
        }
    }

    // Colors

    public static @NotNull TextColor asAdventure(final ChatFormatting formatting) {
        final Integer color = formatting.getColor();
        if (color == null) {
            throw new IllegalArgumentException("Not a valid color");
        }
        return TextColor.color(color);
    }

    public static @Nullable ChatFormatting asVanilla(final TextColor color) {
        return ChatFormatting.getByHexValue(color.value());
    }
}
