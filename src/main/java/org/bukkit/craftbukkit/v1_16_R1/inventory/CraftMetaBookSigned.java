package org.bukkit.craftbukkit.v1_16_R1.inventory;

import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.text.Text;
import net.minecraft.text.Text.Serializer;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.v1_16_R1.inventory.CraftMetaItem.SerializableMeta;
import org.bukkit.craftbukkit.v1_16_R1.util.CraftMagicNumbers;
import org.bukkit.inventory.meta.BookMeta;

@DelegateDeserialization(SerializableMeta.class)
class CraftMetaBookSigned extends CraftMetaBook implements BookMeta {

    CraftMetaBookSigned(CraftMetaItem meta) {
        super(meta);
    }

    CraftMetaBookSigned(CompoundTag tag) {
        super(tag, false);

        boolean resolved = true;
        if (tag.contains(RESOLVED.NBT))
            resolved = tag.getBoolean(RESOLVED.NBT);

        if (tag.contains(BOOK_PAGES.NBT)) {
            ListTag pages = tag.getList(BOOK_PAGES.NBT, CraftMagicNumbers.NBT.TAG_STRING);

            for (int i = 0; i < Math.min(pages.size(), MAX_PAGES); i++) {
                String page = pages.getString(i);
                if (resolved) {
                    try {
                        this.pages.add(Serializer.fromJson(page));
                        continue;
                    } catch (Exception e) {/*Ignore and treat as an old book*/}
                }
                addPage(page);
            }
        }
    }

    CraftMetaBookSigned(Map<String, Object> map) {
        super(map);
    }

    @Override
    void applyToItem(CompoundTag itemData) {
        super.applyToItem(itemData, false);

        if (hasTitle())
            itemData.putString(BOOK_TITLE.NBT, this.title);

        if (hasAuthor())
            itemData.putString(BOOK_AUTHOR.NBT, this.author);

        if (hasPages()) {
            ListTag list = new ListTag();
            for (Text page : pages)
                list.add(StringTag.of(Serializer.toJson(page)));

            itemData.put(BOOK_PAGES.NBT, list);
        }
        itemData.putBoolean(RESOLVED.NBT, true);

        if (generation != null)
            itemData.putInt(GENERATION.NBT, generation);
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    boolean applicableTo(Material type) {
        switch (type) {
        case WRITTEN_BOOK:
        case WRITABLE_BOOK:
            return true;
        default:
            return false;
        }
    }

    @Override
    public CraftMetaBookSigned clone() {
        CraftMetaBookSigned meta = (CraftMetaBookSigned) super.clone();
        return meta;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        return original != hash ? CraftMetaBookSigned.class.hashCode() ^ hash : hash;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        return super.equalsCommon(meta);
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaBookSigned || isBookEmpty());
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);
        return builder;
    }

}