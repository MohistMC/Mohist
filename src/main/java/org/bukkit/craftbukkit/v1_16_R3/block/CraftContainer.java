package org.bukkit.craftbukkit.v1_16_R3.block;

import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.world.LockCode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftChatMessage;

public abstract class CraftContainer<T extends LockableTileEntity> extends CraftBlockEntityState<T> implements Container {

    public CraftContainer(Block block, Class<T> tileEntityClass) {
        super(block, tileEntityClass);
    }

    public CraftContainer(final Material material, T tileEntity) {
        super(material, tileEntity);
    }

    @Override
    public boolean isLocked() {
        return !this.getSnapshot().lockKey.key.isEmpty();
    }

    @Override
    public String getLock() {
        return this.getSnapshot().lockKey.key;
    }

    @Override
    public void setLock(String key) {
        this.getSnapshot().lockKey = (key == null) ? LockCode.NO_LOCK : new LockCode(key);
    }

    // Paper start
    @Override
    public net.kyori.adventure.text.Component customName() {
        final T be = this.getSnapshot();
        return be.hasCustomName() ? io.papermc.paper.adventure.PaperAdventure.asAdventure(be.getCustomName()) : null;
    }

    @Override
    public void customName(final net.kyori.adventure.text.Component customName) {
        this.getSnapshot().setCustomName(customName != null ? io.papermc.paper.adventure.PaperAdventure.asVanilla(customName) : null);
    }
    // Paper end
    
    @Override
    public String getCustomName() {
        T container = this.getSnapshot();
        return container.name != null ? CraftChatMessage.fromComponent(container.getCustomName()) : null;
    }

    @Override
    public void setCustomName(String name) {
        this.getSnapshot().setCustomName(CraftChatMessage.fromStringOrNull(name));
    }

    @Override
    public void applyTo(T container) {
        super.applyTo(container);

        if (this.getSnapshot().name == null) {
            container.setCustomName(null);
        }
    }
}
