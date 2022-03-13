package org.bukkit.craftbukkit.v1_18_R2.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.v1_18_R2.util.CraftMagicNumbers;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BundleMeta;

@DelegateDeserialization(CraftMetaItem.SerializableMeta.class)
public class CraftMetaBundle extends CraftMetaItem implements BundleMeta {

    static final ItemMetaKey ITEMS = new ItemMetaKey("Items", "items");
    //
    private List<ItemStack> items;

    CraftMetaBundle(CraftMetaItem meta) {
        super(meta);

        if (!(meta instanceof CraftMetaBundle)) {
            return;
        }

        CraftMetaBundle bundle = (CraftMetaBundle) meta;

        if (bundle.hasItems()) {
            this.items = new ArrayList<>(bundle.items);
        }
    }

    CraftMetaBundle(CompoundTag tag) {
        super(tag);

        if (tag.contains(ITEMS.NBT, CraftMagicNumbers.NBT.TAG_LIST)) {
            ListTag list = tag.getList(ITEMS.NBT, CraftMagicNumbers.NBT.TAG_COMPOUND);

            if (list != null && !list.isEmpty()) {
                items = new ArrayList<>();

                for (int i = 0; i < list.size(); i++) {
                    CompoundTag nbttagcompound1 = list.getCompound(i);

                    addItem(CraftItemStack.asCraftMirror(net.minecraft.world.item.ItemStack.of(nbttagcompound1)));
                }
            }
        }
    }

    CraftMetaBundle(Map<String, Object> map) {
        super(map);

        Iterable<?> items = SerializableMeta.getObject(Iterable.class, map, ITEMS.BUKKIT, true);
        if (items != null) {
            for (Object stack : items) {
                if (stack instanceof ItemStack) {
                    addItem((ItemStack) stack);
                }
            }
        }
    }

    @Override
    void applyToItem(CompoundTag tag) {
        super.applyToItem(tag);

        if (hasItems()) {
            ListTag list = new ListTag();

            for (ItemStack item : items) {
                CompoundTag saved = new CompoundTag();
                CraftItemStack.asNMSCopy(item).save(saved);
                list.add(saved);
            }

            tag.put(ITEMS.NBT, list);
        }
    }

    @Override
    boolean applicableTo(Material type) {
        switch (type) {
            case BUNDLE:
                return true;
            default:
                return false;
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && isBundleEmpty();
    }

    boolean isBundleEmpty() {
        return !(hasItems());
    }

    @Override
    public boolean hasItems() {
        return items != null && !items.isEmpty();
    }

    @Override
    public List<ItemStack> getItems() {
        return (items == null) ? ImmutableList.of() : ImmutableList.copyOf(items);
    }

    @Override
    public void setItems(List<ItemStack> items) {
        this.items = null;

        if (items == null) {
            return;
        }

        for (ItemStack i : items) {
            addItem(i);
        }
    }

    @Override
    public void addItem(ItemStack item) {
        Preconditions.checkArgument(item != null && !item.getType().isAir(), "item is null or air");

        if (items == null) {
            items = new ArrayList<>();
        }

        items.add(item);
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaBundle) {
            CraftMetaBundle that = (CraftMetaBundle) meta;

            return (hasItems() ? that.hasItems() && this.items.equals(that.items) : !that.hasItems());
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaBundle || isBundleEmpty());
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();

        if (hasItems()) {
            hash = 61 * hash + items.hashCode();
        }

        return original != hash ? CraftMetaBundle.class.hashCode() ^ hash : hash;
    }

    @Override
    public CraftMetaBundle clone() {
        return (CraftMetaBundle) super.clone();
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);

        if (hasItems()) {
            builder.put(ITEMS.BUKKIT, items);
        }

        return builder;
    }
}
