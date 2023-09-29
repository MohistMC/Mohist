package org.bukkit.craftbukkit.v1_20_R2.block;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.SculkShriekerBlockEntity;
import org.bukkit.World;
import org.bukkit.block.SculkShrieker;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class CraftSculkShrieker extends CraftBlockEntityState<SculkShriekerBlockEntity> implements SculkShrieker {

    public CraftSculkShrieker(World world, SculkShriekerBlockEntity tileEntity) {
        super(world, tileEntity);
    }

    protected CraftSculkShrieker(CraftSculkShrieker state) {
        super(state);
    }

    @Override
    public int getWarningLevel() {
        return getSnapshot().warningLevel;
    }

    @Override
    public void setWarningLevel(int level) {
        getSnapshot().warningLevel = level;
    }

    @Override
    public void tryShriek(Player player) {
        requirePlaced();

        ServerPlayer entityPlayer = (player == null) ? null : ((CraftPlayer) player).getHandle();
        getTileEntity().tryShriek(world.getHandle(), entityPlayer);
    }

    @Override
    public CraftSculkShrieker copy() {
        return new CraftSculkShrieker(this);
    }
}
