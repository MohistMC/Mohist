package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.SmokerBlockEntity;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Smoker;

public class CraftSmoker extends CraftFurnace<SmokerBlockEntity> implements Smoker {

    public CraftSmoker(Block block) {
        super(block, SmokerBlockEntity.class);
    }

    public CraftSmoker(Material material, SmokerBlockEntity te) {
        super(material, te);
    }
}
