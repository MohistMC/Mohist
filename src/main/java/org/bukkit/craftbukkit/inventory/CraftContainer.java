package org.bukkit.craftbukkit.inventory;

import net.minecraft.container.AnvilContainer;
import net.minecraft.container.ArrayPropertyDelegate;
import net.minecraft.container.BeaconContainer;
import net.minecraft.container.BlastFurnaceContainer;
import net.minecraft.container.BrewingStandContainer;
import net.minecraft.container.CartographyTableContainer;
import net.minecraft.container.Container;
import net.minecraft.container.ContainerType;
import net.minecraft.container.CraftingTableContainer;
import net.minecraft.container.EnchantingTableContainer;
import net.minecraft.container.FurnaceContainer;
import net.minecraft.container.Generic3x3Container;
import net.minecraft.container.GenericContainer;
import net.minecraft.container.GrindstoneContainer;
import net.minecraft.container.HopperContainer;
import net.minecraft.container.LecternContainer;
import net.minecraft.container.LoomContainer;
import net.minecraft.container.MerchantContainer;
import net.minecraft.container.ShulkerBoxContainer;
import net.minecraft.container.Slot;
import net.minecraft.container.SmokerContainer;
import net.minecraft.container.StonecutterContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.OpenContainerS2CPacket;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public class CraftContainer extends Container {

    private final InventoryView view;
    private InventoryType cachedType;
    private String cachedTitle;
    private Container delegate;
    private final int cachedSize;

    public CraftContainer(InventoryView view, PlayerEntity player, int id) {
        super(getNotchInventoryType(view.getTopInventory()), id);
        this.view = view;
        // TODO: Do we need to check that it really is a CraftInventory?
        net.minecraft.inventory.Inventory top = ((CraftInventory) view.getTopInventory()).getInventory();
        PlayerInventory bottom = (PlayerInventory) ((CraftInventory) view.getBottomInventory()).getInventory();
        cachedType = view.getType();
        cachedTitle = view.getTitle();
        cachedSize = getSize();
        setupSlots(top, bottom, player);
    }

    public CraftContainer(final Inventory inventory, final PlayerEntity player, int id) {
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
    public void transferTo(Container var0, CraftHumanEntity var1) {
        
    }

    @Override
    public InventoryView getBukkitView() {
        return view;
    }

    @Override
    public Text getTitle() {
        return null;
    }

    @Override
    public void setTitle(Text var0) {

    }

    private int getSize() {
        return view.getTopInventory().getSize();
    }

    @Override
    public boolean isNotRestricted(PlayerEntity entityhuman) {
        if (cachedType == view.getType() && cachedSize == getSize() && cachedTitle.equals(view.getTitle())) {
            return true;
        }
        // If the window type has changed for some reason, update the player
        // This method will be called every tick or something, so it's
        // as good a place as any to put something like this.
        boolean typeChanged = (cachedType != view.getType());
        cachedType = view.getType();
        cachedTitle = view.getTitle();
        if (view.getPlayer() instanceof CraftPlayer) {
            CraftPlayer player = (CraftPlayer) view.getPlayer();
            ContainerType<?> type = getNotchInventoryType(view.getTopInventory());
            net.minecraft.inventory.Inventory top = ((CraftInventory) view.getTopInventory()).getInventory();
            PlayerInventory bottom = (PlayerInventory) ((CraftInventory) view.getBottomInventory()).getInventory();
            this.trackedStacks.clear();
            this.slots.clear();
            if (typeChanged) {
                setupSlots(top, bottom, player.getHandle());
            }
            int size = getSize();
            player.getHandle().networkHandler.sendPacket(new OpenContainerS2CPacket(this.syncId, type, new LiteralText(cachedTitle)));
            player.updateInventory();
        }
        return true;
    }

    public static ContainerType getNotchInventoryType(Inventory inventory) {
        switch (inventory.getType()) {
            case PLAYER:
            case CHEST:
            case ENDER_CHEST:
            case BARREL:
                switch (inventory.getSize()) {
                    case 9:
                        return ContainerType.GENERIC_9X1;
                    case 18:
                        return ContainerType.GENERIC_9X2;
                    case 27:
                        return ContainerType.GENERIC_9X3;
                    case 36:
                    case 41: // PLAYER
                        return ContainerType.GENERIC_9X4;
                    case 45:
                        return ContainerType.GENERIC_9X5;
                    case 54:
                        return ContainerType.GENERIC_9X6;
                    default:
                        throw new IllegalArgumentException("Unsupported custom inventory size " + inventory.getSize());
                }
            case WORKBENCH:
                return ContainerType.CRAFTING;
            case FURNACE:
                return ContainerType.FURNACE;
            case DISPENSER:
                return ContainerType.GENERIC_3X3;
            case ENCHANTING:
                return ContainerType.ENCHANTMENT;
            case BREWING:
                return ContainerType.BREWING_STAND;
            case BEACON:
                return ContainerType.BEACON;
            case ANVIL:
                return ContainerType.ANVIL;
            case HOPPER:
                return ContainerType.HOPPER;
            case DROPPER:
                return ContainerType.GENERIC_3X3;
            case SHULKER_BOX:
                return ContainerType.SHULKER_BOX;
            case BLAST_FURNACE:
                return ContainerType.BLAST_FURNACE;
            case LECTERN:
                return ContainerType.LECTERN;
            case SMOKER:
                return ContainerType.SMOKER;
            case LOOM:
                return ContainerType.LOOM;
            case CARTOGRAPHY:
                return ContainerType.CARTOGRAPHY_TABLE;
            case GRINDSTONE:
                return ContainerType.GRINDSTONE;
            case STONECUTTER:
                return ContainerType.STONECUTTER;
            case CREATIVE:
            case CRAFTING:
            case MERCHANT:
                throw new IllegalArgumentException("Can't open a " + inventory.getType() + " inventory!");
            default:
                // TODO: If it reaches the default case, should we throw an error?
                return ContainerType.GENERIC_9X3;
        }
    }

    private void setupSlots(net.minecraft.inventory.Inventory top, PlayerInventory bottom, PlayerEntity entityhuman) {
        int windowId = -1;
        switch (cachedType) {
            case CREATIVE:
                break; // TODO: This should be an error?
            case PLAYER:
            case CHEST:
            case ENDER_CHEST:
            case BARREL:
                delegate = new GenericContainer(ContainerType.GENERIC_9X3, windowId, bottom, top, top.getInvSize() / 9);
                break;
            case DISPENSER:
            case DROPPER:
                delegate = new Generic3x3Container(windowId, bottom, top);
                break;
            case FURNACE:
                delegate = new FurnaceContainer(windowId, bottom, top, new ArrayPropertyDelegate(4));
                break;
            case CRAFTING: // TODO: This should be an error?
            case WORKBENCH:
                setupWorkbench(top, bottom); // SPIGOT-3812 - manually set up slots so we can use the delegated inventory and not the automatically created one
                break;
            case ENCHANTING:
                delegate = new EnchantingTableContainer(windowId, bottom);
                break;
            case BREWING:
                delegate = new BrewingStandContainer(windowId, bottom, top, new ArrayPropertyDelegate(2));
                break;
            case HOPPER:
                delegate = new HopperContainer(windowId, bottom, top);
                break;
            case ANVIL:
                delegate = new AnvilContainer(windowId, bottom);
                break;
            case BEACON:
                delegate = new BeaconContainer(windowId, bottom);
                break;
            case SHULKER_BOX:
                delegate = new ShulkerBoxContainer(windowId, bottom, top);
                break;
            case BLAST_FURNACE:
                delegate = new BlastFurnaceContainer(windowId, bottom, top, new ArrayPropertyDelegate(4));
                break;
            case LECTERN:
                delegate = new LecternContainer(windowId, top, new ArrayPropertyDelegate(1));
                break;
            case SMOKER:
                delegate = new SmokerContainer(windowId, bottom, top, new ArrayPropertyDelegate(4));
                break;
            case LOOM:
                delegate = new LoomContainer(windowId, bottom);
                break;
            case CARTOGRAPHY:
                delegate = new CartographyTableContainer(windowId, bottom);
                break;
            case GRINDSTONE:
                delegate = new GrindstoneContainer(windowId, bottom);
                break;
            case STONECUTTER:
                delegate = new StonecutterContainer(windowId, bottom);
                break;
            case MERCHANT:
                delegate = new MerchantContainer(windowId, bottom);
                break;
        }

        if (delegate != null) {
            this.trackedStacks = delegate.trackedStacks;
            this.slots = delegate.slots;
        }

        // SPIGOT-4598 - we should still delegate the shift click handler
        if (cachedType == InventoryType.WORKBENCH) {
            delegate = new CraftingTableContainer(windowId, bottom);
        }
    }

    private void setupWorkbench(net.minecraft.inventory.Inventory top, net.minecraft.inventory.Inventory bottom) {
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

    @Override
    public ItemStack transferSlot(PlayerEntity entityhuman, int i) {
        return (delegate != null) ? delegate.transferSlot(entityhuman, i) : super.transferSlot(entityhuman, i);
    }

    @Override
    public boolean canUse(PlayerEntity entity) {
        return true;
    }

    @Override
    public ContainerType<?> getType() {
        return getNotchInventoryType(view.getTopInventory());
    }
}
