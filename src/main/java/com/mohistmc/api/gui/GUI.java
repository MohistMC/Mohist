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

public class GUI {

    static Map<Player, GUI> openGUI = new HashMap<>();
    public GUIItem[] items;
    public Inventory inv;
    GUISize type;
    String tempName;

    private GUI() {
    }

    public GUI(GUISize type, String name) {
        this.type = type;
        this.tempName = name;
        this.items = new GUIItem[type.size];

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

                    GUIItem guiItem = items[event.getSlot()];
                    if (guiItem != null && guiItem.clickAction() != null) {
                        guiItem.clickAction().run(event.getClick(), p, items[event.getSlot()].display());
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
        Inventory inv = Bukkit.createInventory(null, this.items.length, this.tempName);
        for (int index = 0; index < this.items.length; index++) {
            if (items[index] == null) {
                inv.setItem(index, new ItemStack(Material.AIR));
            } else {
                inv.setItem(index, items[index].display());
            }
        }
        this.inv = inv;
        p.openInventory(inv);
        openGUI.put(p, this);
    }

}
