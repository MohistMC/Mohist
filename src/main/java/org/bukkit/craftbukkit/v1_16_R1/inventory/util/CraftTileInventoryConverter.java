package org.bukkit.craftbukkit.v1_16_R1.inventory.util;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlastFurnaceBlockEntity;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.block.entity.DropperBlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.entity.SmokerBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.bukkit.craftbukkit.v1_16_R1.CraftServer;
import org.bukkit.craftbukkit.v1_16_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_16_R1.inventory.CraftInventoryBrewer;
import org.bukkit.craftbukkit.v1_16_R1.inventory.CraftInventoryFurnace;
import org.bukkit.craftbukkit.v1_16_R1.util.CraftChatMessage;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class CraftTileInventoryConverter implements CraftInventoryCreator.InventoryConverter {

    public abstract net.minecraft.inventory.Inventory getTileEntity();

    @Override
    public Inventory createInventory(InventoryHolder holder, InventoryType type) {
        return getInventory(getTileEntity());
    }

    @Override
    public Inventory createInventory(InventoryHolder holder, InventoryType type, String title) {
        net.minecraft.inventory.Inventory inventory = getTileEntity();
        if (inventory instanceof LootableContainerBlockEntity)
            ((LootableContainerBlockEntity) inventory).setCustomName(CraftChatMessage.fromStringOrNull(title));
        return getInventory(inventory);
    }

    public Inventory getInventory(net.minecraft.inventory.Inventory tileEntity) {
        return new CraftInventory(tileEntity);
    }

    public static class Furnace extends CraftTileInventoryConverter {

        @Override
        public net.minecraft.inventory.Inventory getTileEntity() {
            AbstractFurnaceBlockEntity furnace = new FurnaceBlockEntity();
            furnace.setLocation(CraftServer.server.getWorld(World.OVERWORLD), BlockPos.ORIGIN);
            return furnace;
        }

        @Override
        public Inventory createInventory(InventoryHolder owner, InventoryType type, String title) {
            net.minecraft.inventory.Inventory tileEntity = getTileEntity();
            ((AbstractFurnaceBlockEntity) tileEntity).setCustomName(CraftChatMessage.fromStringOrNull(title));
            return getInventory(tileEntity);
        }

        @Override
        public Inventory getInventory(net.minecraft.inventory.Inventory tileEntity) {
            return new CraftInventoryFurnace((AbstractFurnaceBlockEntity) tileEntity);
        }
    }

    public static class BrewingStand extends CraftTileInventoryConverter {

        @Override
        public net.minecraft.inventory.Inventory getTileEntity() {
            return new BrewingStandBlockEntity();
        }

        @Override
        public Inventory createInventory(InventoryHolder holder, InventoryType type, String title) {
            net.minecraft.inventory.Inventory tileEntity = getTileEntity();
            if (tileEntity instanceof BrewingStandBlockEntity)
                ((BrewingStandBlockEntity) tileEntity).setCustomName(CraftChatMessage.fromStringOrNull(title));

            return getInventory(tileEntity);
        }

        @Override
        public Inventory getInventory(net.minecraft.inventory.Inventory tileEntity) {
            return new CraftInventoryBrewer(tileEntity);
        }
    }

    public static class Dispenser extends CraftTileInventoryConverter {

        @Override
        public net.minecraft.inventory.Inventory getTileEntity() {
            return new DispenserBlockEntity();
        }
    }

    public static class Dropper extends CraftTileInventoryConverter {

        @Override
        public net.minecraft.inventory.Inventory getTileEntity() {
            return new DropperBlockEntity();
        }
    }

    public static class Hopper extends CraftTileInventoryConverter {

        @Override
        public net.minecraft.inventory.Inventory getTileEntity() {
            return new HopperBlockEntity();
        }
    }

    public static class BlastFurnace extends CraftTileInventoryConverter {

        @Override
        public net.minecraft.inventory.Inventory getTileEntity() {
            return new BlastFurnaceBlockEntity();
        }
    }

    public static class Lectern extends CraftTileInventoryConverter {

        @Override
        public net.minecraft.inventory.Inventory getTileEntity() {
            return new LecternBlockEntity().inventory;
        }
    }

    public static class Smoker extends CraftTileInventoryConverter {

        @Override
        public net.minecraft.inventory.Inventory getTileEntity() {
            return new SmokerBlockEntity();
        }
    }

}