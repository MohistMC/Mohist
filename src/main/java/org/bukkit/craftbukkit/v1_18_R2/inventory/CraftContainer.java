package org.bukkit.craftbukkit.v1_18_R2.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.BeaconMenu;
import net.minecraft.world.inventory.BlastFurnaceMenu;
import net.minecraft.world.inventory.BrewingStandMenu;
import net.minecraft.world.inventory.CartographyTableMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.inventory.FurnaceMenu;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.inventory.HopperMenu;
import net.minecraft.world.inventory.LecternMenu;
import net.minecraft.world.inventory.LoomMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.inventory.ShulkerBoxMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.inventory.SmokerMenu;
import net.minecraft.world.inventory.StonecutterMenu;
import net.minecraft.world.item.ItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public class CraftContainer extends AbstractContainerMenu {

    private final InventoryView view;
    private InventoryType cachedType;
    private AbstractContainerMenu delegate;

    public CraftContainer(InventoryView view, net.minecraft.world.entity.player.Player player, int id) {
        super(getNotchInventoryType(view.getTopInventory()), id);
        this.view = view;
        // TODO: Do we need to check that it really is a CraftInventory?
        Container top = ((CraftInventory) view.getTopInventory()).getInventory();
        net.minecraft.world.entity.player.Inventory bottom = (net.minecraft.world.entity.player.Inventory) ((CraftInventory) view.getBottomInventory()).getInventory();
        cachedType = view.getType();
        setupSlots(top, bottom, player);
    }

    public CraftContainer(final Inventory inventory, final net.minecraft.world.entity.player.Player player, int id) {
        this(new InventoryView() {
            @Override
            public Inventory getTopInventory() {
                return inventory;
            }

            @Override
            public Inventory getBottomInventory() {
                return getPlayer().getInventory();
            }

            @Override
            public HumanEntity getPlayer() {
                return player.getBukkitEntity();
            }

            @Override
            public InventoryType getType() {
                return inventory.getType();
            }

            @Override
            public String getTitle() {
                return inventory instanceof CraftInventoryCustom ? ((CraftInventoryCustom.MinecraftInventory) ((CraftInventory) inventory).getInventory()).getTitle() : inventory.getType().getDefaultTitle();
            }
        }, player, id);
    }

    @Override
    public InventoryView getBukkitView() {
        return view;
    }

    public static MenuType getNotchInventoryType(Inventory inventory) {
        switch (inventory.getType()) {
            case PLAYER:
            case CHEST:
            case ENDER_CHEST:
            case BARREL:
                switch (inventory.getSize()) {
                    case 9:
                        return MenuType.GENERIC_9x1;
                    case 18:
                        return MenuType.GENERIC_9x2;
                    case 27:
                        return MenuType.GENERIC_9x3;
                    case 36:
                    case 41: // PLAYER
                        return MenuType.GENERIC_9x4;
                    case 45:
                        return MenuType.GENERIC_9x5;
                    case 54:
                        return MenuType.GENERIC_9x6;
                    default:
                        throw new IllegalArgumentException("Unsupported custom inventory size " + inventory.getSize());
                }
            case WORKBENCH:
                return MenuType.CRAFTING;
            case FURNACE:
                return MenuType.FURNACE;
            case DISPENSER:
                return MenuType.GENERIC_3x3;
            case ENCHANTING:
                return MenuType.ENCHANTMENT;
            case BREWING:
                return MenuType.BREWING_STAND;
            case BEACON:
                return MenuType.BEACON;
            case ANVIL:
                return MenuType.ANVIL;
            case SMITHING:
                return MenuType.SMITHING;
            case HOPPER:
                return MenuType.HOPPER;
            case DROPPER:
                return MenuType.GENERIC_3x3;
            case SHULKER_BOX:
                return MenuType.SHULKER_BOX;
            case BLAST_FURNACE:
                return MenuType.BLAST_FURNACE;
            case LECTERN:
                return MenuType.LECTERN;
            case SMOKER:
                return MenuType.SMOKER;
            case LOOM:
                return MenuType.LOOM;
            case CARTOGRAPHY:
                return MenuType.CARTOGRAPHY_TABLE;
            case GRINDSTONE:
                return MenuType.GRINDSTONE;
            case STONECUTTER:
                return MenuType.STONECUTTER;
            case CREATIVE:
            case CRAFTING:
            case MERCHANT:
                throw new IllegalArgumentException("Can't open a " + inventory.getType() + " inventory!");
            default:
                // TODO: If it reaches the default case, should we throw an error?
                return MenuType.GENERIC_9x3;
        }
    }

    private void setupSlots(Container top, net.minecraft.world.entity.player.Inventory bottom, net.minecraft.world.entity.player.Player entityhuman) {
        int windowId = -1;
        switch (cachedType) {
            case CREATIVE:
                break; // TODO: This should be an error?
            case PLAYER:
            case CHEST:
            case ENDER_CHEST:
            case BARREL:
                delegate = new ChestMenu(MenuType.GENERIC_9x3, windowId, bottom, top, top.getContainerSize() / 9);
                break;
            case DISPENSER:
            case DROPPER:
                delegate = new DispenserMenu(windowId, bottom, top);
                break;
            case FURNACE:
                delegate = new FurnaceMenu(windowId, bottom, top, new SimpleContainerData(4));
                break;
            case CRAFTING: // TODO: This should be an error?
            case WORKBENCH:
                setupWorkbench(top, bottom); // SPIGOT-3812 - manually set up slots so we can use the delegated inventory and not the automatically created one
                break;
            case ENCHANTING:
                delegate = new EnchantmentMenu(windowId, bottom);
                break;
            case BREWING:
                delegate = new BrewingStandMenu(windowId, bottom, top, new SimpleContainerData(2));
                break;
            case HOPPER:
                delegate = new HopperMenu(windowId, bottom, top);
                break;
            case ANVIL:
                setupAnvil(top, bottom); // SPIGOT-6783 - manually set up slots so we can use the delegated inventory and not the automatically created one
                break;
            case SMITHING:
                delegate = new SmithingMenu(windowId, bottom);
                break;
            case BEACON:
                delegate = new BeaconMenu(windowId, bottom);
                break;
            case SHULKER_BOX:
                delegate = new ShulkerBoxMenu(windowId, bottom, top);
                break;
            case BLAST_FURNACE:
                delegate = new BlastFurnaceMenu(windowId, bottom, top, new SimpleContainerData(4));
                break;
            case LECTERN:
                delegate = new LecternMenu(windowId, top, new SimpleContainerData(1), bottom);
                break;
            case SMOKER:
                delegate = new SmokerMenu(windowId, bottom, top, new SimpleContainerData(4));
                break;
            case LOOM:
                delegate = new LoomMenu(windowId, bottom);
                break;
            case CARTOGRAPHY:
                delegate = new CartographyTableMenu(windowId, bottom);
                break;
            case GRINDSTONE:
                delegate = new GrindstoneMenu(windowId, bottom);
                break;
            case STONECUTTER:
                delegate = new StonecutterMenu(windowId, bottom);
                break;
            case MERCHANT:
                delegate = new MerchantMenu(windowId, bottom);
                break;
        }

        if (delegate != null) {
            this.lastSlots = delegate.lastSlots;
            this.slots = delegate.slots;
            this.remoteSlots = delegate.remoteSlots;
        }

        // SPIGOT-4598 - we should still delegate the shift click handler
        switch (cachedType) {
            case WORKBENCH:
                delegate = new CraftingMenu(windowId, bottom);
                break;
            case ANVIL:
                delegate = new AnvilMenu(windowId, bottom);
                break;
        }
    }

    private void setupWorkbench(Container top, Container bottom) {
        // This code copied from ContainerWorkbench
        this.addSlot(new Slot(top, 0, 124, 35));

        int row;
        int col;

        for (row = 0; row < 3; ++row) {
            for (col = 0; col < 3; ++col) {
                this.addSlot(new Slot(top, 1 + col + row * 3, 30 + col * 18, 17 + row * 18));
            }
        }

        for (row = 0; row < 3; ++row) {
            for (col = 0; col < 9; ++col) {
                this.addSlot(new Slot(bottom, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        for (col = 0; col < 9; ++col) {
            this.addSlot(new Slot(bottom, col, 8 + col * 18, 142));
        }
        // End copy from ContainerWorkbench
    }

    private void setupAnvil(net.minecraft.world.Container top, net.minecraft.world.Container bottom) {
        // This code copied from ContainerAnvilAbstract
        this.addSlot(new Slot(top, 0, 27, 47));
        this.addSlot(new Slot(top, 1, 76, 47));
        this.addSlot(new Slot(top, 2, 134, 47));

        int row;
        int col;

        for (row = 0; row < 3; ++row) {
            for (col = 0; col < 9; ++col) {
                this.addSlot(new Slot(bottom, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        for (row = 0; row < 9; ++row) {
            this.addSlot(new Slot(bottom, row, 8 + row * 18, 142));
        }
        // End copy from ContainerAnvilAbstract
    }

    @Override
    public ItemStack quickMoveStack(net.minecraft.world.entity.player.Player entityhuman, int i) {
        return (delegate != null) ? delegate.quickMoveStack(entityhuman, i) : super.quickMoveStack(entityhuman, i);
    }

    @Override
    public boolean stillValid(net.minecraft.world.entity.player.Player entity) {
        return true;
    }

    @Override
    public MenuType<?> getType() {
        return getNotchInventoryType(view.getTopInventory());
    }
}
