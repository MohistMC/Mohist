package me.jellysquid.mods.lithium.common.world.chunk.palette;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.palette.IPalette;
import net.minecraft.util.palette.PaletteHashMap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Function;

/**
 * Generally provides better performance over the vanilla {@link PaletteHashMap} when calling
 * {@link LithiumPaletteHashMap#idFor(Object)} through using a faster backing map.
 */
public class LithiumPaletteHashMap<T> implements IPalette<T> {
    private final ObjectIntIdentityMap<T> idList;
    private final LithiumIntIdentityHashBiMap<T> map;
    private final LithiumResizeCallback<T> resizeHandler;
    private final Function<CompoundNBT, T> elementDeserializer;
    private final Function<T, CompoundNBT> elementSerializer;
    private final int indexBits;

    public LithiumPaletteHashMap(ObjectIntIdentityMap<T> ids, int bits, LithiumResizeCallback<T> resizeHandler, Function<CompoundNBT, T> deserializer, Function<T, CompoundNBT> serializer) {
        this.idList = ids;
        this.indexBits = bits;
        this.resizeHandler = resizeHandler;
        this.elementDeserializer = deserializer;
        this.elementSerializer = serializer;
        this.map = new LithiumIntIdentityHashBiMap<>(1 << bits);
    }

    @Override
    public int idFor(T obj) {
        int id = this.map.getId(obj);

        if (id == -1) {
            id = this.map.add(obj);

            if (id >= 1 << this.indexBits) {
                if (this.resizeHandler == null) {
                    throw new IllegalStateException("Cannot grow");
                } else {
                    id = this.resizeHandler.onLithiumPaletteResized(this.indexBits + 1, obj);
                }
            }
        }

        return id;
    }

    @Override
    public boolean contains(T obj) {
        return this.map.containsObject(obj);
    }

    @Override
    public T get(int id) {
        return this.map.getByValue(id);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void read(PacketBuffer buf) {
        this.map.clear();
        int entryCount = buf.readVarInt();

        for (int i = 0; i < entryCount; ++i) {
            this.map.add(this.idList.getByValue(buf.readVarInt()));
        }
    }

    @Override
    public void write(PacketBuffer buf) {
        int paletteBits = this.getSize();
        buf.writeVarInt(paletteBits);

        for (int i = 0; i < paletteBits; ++i) {
            buf.writeVarInt(this.idList.get(this.map.getByValue(i)));
        }
    }

    @Override
    public int getSerializedSize() {
        int size = PacketBuffer.getVarIntSize(this.getSize());

        for (int i = 0; i < this.getSize(); ++i) {
            size += PacketBuffer.getVarIntSize(this.idList.get(this.map.getByValue(i)));
        }

        return size;
    }

    @Override
    public void read(ListNBT list) {
        this.map.clear();

        for (int i = 0; i < list.size(); ++i) {
            this.map.add(this.elementDeserializer.apply(list.getCompound(i)));
        }
    }

    public void toTag(ListNBT list) {
        for (int i = 0; i < this.getSize(); ++i) {
            list.add(this.elementSerializer.apply(this.map.getByValue(i)));
        }
    }

    public int getSize() {
        return this.map.size();
    }
}
