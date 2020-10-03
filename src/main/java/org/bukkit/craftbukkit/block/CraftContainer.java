package org.bukkit.craftbukkit.block;

import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.world.LockCode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.craftbukkit.util.CraftChatMessage;

public abstract class CraftContainer<T extends LockableTileEntity> extends CraftBlockEntityState<T> implements Container {

    public CraftContainer(Block block, Class<T> tileEntityClass) {
        super(block, tileEntityClass);
    }

    public CraftContainer(final Material material, T tileEntity) {
        super(material, tileEntity);
    }

    @Override
    public boolean isLocked() {
        return !this.getSnapshot().code.lock.isEmpty();
    }

    @Override
    public String getLock() {
        return this.getSnapshot().code.lock;
    }

    @Override
    public void setLock(String key) {
        this.getSnapshot().code = (key == null) ? LockCode.EMPTY_CODE : new LockCode(key);
    }

    @Override
    public String getCustomName() {
        T container = this.getSnapshot();
        return container.customName != null ? CraftChatMessage.fromComponent(container.getCustomName()) : null;
    }

    @Override
    public void setCustomName(String name) {
        this.getSnapshot().setCustomName(CraftChatMessage.fromStringOrNull(name));
    }

    @Override
    public void applyTo(T container) {
        super.applyTo(container);

        if (this.getSnapshot().customName == null) {
            container.setCustomName(null);
        }
    }
}
