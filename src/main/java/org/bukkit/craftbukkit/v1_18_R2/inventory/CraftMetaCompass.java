package org.bukkit.craftbukkit.v1_18_R2.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap.Builder;
import com.mojang.serialization.DataResult;
import java.util.Map;
import java.util.Optional;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.inventory.meta.CompassMeta;

@DelegateDeserialization(CraftMetaItem.SerializableMeta.class)
public class CraftMetaCompass extends CraftMetaItem implements CompassMeta {

    static final ItemMetaKey LODESTONE_DIMENSION = new ItemMetaKey("LodestoneDimension");
    static final ItemMetaKey LODESTONE_POS = new ItemMetaKey("LodestonePos", "lodestone");
    static final ItemMetaKey LODESTONE_POS_WORLD = new ItemMetaKey("LodestonePosWorld");
    static final ItemMetaKey LODESTONE_POS_X = new ItemMetaKey("LodestonePosX");
    static final ItemMetaKey LODESTONE_POS_Y = new ItemMetaKey("LodestonePosY");
    static final ItemMetaKey LODESTONE_POS_Z = new ItemMetaKey("LodestonePosZ");
    static final ItemMetaKey LODESTONE_TRACKED = new ItemMetaKey("LodestoneTracked");

    private StringTag lodestoneWorld;
    private int lodestoneX;
    private int lodestoneY;
    private int lodestoneZ;
    private Boolean tracked;

    CraftMetaCompass(CraftMetaItem meta) {
        super(meta);
        if (!(meta instanceof CraftMetaCompass)) {
            return;
        }
        CraftMetaCompass compassMeta = (CraftMetaCompass) meta;
        lodestoneWorld = compassMeta.lodestoneWorld;
        lodestoneX = compassMeta.lodestoneX;
        lodestoneY = compassMeta.lodestoneY;
        lodestoneZ = compassMeta.lodestoneZ;
        tracked = compassMeta.tracked;
    }

    CraftMetaCompass(CompoundTag tag) {
        super(tag);
        if (tag.contains(LODESTONE_DIMENSION.NBT) && tag.contains(LODESTONE_POS.NBT)) {
            lodestoneWorld = (StringTag) tag.get(LODESTONE_DIMENSION.NBT);
            CompoundTag pos = tag.getCompound(LODESTONE_POS.NBT);
            lodestoneX = pos.getInt("X");
            lodestoneY = pos.getInt("Y");
            lodestoneZ = pos.getInt("Z");
        }
        if (tag.contains(LODESTONE_TRACKED.NBT)) {
            tracked = tag.getBoolean(LODESTONE_TRACKED.NBT);
        }
    }

    CraftMetaCompass(Map<String, Object> map) {
        super(map);
        String lodestoneWorldString = SerializableMeta.getString(map, LODESTONE_POS_WORLD.BUKKIT, true);
        if (lodestoneWorldString != null) {
            lodestoneWorld = StringTag.valueOf(lodestoneWorldString);
            lodestoneX = (Integer) map.get(LODESTONE_POS_X.BUKKIT);
            lodestoneY = (Integer) map.get(LODESTONE_POS_Y.BUKKIT);
            lodestoneZ = (Integer) map.get(LODESTONE_POS_Z.BUKKIT);
        } else {
            // legacy
            Location lodestone = SerializableMeta.getObject(Location.class, map, LODESTONE_POS.BUKKIT, true);
            if (lodestone != null && lodestone.getWorld() != null) {
                setLodestone(lodestone);
            }
        }
        tracked = SerializableMeta.getBoolean(map, LODESTONE_TRACKED.BUKKIT);
    }

