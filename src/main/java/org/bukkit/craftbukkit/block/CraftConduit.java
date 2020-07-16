package org.bukkit.craftbukkit.block;

import net.minecraft.block.entity.ConduitBlockEntity;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Conduit;

public class CraftConduit extends CraftBlockEntityState<ConduitBlockEntity> implements Conduit {

    public CraftConduit(Block block) {
        super(block, ConduitBlockEntity.class);
    }

    public CraftConduit(Material material, ConduitBlockEntity te) {
        super(material, te);
    }
}
