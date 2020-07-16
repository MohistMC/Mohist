package org.bukkit.craftbukkit.block;

import net.minecraft.block.entity.DaylightDetectorBlockEntity;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.DaylightDetector;

public class CraftDaylightDetector extends CraftBlockEntityState<DaylightDetectorBlockEntity> implements DaylightDetector {

    public CraftDaylightDetector(final Block block) {
        super(block, DaylightDetectorBlockEntity.class);
    }

    public CraftDaylightDetector(final Material material, final DaylightDetectorBlockEntity te) {
        super(material, te);
    }
}
