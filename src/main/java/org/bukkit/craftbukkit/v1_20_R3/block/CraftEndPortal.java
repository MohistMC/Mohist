package org.bukkit.craftbukkit.v1_20_R3.block;

import net.minecraft.world.level.block.entity.TheEndPortalBlockEntity;
import org.bukkit.World;

public class CraftEndPortal extends CraftBlockEntityState<TheEndPortalBlockEntity> {

    public CraftEndPortal(World world, TheEndPortalBlockEntity tileEntity) {
        super(world, tileEntity);
    }

    protected CraftEndPortal(CraftEndPortal state) {
        super(state);
    }

    @Override
    public CraftEndPortal copy() {
        return new CraftEndPortal(this);
    }
}
