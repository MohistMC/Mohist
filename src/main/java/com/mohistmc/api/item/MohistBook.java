package com.mohistmc.api.item;

import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class MohistBook {

    private BookMeta bm;
    private ItemStack item;

    public static MohistBook create(ItemStack writtenbook) {
        return new MohistBook(writtenbook);
    }

    public MohistBook(ItemStack writtenbook) {
        item = writtenbook;
        bm = (BookMeta)item.getItemMeta();
    }

    public MohistBook setAuthor(String name) {
        bm.setAuthor(name);
        return this;
    }

    public MohistBook addPage(String... content) {
        bm.addPage(content);
        return this;
    }

    public MohistBook addPages(List<String> contents) {
        for (String content : contents) {
            bm.addPage(content);
        }
        return this;
    }

    public int getPageCount() {
        return bm.getPageCount();
    }

    public MohistBook clone(ItemStack writtenbook) {
        this.item = writtenbook;
        this.bm = (BookMeta)item.getItemMeta();
        return this;
    }

    public ItemStack build() {
        item.setItemMeta(bm);
        return item;
    }
}
