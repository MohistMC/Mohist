package org.bukkit.craftbukkit.v1_18_R1.tag;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagCollection;
import org.bukkit.Fluid;
import org.bukkit.craftbukkit.v1_18_R1.util.CraftMagicNumbers;

public class CraftFluidTag extends CraftTag<net.minecraft.world.level.material.Fluid, Fluid> {

    public CraftFluidTag(TagCollection<net.minecraft.world.level.material.Fluid> registry, ResourceLocation tag) {
        super(registry, tag);
    }

    @Override
    public boolean isTagged(Fluid fluid) {
        return getHandle().contains(CraftMagicNumbers.getFluid(fluid));
    }

    @Override
    public Set<Fluid> getValues() {
        return Collections.unmodifiableSet(getHandle().getValues().stream().map(CraftMagicNumbers::getFluid).collect(Collectors.toSet()));
    }
}
