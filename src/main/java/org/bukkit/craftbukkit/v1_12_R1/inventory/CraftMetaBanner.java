package org.bukkit.craftbukkit.v1_12_R1.inventory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@DelegateDeserialization(CraftMetaItem.SerializableMeta.class)
public class CraftMetaBanner extends CraftMetaItem implements BannerMeta {

    static final ItemMetaKey BASE = new ItemMetaKey("Base", "base-color");
    static final ItemMetaKey PATTERNS = new ItemMetaKey("Patterns", "patterns");
    static final ItemMetaKey COLOR = new ItemMetaKey("Color", "color");
    static final ItemMetaKey PATTERN = new ItemMetaKey("Pattern", "pattern");

    private DyeColor base;
    private List<Pattern> patterns = new ArrayList<>();

    CraftMetaBanner(CraftMetaItem meta) {
        super(meta);

        if (!(meta instanceof CraftMetaBanner)) {
            return;
        }

        CraftMetaBanner banner = (CraftMetaBanner) meta;
        base = banner.base;
        patterns = new ArrayList<>(banner.patterns);
    }

    CraftMetaBanner(NBTTagCompound tag) {
        super(tag);

        if (!tag.hasKey("BlockEntityTag")) {
            return;
        }

        NBTTagCompound entityTag = tag.getCompoundTag("BlockEntityTag");

        base = entityTag.hasKey(BASE.NBT) ? DyeColor.getByDyeData((byte) entityTag.getInteger(BASE.NBT)) : null;

        if (entityTag.hasKey(PATTERNS.NBT)) {
            NBTTagList patterns = entityTag.getTagList(PATTERNS.NBT, CraftMagicNumbers.NBT.TAG_COMPOUND);
            for (int i = 0; i < Math.min(patterns.tagCount(), 20); i++) {
                NBTTagCompound p = patterns.getCompoundTagAt(i);
                this.patterns.add(new Pattern(DyeColor.getByDyeData((byte) p.getInteger(COLOR.NBT)), PatternType.getByIdentifier(p.getString(PATTERN.NBT))));
            }
        }
    }

    CraftMetaBanner(Map<String, Object> map) {
        super(map);

        String baseStr = SerializableMeta.getString(map, BASE.BUKKIT, true);
        if (baseStr != null) {
            base = DyeColor.valueOf(baseStr);
        }

        Iterable<?> rawPatternList = SerializableMeta.getObject(Iterable.class, map, PATTERNS.BUKKIT, true);
        if (rawPatternList == null) {
            return;
        }

        for (Object obj : rawPatternList) {
            if (!(obj instanceof Pattern)) {
                throw new IllegalArgumentException("Object in pattern list is not valid. " + obj.getClass());
            }
            addPattern((Pattern) obj);
        }
    }

    @Override
    void applyToItem(NBTTagCompound tag) {
        super.applyToItem(tag);

        NBTTagCompound entityTag = new NBTTagCompound();
        if (base != null) {
            entityTag.setInteger(BASE.NBT, base.getDyeData());
        }

        NBTTagList newPatterns = new NBTTagList();

        for (Pattern p : patterns) {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setInteger(COLOR.NBT, p.getColor().getDyeData());
            compound.setString(PATTERN.NBT, p.getPattern().getIdentifier());
            newPatterns.appendTag(compound);
        }
        entityTag.setTag(PATTERNS.NBT, newPatterns);

        tag.setTag("BlockEntityTag", entityTag);
    }

    @Override
    public DyeColor getBaseColor() {
        return base;
    }

    @Override
    public void setBaseColor(DyeColor color) {
        base = color;
    }

    @Override
    public List<Pattern> getPatterns() {
        return new ArrayList<>(patterns);
    }

    @Override
    public void setPatterns(List<Pattern> patterns) {
        this.patterns = new ArrayList<>(patterns);
    }

    @Override
    public void addPattern(Pattern pattern) {
        patterns.add(pattern);
    }

    @Override
    public Pattern getPattern(int i) {
        return patterns.get(i);
    }

    @Override
    public Pattern removePattern(int i) {
        return patterns.remove(i);
    }

    @Override
    public void setPattern(int i, Pattern pattern) {
        patterns.set(i, pattern);
    }

    @Override
    public int numberOfPatterns() {
        return patterns.size();
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);

        if (base != null) {
            builder.put(BASE.BUKKIT, base.toString());
        }

        if (!patterns.isEmpty()) {
            builder.put(PATTERNS.BUKKIT, ImmutableList.copyOf(patterns));
        }

        return builder;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (base != null) {
            hash = 31 * hash + base.hashCode();
        }
        if (!patterns.isEmpty()) {
            hash = 31 * hash + patterns.hashCode();
        }
        return original != hash ? CraftMetaBanner.class.hashCode() ^ hash : hash;
    }

    @Override
    public boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaBanner) {
            CraftMetaBanner that = (CraftMetaBanner) meta;

            return base == that.base && patterns.equals(that.patterns);
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaBanner || (patterns.isEmpty() && base == null));
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && patterns.isEmpty() && base == null;
    }

    @Override
    boolean applicableTo(Material type) {
        return type == Material.BANNER;
    }

    @Override
    public CraftMetaBanner clone() {
        CraftMetaBanner meta = (CraftMetaBanner) super.clone();
        meta.patterns = new ArrayList<>(patterns);
        return meta;
    }
}
