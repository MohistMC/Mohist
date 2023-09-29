package org.bukkit.craftbukkit.v1_20_R2.block;

import net.minecraft.world.level.block.entity.EnchantmentTableBlockEntity;
import org.bukkit.World;
import org.bukkit.block.EnchantingTable;
import org.bukkit.craftbukkit.v1_20_R2.util.CraftChatMessage;

public class CraftEnchantingTable extends CraftBlockEntityState<EnchantmentTableBlockEntity> implements EnchantingTable {

    public CraftEnchantingTable(World world, final EnchantmentTableBlockEntity te) {
        super(world, te);
    }

    protected CraftEnchantingTable(CraftEnchantingTable state) {
        super(state);
    }

    @Override
    public String getCustomName() {
        EnchantmentTableBlockEntity enchant = this.getSnapshot();
        return enchant.hasCustomName() ? CraftChatMessage.fromComponent(enchant.getCustomName()) : null;
    }

    @Override
    public void setCustomName(String name) {
        this.getSnapshot().setCustomName(CraftChatMessage.fromStringOrNull(name));
    }

    @Override
    public void applyTo(EnchantmentTableBlockEntity enchantingTable) {
        super.applyTo(enchantingTable);

        if (!this.getSnapshot().hasCustomName()) {
            enchantingTable.setCustomName(null);
        }
    }

    @Override
    public CraftEnchantingTable copy() {
        return new CraftEnchantingTable(this);
    }
}
