package org.bukkit.craftbukkit.v1_19_R1.block;

import net.minecraft.world.level.block.entity.JigsawBlockEntity;
import org.bukkit.World;
import org.bukkit.block.Jigsaw;

public class CraftJigsaw extends CraftBlockEntityState<JigsawBlockEntity> implements Jigsaw {

    public CraftJigsaw(World world, JigsawBlockEntity te) {
        super(world, te);
    }
}
