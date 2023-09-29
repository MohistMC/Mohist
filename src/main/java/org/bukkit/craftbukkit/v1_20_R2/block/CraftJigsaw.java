package org.bukkit.craftbukkit.v1_20_R2.block;

import net.minecraft.world.level.block.entity.JigsawBlockEntity;
import org.bukkit.World;
import org.bukkit.block.Jigsaw;

public class CraftJigsaw extends CraftBlockEntityState<JigsawBlockEntity> implements Jigsaw {

    public CraftJigsaw(World world, JigsawBlockEntity te) {
        super(world, te);
    }

    protected CraftJigsaw(CraftJigsaw state) {
        super(state);
    }

    @Override
    public CraftJigsaw copy() {
        return new CraftJigsaw(this);
    }
}
