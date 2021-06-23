package io.papermc.paper.adventure;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.gson.LegacyHoverEventSerializer;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import net.kyori.adventure.util.Codec;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.JsonToNBT;

import java.io.IOException;
import java.util.UUID;

final class NBTLegacyHoverEventSerializer implements LegacyHoverEventSerializer {
    public static final NBTLegacyHoverEventSerializer INSTANCE = new NBTLegacyHoverEventSerializer();
    private static final Codec<CompoundNBT, String, CommandSyntaxException, RuntimeException> SNBT_CODEC = Codec.of(JsonToNBT::parseTag, INBT::toString);

    static final String ITEM_TYPE = "id";
    static final String ITEM_COUNT = "Count";
    static final String ITEM_TAG = "tag";

    static final String ENTITY_NAME = "name";
    static final String ENTITY_TYPE = "type";
    static final String ENTITY_ID = "id";

    NBTLegacyHoverEventSerializer() {
    }

    @Override
    public HoverEvent.ShowItem deserializeShowItem(final Component input) throws IOException {
        final String raw = PlainComponentSerializer.plain().serialize(input);
        try {
            final CompoundNBT contents = SNBT_CODEC.decode(raw);
            final CompoundNBT tag = contents.getCompound(ITEM_TAG);
            return HoverEvent.ShowItem.of(
                    Key.key(contents.getString(ITEM_TYPE)),
                    contents.contains(ITEM_COUNT) ? contents.getByte(ITEM_COUNT) : 1,
                    tag.isEmpty() ? null : BinaryTagHolder.encode(tag, SNBT_CODEC)
            );
        } catch (final CommandSyntaxException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public HoverEvent.ShowEntity deserializeShowEntity(final Component input, final Codec.Decoder<Component, String, ? extends RuntimeException> componentCodec) throws IOException {
        final String raw = PlainComponentSerializer.plain().serialize(input);
        try {
            final CompoundNBT contents = SNBT_CODEC.decode(raw);
            return HoverEvent.ShowEntity.of(
                    Key.key(contents.getString(ENTITY_TYPE)),
                    UUID.fromString(contents.getString(ENTITY_ID)),
                    componentCodec.decode(contents.getString(ENTITY_NAME))
            );
        } catch (final CommandSyntaxException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public Component serializeShowItem(final HoverEvent.ShowItem input) throws IOException {
        final CompoundNBT tag = new CompoundNBT();
        tag.putString(ITEM_TYPE, input.item().asString());
        tag.putByte(ITEM_COUNT, (byte) input.count());
        if (input.nbt() != null) {
            try {
                tag.put(ITEM_TAG, input.nbt().get(SNBT_CODEC));
            } catch (final CommandSyntaxException ex) {
                throw new IOException(ex);
            }
        }
        return Component.text(SNBT_CODEC.encode(tag));
    }

    @Override
    public Component serializeShowEntity(final HoverEvent.ShowEntity input, final Codec.Encoder<Component, String, ? extends RuntimeException> componentCodec) throws IOException {
        final CompoundNBT tag = new CompoundNBT();
        tag.putString(ENTITY_ID, input.id().toString());
        tag.putString(ENTITY_TYPE, input.type().asString());
        if (input.name() != null) {
            tag.putString(ENTITY_NAME, componentCodec.encode(input.name()));
        }
        return Component.text(SNBT_CODEC.encode(tag));
    }
}
