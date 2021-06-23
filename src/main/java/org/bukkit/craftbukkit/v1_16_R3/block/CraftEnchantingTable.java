package org.bukkit.craftbukkit.v1_16_R3.block;

import net.minecraft.tileentity.EnchantingTableTileEntity;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.EnchantingTable;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftChatMessage;

public class CraftEnchantingTable extends CraftBlockEntityState<EnchantingTableTileEntity> implements EnchantingTable {

    public CraftEnchantingTable(final Block block) {
        super(block, EnchantingTableTileEntity.class);
    }

    public CraftEnchantingTable(final Material material, final EnchantingTableTileEntity te) {
        super(material, te);
    }

    // Paper start
    @Override
    public net.kyori.adventure.text.Component customName() {
        final EnchantingTableTileEntity be = this.getSnapshot();
        return be.hasCustomName() ? io.papermc.paper.adventure.PaperAdventure.asAdventure(be.getCustomName()) : null;
    }

    @Override
    public void customName(final net.kyori.adventure.text.Component customName) {
        this.getSnapshot().setCustomName(customName != null ? io.papermc.paper.adventure.PaperAdventure.asVanilla(customName) : null);
    }
    // Paper end

    @Override
    public String getCustomName() {
        EnchantingTableTileEntity enchant = this.getSnapshot();
        return enchant.hasCustomName() ? CraftChatMessage.fromComponent(enchant.getCustomName()) : null;
    }

    @Override
    public void setCustomName(String name) {
        this.getSnapshot().setCustomName(CraftChatMessage.fromStringOrNull(name));
    }

    @Override
    public void applyTo(EnchantingTableTileEntity enchantingTable) {
        super.applyTo(enchantingTable);

        if (!this.getSnapshot().hasCustomName()) {
            enchantingTable.setCustomName(null);
        }
    }
}
