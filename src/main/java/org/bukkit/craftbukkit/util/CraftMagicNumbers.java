package org.bukkit.craftbukkit.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import org.bukkit.Achievement;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.UnsafeValues;
import org.bukkit.craftbukkit.CraftStatistic;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

@SuppressWarnings("deprecation")
public final class CraftMagicNumbers implements UnsafeValues {
    public static final UnsafeValues INSTANCE = new CraftMagicNumbers();

    private CraftMagicNumbers() {}

    public static net.minecraft.block.Block getBlock(org.bukkit.block.Block block) {
        return getBlock(block.getType());
    }

    @Deprecated
    // A bad method for bad magic.
    public static net.minecraft.block.Block getBlock(int id) {
        return getBlock(Material.getMaterial(id));
    }

    @Deprecated
    // A bad method for bad magic.
    public static int getId(net.minecraft.block.Block block) {
        return net.minecraft.block.Block.getIdFromBlock(block);
    }

    public static Material getMaterial(net.minecraft.block.Block block) {
        return Material.getMaterial(net.minecraft.block.Block.getIdFromBlock(block));
    }

    public static net.minecraft.item.Item getItem(Material material) {
        // TODO: Don't use ID
        net.minecraft.item.Item item = net.minecraft.item.Item.getItemById(material.getId());
        return item;
    }

    @Deprecated
    // A bad method for bad magic.
    public static net.minecraft.item.Item getItem(int id) {
        return net.minecraft.item.Item.getItemById(id);
    }

    @Deprecated
    // A bad method for bad magic.
    public static int getId(net.minecraft.item.Item item) {
        return net.minecraft.item.Item.getIdFromItem(item);
    }

    public static Material getMaterial(net.minecraft.item.Item item) {
        // TODO: Don't use ID
        Material material = Material.getMaterial(net.minecraft.item.Item.getIdFromItem(item));

        if (material == null) {
            return Material.AIR;
        }

        return material;
    }

    public static net.minecraft.block.Block getBlock(Material material) {
        material = material == null ? Material.AIR : material; // Cauldron - this should not happen but just in case it does
        // TODO: Don't use ID
        net.minecraft.block.Block block = net.minecraft.block.Block.getBlockById(material.getId());

        if (block == null) {
            return net.minecraft.init.Blocks.air;
        }

        return block;
    }

    @Override
    public Material getMaterialFromInternalName(String name) {
        return getMaterial((net.minecraft.item.Item) net.minecraft.item.Item.itemRegistry.getObject(name));
    }

    @Override
    public List<String> tabCompleteInternalMaterialName(String token, List<String> completions) {
        return StringUtil.copyPartialMatches(token, net.minecraft.item.Item.itemRegistry.getKeys(), completions);
    }

    @Override
    public ItemStack modifyItemStack(ItemStack stack, String arguments) {
        net.minecraft.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);

        try
        {
            nmsStack.setTagCompound((net.minecraft.nbt.NBTTagCompound) net.minecraft.nbt.JsonToNBT.func_150315_a(arguments));
        }
        catch (net.minecraft.nbt.NBTException e)
        {
            e.printStackTrace();
        }

        stack.setItemMeta(CraftItemStack.getItemMeta(nmsStack));

        return stack;
    }

    @Override
    public Statistic getStatisticFromInternalName(String name) {
        return CraftStatistic.getBukkitStatisticByName(name);
    }

    @Override
    public Achievement getAchievementFromInternalName(String name) {
        return CraftStatistic.getBukkitAchievementByName(name);
    }

    @Override
    public List<String> tabCompleteInternalStatisticOrAchievementName(String token, List<String> completions) {
        List<String> matches = new ArrayList<String>();
        Iterator iterator = net.minecraft.stats.StatList.allStats.iterator();
        while (iterator.hasNext()) {
            String statistic = ((net.minecraft.stats.StatBase) iterator.next()).statId;
            if (statistic.startsWith(token)) {
                matches.add(statistic);
            }
        }
        return matches;
    }
}