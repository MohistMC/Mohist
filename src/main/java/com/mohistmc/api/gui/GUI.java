package com.mohistmc.api.gui;

import com.mohistmc.plugins.MohistPlugin;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @author LSeng
 */
public class GUI {

    static Map<Player, GUI> openGUI = new HashMap<>();
    public GUIItem[] items;
    public Inventory inv;
    GUIType type;
    String tempName;

    private GUI() {
    }

    public GUI(GUIType type, String name) {
        this.type = type;
        this.tempName = name;
        switch (type) {
            default:
                this.items = new GUIItem[36];
            case ONEBYNINE:
                this.items = new GUIItem[9];
                break;
            case TWOBYNINE:
                this.items = new GUIItem[18];
                break;
            case THREEBYNINE:
                this.items = new GUIItem[27];
                break;
            case FOURBYNINE:
                this.items = new GUIItem[36];
                break;
            case FIVEBYNINE:
                this.items = new GUIItem[45];
                break;
            case SIXBYNINE:
                this.items = new GUIItem[54];
                break;
        }

        Bukkit.getPluginManager().registerEvents(new Listener() {

            @EventHandler
            public void onInventoryClickEvent(InventoryClickEvent event) {
                if (!(event.getWhoClicked() instanceof Player p)) {
                    return;
                }
                if (event.getCurrentItem() == null) {
                    return;
                }
                if (openGUI.containsKey(p) && openGUI.get(p) == GUI.this) {
                    event.setCancelled(true);

                    if (items[event.getSlot()] != null) {
                        items[event.getSlot()].ClickAction(event.getClick(), p, items[event.getSlot()].display);
                    }
                }
            }

            @EventHandler
            public void onInventoryCloseEvent(InventoryCloseEvent event) {
                if (event.getInventory() == inv) {
                    HandlerList.unregisterAll(this);
                    openGUI.remove((Player) event.getPlayer());
                }
            }

        }, MohistPlugin.plugin);

    }

    public final void setItem(GUIItem item, int... index) {
        for (int i : index) {
            setItem(i, item);
        }
    }

    public final void setItem(int index, GUIItem item) {
        this.items[index] = Objects.requireNonNullElseGet(item, () -> new GUIItem(new ItemStack(Material.AIR)));
    }

    public final GUIItem getItem(int index) {
        return this.items[index];
    }


    public void openGUI(Player p) {
        Inventory inv;
        if (this.type == GUIType.CANCEL) {
            throw new NullPointerException("Canceled or non-existent GUI");
        }
        inv = Bukkit.createInventory(null, this.items.length, this.tempName);
        for (int index = 0; index < this.items.length; index++) {
            if (items[index] == null) {
                inv.setItem(index, new ItemStack(Material.AIR));
            } else {
                inv.setItem(index, items[index].display);
            }
        }
        this.inv = inv;
        p.getPlayer().openInventory(inv);
        openGUI.put(p, this);

    }

}
