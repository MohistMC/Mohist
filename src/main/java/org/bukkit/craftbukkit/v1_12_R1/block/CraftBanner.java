package org.bukkit.craftbukkit.v1_12_R1.block;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityBanner;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

import java.util.ArrayList;
import java.util.List;

public class CraftBanner extends CraftBlockEntityState<TileEntityBanner> implements Banner {

    private DyeColor base;
    private List<Pattern> patterns;

    public CraftBanner(final Block block) {
        super(block, TileEntityBanner.class);
    }

    public CraftBanner(final Material material, final TileEntityBanner te) {
        super(material, te);
    }

    @Override
    public void load(TileEntityBanner banner) {
        super.load(banner);

        base = DyeColor.getByDyeData((byte) banner.baseColor.getDyeDamage());
        patterns = new ArrayList<>();

        if (banner.patterns != null) {
            for (int i = 0; i < banner.patterns.tagCount(); i++) {
                NBTTagCompound p = (NBTTagCompound) banner.patterns.get(i);
                patterns.add(new Pattern(DyeColor.getByDyeData((byte) p.getInteger("Color")), PatternType.getByIdentifier(p.getString("Pattern"))));
            }
        }
    }

    @Override
    public DyeColor getBaseColor() {
        return this.base;
    }

    @Override
    public void setBaseColor(DyeColor color) {
        this.base = color;
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
        this.patterns.add(pattern);
    }

    @Override
    public Pattern getPattern(int i) {
        return this.patterns.get(i);
    }

    @Override
    public Pattern removePattern(int i) {
        return this.patterns.remove(i);
    }

    @Override
    public void setPattern(int i, Pattern pattern) {
        this.patterns.set(i, pattern);
    }

    @Override
    public int numberOfPatterns() {
        return patterns.size();
    }

    @Override
    public void applyTo(TileEntityBanner banner) {
        super.applyTo(banner);

        banner.baseColor = EnumDyeColor.byDyeDamage(base.getDyeData());

        NBTTagList newPatterns = new NBTTagList();

        for (Pattern p : patterns) {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setInteger("Color", p.getColor().getDyeData());
            compound.setString("Pattern", p.getPattern().getIdentifier());
            newPatterns.appendTag(compound);
        }
        banner.patterns = newPatterns;
    }
}
