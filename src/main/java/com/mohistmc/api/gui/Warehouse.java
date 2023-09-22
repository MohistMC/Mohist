package com.mohistmc.api.gui;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author LSeng
 */
public class Warehouse {

    GUI gui;
    List<GUIItem> items = new ArrayList<>();
    String tempName;
    int pageChoose = 0;

    private Warehouse() {
    }

    public Warehouse(String name) {
        this.gui = new GUI(GUIType.SIXBYNINE, name);

        for (int i = 36; i < 46; i++) {
            this.gui.setItem(i, new GUIItem(new ItemStackFactory(Material.GLASS_PANE)
                    .setDisplayName(" ")
                    .toItemStack()));
        }

        this.gui.setItem(new GUIItem(new ItemStackFactory(Material.GLASS_PANE)
                .setDisplayName(" ")
                .toItemStack()), 47, 48, 49, 50);

        this.gui.setItem(46, new GUIItem(new ItemStackFactory(Material.REDSTONE)
                .setDisplayName("§cClose")
                .toItemStack()) {
            @Override
            public void ClickAction(ClickType type, Player u, ItemStack itemStack) {
                u.closeInventory();
            }
        });
    }

    public final GUIItem getItem(int index) {
        return this.items.get(index);
    }

    public final void removeItem(int index) {
        this.items.remove(index);
    }

    public final void removeItem(GUIItem item) {
        this.items.remove(item);
    }

    public final void addItem(GUIItem item) {
        this.items.add(item);
    }

    public final void addItem(List<GUIItem> items) {
        for (GUIItem item : items) {
            addItem(item);
        }
    }

    public final void addItem(GUIItem... items) {
        for (GUIItem item : items) {
            addItem(item);
        }
    }

    public final List<GUIItem> getItems() {
        return this.items;
    }

    public GUI getGUI() {
        return getGUI(this.items);
    }


    private GUI getGUI(List<GUIItem> items) {
        int page = items.size() / 36 + 1;
        for (int i = 0; i < 36; i++) {
            gui.setItem(i, new GUIItem(new ItemStack(Material.AIR)));
        }

        int index = 0;
        if (pageChoose == page - 1) {
            if (pageChoose == 0) {
                for (int i = 0; i < items.size() % 36; i++) {
                    gui.setItem(index, items.get(i));
                    index++;
                }
            } else {
                for (int i = this.pageChoose * 36; i < pageChoose * 36 + items.size() % 36; i++) {
                    gui.setItem(index, items.get(i));
                    index++;
                }
            }
        } else if (pageChoose == 0) {
            for (int i = 0; i < 36; i++) {
                gui.setItem(index, items.get(i));
                index++;
            }
        } else {
            for (int i = this.pageChoose * 36; i < this.pageChoose * 36 + 36; i++) {
                gui.setItem(index, items.get(i));
                index++;
            }
        }

        if (this.pageChoose == 0) {
            gui.setItem(51, new GUIItem(new ItemStackFactory(Material.ACACIA_FENCE)
                    .setDisplayName("&cThis is already the home page")
                    .toItemStack()));
        } else {
            gui.setItem(51, new GUIItem(new ItemStackFactory(Material.ACACIA_FENCE)
                    .setDisplayName("&eprevious page")
                    .toItemStack()) {
                @Override
                public void ClickAction(ClickType type, Player u, ItemStack itemStack) {
                    Warehouse.this.pageChoose--;
                    openGUI(u);
                }
            });
        }

        gui.setItem(52, new GUIItem(new ItemStackFactory(Material.PAPER, pageChoose + 1)
                .setDisplayName("&7No. &f&l" + (pageChoose + 1) + " &7page")
                .toItemStack()));

        if (this.pageChoose < page - 1) {
            gui.setItem(53, new GUIItem(new ItemStackFactory(Material.ACACIA_FENCE)
                    .setDisplayName("&enext page")
                    .toItemStack()) {
                @Override
                public void ClickAction(ClickType type, Player u, ItemStack itemStack) {
                    Warehouse.this.pageChoose++;
                    openGUI(u);
                }
            });
        } else {
            gui.setItem(53, new GUIItem(new ItemStackFactory(Material.ACACIA_FENCE)
                    .setDisplayName("&cThis is already the last page")
                    .toItemStack()));
        }

        return this.gui;
    }

    public void openGUI(Player p) {
        getGUI().openGUI(p);
    }

}