    @Override
    void applyToItem(CompoundTag tag) {
        super.applyToItem(tag);

        if (lodestoneWorld != null) {
            CompoundTag pos = new CompoundTag();
            pos.putInt("X", lodestoneX);
            pos.putInt("Y", lodestoneY);
            pos.putInt("Z", lodestoneZ);
            tag.put(LODESTONE_POS.NBT, pos);
            tag.put(LODESTONE_DIMENSION.NBT, lodestoneWorld);
        }

        if (tracked != null) {
            tag.putBoolean(LODESTONE_TRACKED.NBT, tracked);
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && isCompassEmpty();
    }

    boolean isCompassEmpty() {
        return !(hasLodestone() || hasLodestoneTracked());
    }

    @Override
    boolean applicableTo(Material type) {
        return type == Material.COMPASS;
    }

    @Override
    public CraftMetaCompass clone() {
        CraftMetaCompass clone = ((CraftMetaCompass) super.clone());
        return clone;
    }

    @Override
    public boolean hasLodestone() {
        return lodestoneWorld != null;
    }

    @Override
    public Location getLodestone() {
        if (lodestoneWorld == null) {
            return null;
        }
        Optional<ResourceKey<net.minecraft.world.level.Level>> key = net.minecraft.world.level.Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, lodestoneWorld).result();
        ServerLevel worldServer = key.isPresent() ? MinecraftServer.getServer().getLevel(key.get()) : null;
        World world = worldServer != null ? worldServer.getWorld() : null;
        return new Location(world, lodestoneX, lodestoneY, lodestoneZ); // world may be null here, if the referenced world is not loaded
    }

    @Override
    public void setLodestone(Location lodestone) {
        Preconditions.checkArgument(lodestone == null || lodestone.getWorld() != null, "world is null");
        if (lodestone == null) {
            this.lodestoneWorld = null;
        } else {
            ResourceKey<net.minecraft.world.level.Level> key = ((CraftWorld) lodestone.getWorld()).getHandle().dimension();
            DataResult<Tag> dataresult = net.minecraft.world.level.Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, key);
            this.lodestoneWorld = (StringTag) dataresult.get().orThrow();
            this.lodestoneX = lodestone.getBlockX();
            this.lodestoneY = lodestone.getBlockY();
            this.lodestoneZ = lodestone.getBlockZ();
        }
    }

    boolean hasLodestoneTracked() {
        return tracked != null;
    }

    @Override
    public boolean isLodestoneTracked() {
        return hasLodestoneTracked() && tracked;
    }

    @Override
    public void setLodestoneTracked(boolean tracked) {
        this.tracked = tracked;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (hasLodestone()) {
            hash = 73 * hash + lodestoneWorld.hashCode();
            hash = 73 * hash + lodestoneX;
            hash = 73 * hash + lodestoneY;
            hash = 73 * hash + lodestoneZ;
        }
        if (hasLodestoneTracked()) {
            hash = 73 * hash + (isLodestoneTracked() ? 1231 : 1237);
        }

        return original != hash ? CraftMetaCompass.class.hashCode() ^ hash : hash;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaCompass) {
            CraftMetaCompass that = (CraftMetaCompass) meta;

            return (this.hasLodestone() ? that.hasLodestone() && this.lodestoneWorld.getAsString().equals(that.lodestoneWorld.getAsString())
                    && this.lodestoneX == that.lodestoneX && this.lodestoneY == that.lodestoneY
                    && this.lodestoneZ == that.lodestoneZ : !that.hasLodestone())
                    && this.tracked == that.tracked;
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaCompass || isCompassEmpty());
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);

        if (hasLodestone()) {
            builder.put(LODESTONE_POS_WORLD.BUKKIT, lodestoneWorld.getAsString());
            builder.put(LODESTONE_POS_X.BUKKIT, lodestoneX);
            builder.put(LODESTONE_POS_Y.BUKKIT, lodestoneY);
            builder.put(LODESTONE_POS_Z.BUKKIT, lodestoneZ);
        }
        if (hasLodestoneTracked()) {
            builder.put(LODESTONE_TRACKED.BUKKIT, tracked);
        }

        return builder;
    }
}
