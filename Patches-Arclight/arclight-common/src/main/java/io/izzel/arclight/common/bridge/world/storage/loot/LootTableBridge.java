package io.izzel.arclight.common.bridge.world.storage.loot;

import net.minecraft.inventory.IInventory;
import net.minecraft.world.storage.loot.LootContext;

public interface LootTableBridge {

    void bridge$fillInventory(IInventory inv, LootContext context, boolean plugin);
}
