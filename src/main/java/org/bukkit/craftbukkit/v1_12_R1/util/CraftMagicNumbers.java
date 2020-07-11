package org.bukkit.craftbukkit.v1_12_R1.util;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatList;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.craftbukkit.v1_12_R1.CraftStatistic;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;
import red.mohist.Mohist;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class CraftMagicNumbers implements UnsafeValues {
    public static final UnsafeValues INSTANCE = new CraftMagicNumbers();

    private CraftMagicNumbers() {
    }

    public static Block getBlock(org.bukkit.block.Block block) {
        return getBlock(block.getType());
    }

    public static Block getBlock(int id) {
        return getBlock(Material.getBlockMaterial(id));
    }

    public static int getId(Block block) {
        return Block.getIdFromBlock(block);
    }

    public static Material getMaterial(Block block) {
        return Material.getBlockMaterial(Block.getIdFromBlock(block));
    }

    public static Item getItem(Material material) {
        // TODO: Don't use ID
        Item item = Item.getItemById(material.getId());
        return item;
    }

    public static Item getItem(int id) {
        return Item.getItemById(id);
    }

    public static int getId(Item item) {
        return Item.getIdFromItem(item);
    }

    public static Material getMaterial(Item item) {
        // TODO: Don't use ID
        Material material = Material.getMaterial(Item.getIdFromItem(item));

        if (material == null) {
            return Material.AIR;
        }

        return material;
    }

    public static Block getBlock(Material material) {
        // MCPC+ - BOP sometimes ends up with a null material here
        material = material == null ? Material.AIR : material;

        // TODO: Don't use ID
        Block block = Block.getBlockById(material.getId());

        if (block == null) {
            return Blocks.AIR;
        }

        return block;
    }

    @Override
    public Material getMaterialFromInternalName(String name) {
        return getMaterial(Item.REGISTRY.getObject(new ResourceLocation(name)));
    }

    @Override
    public List<String> tabCompleteInternalMaterialName(String token, List<String> completions) {
        ArrayList<String> results = Lists.newArrayList();
        for (ResourceLocation key : Item.REGISTRY.getKeys()) {
            results.add(key.toString());
        }
        return StringUtil.copyPartialMatches(token, results, completions);
    }

    @Override
    public ItemStack modifyItemStack(ItemStack stack, String arguments) {
        net.minecraft.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);

        try {
            nmsStack.setTagCompound(JsonToNBT.getTagFromJson(arguments));
        } catch (NBTException ex) {
            Logger.getLogger(CraftMagicNumbers.class.getName()).log(Level.SEVERE, null, ex);
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
        throw new UnsupportedOperationException("Not supported in this Minecraft version.");
    }

    @Override
    public List<String> tabCompleteInternalStatisticOrAchievementName(String token, List<String> completions) {
        List<String> matches = new ArrayList<>();
        for (net.minecraft.stats.StatBase statBase : StatList.ALL_STATS) {
            String statistic = statBase.statId;
            if (statistic.startsWith(token)) {
                matches.add(statistic);
            }
        }
        return matches;
    }

    @Override
    public Advancement loadAdvancement(NamespacedKey key, String advancement) {
        if (Bukkit.getAdvancement(key) != null) {
            throw new IllegalArgumentException("Advancement " + key + " already exists.");
        }

        net.minecraft.advancements.Advancement.Builder nms = JsonUtils.gsonDeserialize(AdvancementManager.GSON, advancement, net.minecraft.advancements.Advancement.Builder.class);
        if (nms != null) {
            AdvancementManager.ADVANCEMENT_LIST.loadAdvancements(Maps.newHashMap(Collections.singletonMap(CraftNamespacedKey.toMinecraft(key), nms)));
            Advancement bukkit = Bukkit.getAdvancement(key);

            if (bukkit != null) {
                File file = new File(MinecraftServer.getServerInst().getAdvancementManager().advancementsDir, key.getNamespace() + File.separator + key.getKey() + ".json");
                file.getParentFile().mkdirs();

                try {
                    Files.write(advancement, file, Charsets.UTF_8);
                } catch (IOException ex) {
                    Mohist.LOGGER.error("Error saving advancement " + key, ex);
                }

                MinecraftServer.getServerInst().getPlayerList().reloadResources();

                return bukkit;
            }
        }

        return null;
    }

    @Override
    public boolean removeAdvancement(NamespacedKey key) {
        File file = new File(MinecraftServer.getServerInst().getAdvancementManager().advancementsDir, key.getNamespace() + File.separator + key.getKey() + ".json");
        return file.delete();
    }

    public static class NBT {

        public static final int TAG_END = 0;
        public static final int TAG_BYTE = 1;
        public static final int TAG_SHORT = 2;
        public static final int TAG_INT = 3;
        public static final int TAG_LONG = 4;
        public static final int TAG_FLOAT = 5;
        public static final int TAG_DOUBLE = 6;
        public static final int TAG_BYTE_ARRAY = 7;
        public static final int TAG_STRING = 8;
        public static final int TAG_LIST = 9;
        public static final int TAG_COMPOUND = 10;
        public static final int TAG_INT_ARRAY = 11;
        public static final int TAG_ANY_NUMBER = 99;
    }
}
