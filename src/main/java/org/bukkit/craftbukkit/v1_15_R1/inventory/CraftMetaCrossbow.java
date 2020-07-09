package org.bukkit.craftbukkit.v1_15_R1.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.item.ArrowItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftMagicNumbers;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;

@DelegateDeserialization(CraftMetaItem.SerializableMeta.class)
public class CraftMetaCrossbow extends CraftMetaItem implements CrossbowMeta {

    static final ItemMetaKey CHARGED = new ItemMetaKey("Charged", "charged");
    static final ItemMetaKey CHARGED_PROJECTILES = new ItemMetaKey("ChargedProjectiles", "charged-projectiles");
    //
    private boolean charged;
    private List<ItemStack> chargedProjectiles;

    CraftMetaCrossbow(CraftMetaItem meta) {
        super(meta);

        if (!(meta instanceof CraftMetaCrossbow)) {
            return;
        }

        CraftMetaCrossbow crossbow = (CraftMetaCrossbow) meta;
        this.charged = crossbow.charged;

        if (crossbow.hasChargedProjectiles()) {
            this.chargedProjectiles = new ArrayList<>(crossbow.chargedProjectiles);
        }
    }

    CraftMetaCrossbow(CompoundNBT tag) {
        super(tag);

        charged = tag.getBoolean(CHARGED.NBT);

        if (tag.contains(CHARGED_PROJECTILES.NBT, CraftMagicNumbers.NBT.TAG_LIST)) {
            ListNBT list = tag.getList(CHARGED_PROJECTILES.NBT, CraftMagicNumbers.NBT.TAG_COMPOUND);

            if (list != null && !list.isEmpty()) {
                chargedProjectiles = new ArrayList<>();

                for (int i = 0; i < list.size(); i++) {
                    CompoundNBT nbttagcompound1 = list.getCompound(i);

                    chargedProjectiles.add(CraftItemStack.asCraftMirror(net.minecraft.item.ItemStack.read(nbttagcompound1)));
                }
            }
        }
    }

    CraftMetaCrossbow(Map<String, Object> map) {
        super(map);

        Boolean charged = SerializableMeta.getObject(Boolean.class, map, CHARGED.BUKKIT, true);
        if (charged != null) {
            this.charged = charged;
        }

        Iterable<?> projectiles = SerializableMeta.getObject(Iterable.class, map, CHARGED_PROJECTILES.BUKKIT, true);
        if (projectiles != null) {
            for (Object stack : projectiles) {
                if (stack instanceof ItemStack) {
                    addChargedProjectile((ItemStack) stack);
                }
            }
        }
    }

    @Override
    void applyToItem(CompoundNBT tag) {
        super.applyToItem(tag);

        tag.putBoolean(CHARGED.NBT, charged);
        if (hasChargedProjectiles()) {
            ListNBT list = new ListNBT();

            for (ItemStack item : chargedProjectiles) {
                CompoundNBT saved = new CompoundNBT();
                CraftItemStack.asNMSCopy(item).deserializeNBT(saved);
                list.add(saved);
            }

            tag.put(CHARGED_PROJECTILES.NBT, list);
        }
    }

    @Override
    boolean applicableTo(Material type) {
        switch (type) {
            case CROSSBOW:
                return true;
            default:
                return false;
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && isCrossbowEmpty();
    }

    boolean isCrossbowEmpty() {
        return !(hasChargedProjectiles());
    }

    @Override
    public boolean hasChargedProjectiles() {
        return chargedProjectiles != null;
    }

    @Override
    public List<ItemStack> getChargedProjectiles() {
        return (chargedProjectiles == null) ? null : ImmutableList.copyOf(chargedProjectiles);
    }

    @Override
    public void setChargedProjectiles(List<ItemStack> projectiles) {
        chargedProjectiles = null;
        charged = false;

        if (projectiles == null) {
            return;
        }

        for (ItemStack i : projectiles) {
            addChargedProjectile(i);
        }
    }

    @Override
    public void addChargedProjectile(ItemStack item) {
        Preconditions.checkArgument(item != null, "item");
        Preconditions.checkArgument(item.getType() == Material.FIREWORK_ROCKET || CraftMagicNumbers.getItem(item.getType()) instanceof ArrowItem, "Item %s is not an arrow or firework rocket", item);

        if (chargedProjectiles == null) {
            chargedProjectiles = new ArrayList<>();
        }

        charged = true;
        chargedProjectiles.add(item);
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaCrossbow) {
            CraftMetaCrossbow that = (CraftMetaCrossbow) meta;

            return this.charged == that.charged
                    && (hasChargedProjectiles() ? that.hasChargedProjectiles() && this.chargedProjectiles.equals(that.chargedProjectiles) : !that.hasChargedProjectiles());
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaCrossbow || isCrossbowEmpty());
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();

        if (hasChargedProjectiles()) {
            hash = 61 * hash + (this.charged ? 1 : 0);
            hash = 61 * hash + chargedProjectiles.hashCode();
        }

        return original != hash ? CraftMetaCrossbow.class.hashCode() ^ hash : hash;
    }

    @Override
    public CraftMetaCrossbow clone() {
        return (CraftMetaCrossbow) super.clone();
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);

        builder.put(CHARGED.BUKKIT, charged);
        if (hasChargedProjectiles()) {
            builder.put(CHARGED_PROJECTILES.BUKKIT, chargedProjectiles);
        }

        return builder;
    }
}
