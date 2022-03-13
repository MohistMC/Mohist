package org.bukkit.craftbukkit.v1_18_R2.block;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import org.bukkit.DyeColor;
import org.bukkit.World;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

public class CraftBanner extends CraftBlockEntityState<BannerBlockEntity> implements Banner {

    private DyeColor base;
    private List<Pattern> patterns;

    public CraftBanner(World world, final BannerBlockEntity tileEntity) {
        super(world, tileEntity);
    }

    @Override
    public void load(BannerBlockEntity banner) {
        super.load(banner);

        base = DyeColor.getByWoolData((byte) ((AbstractBannerBlock) this.data.getBlock()).getColor().getId());
        patterns = new ArrayList<Pattern>();

        if (banner.itemPatterns != null) {
            for (int i = 0; i < banner.itemPatterns.size(); i++) {
                CompoundTag p = (CompoundTag) banner.itemPatterns.get(i);
                patterns.add(new Pattern(DyeColor.getByWoolData((byte) p.getInt("Color")), PatternType.getByIdentifier(p.getString("Pattern"))));
            }
        }
    }

    @Override
    public DyeColor getBaseColor() {
        return this.base;
    }

    @Override
    public void setBaseColor(DyeColor color) {
        Preconditions.checkArgument(color != null, "color");
        this.base = color;
    }

    @Override
    public List<Pattern> getPatterns() {
        return new ArrayList<Pattern>(patterns);
    }

    @Override
    public void setPatterns(List<Pattern> patterns) {
        this.patterns = new ArrayList<Pattern>(patterns);
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
    public void applyTo(BannerBlockEntity banner) {
        super.applyTo(banner);

        banner.baseColor = net.minecraft.world.item.DyeColor.byId(base.getWoolData());

        ListTag newPatterns = new ListTag();

        for (Pattern p : patterns) {
            CompoundTag compound = new CompoundTag();
            compound.putInt("Color", p.getColor().getWoolData());
            compound.putString("Pattern", p.getPattern().getIdentifier());
            newPatterns.add(compound);
        }
        banner.itemPatterns = newPatterns;
    }
}
