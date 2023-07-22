package com.mohistmc.api;

import com.mohistmc.MohistMC;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ItemAPI {

    public static Logger LOGGER = LogManager.getLogger("ItemAPI");

    public static ItemStack doItem(Material material, int menge, String name, ArrayList<String> lore) {
        final ItemStack item = new ItemStack(material, menge);
        final ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    public static net.minecraft.world.item.ItemStack toNMSItem(Material material) {
        ItemStack itemStackcb = new ItemStack(material);
        return CraftItemStack.asNMSCopy(itemStackcb);
    }

    public static net.minecraft.world.item.ItemStack toNMSItem(ItemStack itemStack) {
        return CraftItemStack.asNMSCopy(itemStack);
    }

    public static ItemStack getBukkit(Material material) {
        return new ItemStack(material);
    }

    public static CompoundTag getNbt(ItemStack itemStack) {
        return toNMSItem(itemStack).getTag() == null ? null : toNMSItem(itemStack).getTag();
    }

    public static String getNBTAsString(ItemStack itemStack) {
        return toNMSItem(itemStack).getTag() == null ? "null" : toNMSItem(itemStack).getTag().getAsString();
    }

    public static String getNbtAsString(CompoundTag compoundTag) {
        return compoundTag == null ? "null" : compoundTag.getAsString();
    }

    /**
     * Parse Base64 into {@link ItemStack}
     * it should be noted that this method is only used for ItemStack without any NBT
     *
     * @param base64
     * @return
     * @throws IOException
     */
    public static ItemStack getBukkitByBase64(String base64) {
        try {
            try (BukkitObjectInputStream dataInput = new BukkitObjectInputStream(new ByteArrayInputStream(Base64Coder.decodeLines(base64)))) {
                return (ItemStack) dataInput.readObject();
            }
        } catch (ClassNotFoundException | IOException e) {
            LOGGER.error("Unable to decode class type.");
            return getBukkit(Material.AIR);
        }
    }

    /**
     * Parse {@link ItemStack} into Base64
     * it should be noted that this method is only used for ItemStack without any NBT
     *
     * @param stack
     * @return
     */
    public static String getBase64byBukkit(ItemStack stack) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(stack);

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            return null;
        }
    }

    public static String serializeNBT(ItemStack itemStack) {
        return getNbt(itemStack) == null ? null : serializeNbt(getNbt(itemStack));
    }

    public static ItemStack deserializeNBT(String serializeNBT) {
        if (serializeNBT != null && !serializeNBT.isEmpty()) {
            return CraftItemStack.asBukkitCopy(new net.minecraft.world.item.ItemStack(ItemAPI.deserializeNbt(serializeNBT)));
        }
        return new ItemStack(Material.AIR);
    }

    public static String serializeNbt(CompoundTag nbtTagCompound) {
        try {
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            NbtIo.writeCompressed(nbtTagCompound, buf);
            return Base64Coder.encodeLines(buf.toByteArray());
        } catch (IOException ignored) {
            return null;
        }
    }

    public static CompoundTag deserializeNbt(String serializeNBT) {
        if (serializeNBT != null) {
            ByteArrayInputStream buf = new ByteArrayInputStream(Base64Coder.decodeLines(serializeNBT));
            try {
                return NbtIo.readCompressed(buf);
            } catch (IOException e) {
                MohistMC.LOGGER.error("Reading nbt ", e);
            }
        }
        return null;
    }

    public static void name(ItemStack itemStack, String name) {
        ItemMeta im = itemStack.getItemMeta();
        im.setDisplayName(name.replace("&", "§"));
        itemStack.setItemMeta(im);
    }

    public static void lore(ItemStack itemStack, List<String> lore) {
        ItemMeta im = itemStack.getItemMeta();
        im.setLore(lore);
        itemStack.setItemMeta(im);
    }
}
