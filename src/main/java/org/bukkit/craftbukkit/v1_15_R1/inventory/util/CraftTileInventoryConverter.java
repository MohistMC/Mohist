package org.bukkit.craftbukkit.v1_15_R1.inventory.util;

import net.minecraft.inventory.IInventory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.BlastFurnaceTileEntity;
import net.minecraft.tileentity.BrewingStandTileEntity;
import net.minecraft.tileentity.DispenserTileEntity;
import net.minecraft.tileentity.DropperTileEntity;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.tileentity.LecternTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.SmokerTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftInventoryBrewer;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftInventoryFurnace;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftChatMessage;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class CraftTileInventoryConverter implements CraftInventoryCreator.InventoryConverter {

    public abstract IInventory getTileEntity();

    @Override
    public Inventory createInventory(InventoryHolder holder, InventoryType type) {
        return getInventory(getTileEntity());
    }

    @Override
    public Inventory createInventory(InventoryHolder holder, InventoryType type, String title) {
        IInventory te = getTileEntity();
        if (te instanceof LockableLootTileEntity) {
            ((LockableLootTileEntity) te).setCustomName(CraftChatMessage.fromStringOrNull(title));
        }

        return getInventory(te);
    }

    public Inventory getInventory(IInventory tileEntity) {
        return new CraftInventory(tileEntity);
    }

    public static class Furnace extends CraftTileInventoryConverter {

        @Override
        public IInventory getTileEntity() {
            AbstractFurnaceTileEntity furnace = new FurnaceTileEntity();
            furnace.setWorldAndPos(MinecraftServer.getServer().getWorld(DimensionType.OVERWORLD), BlockPos.ZERO); // TODO: customize this if required
            return furnace;
        }

        @Override
        public Inventory createInventory(InventoryHolder owner, InventoryType type, String title) {
            IInventory tileEntity = getTileEntity();
            ((AbstractFurnaceTileEntity) tileEntity).setCustomName(CraftChatMessage.fromStringOrNull(title));
            return getInventory(tileEntity);
        }

        @Override
        public Inventory getInventory(IInventory tileEntity) {
            return new CraftInventoryFurnace((AbstractFurnaceTileEntity) tileEntity);
        }
    }

    public static class BrewingStand extends CraftTileInventoryConverter {

        @Override
        public IInventory getTileEntity() {
            return new BrewingStandTileEntity();
        }

        @Override
        public Inventory createInventory(InventoryHolder holder, InventoryType type, String title) {
            // BrewingStand does not extend TileEntityLootable
            IInventory tileEntity = getTileEntity();
            if (tileEntity instanceof BrewingStandTileEntity) {
                ((BrewingStandTileEntity) tileEntity).setCustomName(CraftChatMessage.fromStringOrNull(title));
            }
            return getInventory(tileEntity);
        }

        @Override
        public Inventory getInventory(IInventory tileEntity) {
            return new CraftInventoryBrewer(tileEntity);
        }
    }

    public static class Dispenser extends CraftTileInventoryConverter {

        @Override
        public IInventory getTileEntity() {
            return new DispenserTileEntity();
        }
    }

    public static class Dropper extends CraftTileInventoryConverter {

        @Override
        public IInventory getTileEntity() {
            return new DropperTileEntity();
        }
    }

    public static class Hopper extends CraftTileInventoryConverter {

        @Override
        public IInventory getTileEntity() {
            return new HopperTileEntity();
        }
    }

    public static class BlastFurnace extends CraftTileInventoryConverter {

        @Override
        public IInventory getTileEntity() {
            return new BlastFurnaceTileEntity();
        }
    }

    public static class Lectern extends CraftTileInventoryConverter {

        @Override
        public IInventory getTileEntity() {
            return new LecternTileEntity().inventory;
        }
    }

    public static class Smoker extends CraftTileInventoryConverter {

        @Override
        public IInventory getTileEntity() {
            return new SmokerTileEntity();
        }
    }
}
