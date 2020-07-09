package org.bukkit.craftbukkit.v1_15_R1.inventory;

import static org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemFactory.*;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntNBT;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftMetaItem.SerializableMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

@DelegateDeserialization(SerializableMeta.class)
class CraftMetaLeatherArmor extends CraftMetaItem implements LeatherArmorMeta {
    static final ItemMetaKey COLOR = new ItemMetaKey("color");

    private Color color = DEFAULT_LEATHER_COLOR;

    CraftMetaLeatherArmor(CraftMetaItem meta) {
        super(meta);
        if (!(meta instanceof CraftMetaLeatherArmor)) {
            return;
        }

        CraftMetaLeatherArmor armorMeta = (CraftMetaLeatherArmor) meta;
        this.color = armorMeta.color;
    }

    CraftMetaLeatherArmor(CompoundNBT tag) {
        super(tag);
        if (tag.contains(DISPLAY.NBT)) {
            CompoundNBT display = tag.getCompound(DISPLAY.NBT);
            if (display.contains(COLOR.NBT)) {
                try {
                    color = Color.fromRGB(display.getInt(COLOR.NBT));
                } catch (IllegalArgumentException ex) {
                    // Invalid colour
                }
            }
        }
    }

    CraftMetaLeatherArmor(Map<String, Object> map) {
        super(map);
        setColor(SerializableMeta.getObject(Color.class, map, COLOR.BUKKIT, true));
    }

    @Override
    void applyToItem(CompoundNBT itemTag) {
        super.applyToItem(itemTag);

        if (hasColor()) {
            setDisplayTag(itemTag, COLOR.NBT, IntNBT.valueOf(color.asRGB()));
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && isLeatherArmorEmpty();
    }

    boolean isLeatherArmorEmpty() {
        return !(hasColor());
    }

    @Override
    boolean applicableTo(Material type) {
        switch(type) {
            case LEATHER_HELMET:
            case LEATHER_HORSE_ARMOR:
            case LEATHER_CHESTPLATE:
            case LEATHER_LEGGINGS:
            case LEATHER_BOOTS:
                return true;
            default:
                return false;
        }
    }

    @Override
    public CraftMetaLeatherArmor clone() {
        return (CraftMetaLeatherArmor) super.clone();
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color == null ? DEFAULT_LEATHER_COLOR : color;
    }

    boolean hasColor() {
        return !DEFAULT_LEATHER_COLOR.equals(color);
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);

        if (hasColor()) {
            builder.put(COLOR.BUKKIT, color);
        }

        return builder;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaLeatherArmor) {
            CraftMetaLeatherArmor that = (CraftMetaLeatherArmor) meta;

            return color.equals(that.color);
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaLeatherArmor || isLeatherArmorEmpty());
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (hasColor()) {
            hash ^= color.hashCode();
        }
        return original != hash ? CraftMetaSkull.class.hashCode() ^ hash : hash;
    }
}
