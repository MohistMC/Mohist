package com.mohistmc.api;

import com.mohistmc.MohistConfig;
import com.mohistmc.MohistMC;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

public class ItemAPI {

    public static final Logger LOGGER = LogManager.getLogger("ItemAPI");

    public static ItemStack doItem(Material material, int menge, String name, List<String> lore) {
        ItemStack item = new ItemStack(material, menge);
        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    public static net.minecraft.world.item.ItemStack toNMSItem(Material material) {
        ItemStack itemStack = new ItemStack(material);
        return CraftItemStack.asNMSCopy(itemStack);
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
     * Parse Base64 into {@link org.bukkit.inventory.ItemStack}
     * it should be noted that this method is only used for ItemStack without any NBT
     *
     * @param base64
     * @return
     * @throws IOException
     */
    public static ItemStack getBukkitByBase64(String base64) {
        try {
            try (BukkitObjectInputStream dataInput = new BukkitObjectInputStream(new ByteArrayInputStream(Base64.getDecoder().decode(base64)))) {
                return (ItemStack) dataInput.readObject();
            }
        } catch (ClassNotFoundException | IOException e) {
            LOGGER.error("Unable to decode class type.");
            return getBukkit(Material.AIR);
        }
    }

    /**
     * Parse {@link org.bukkit.inventory.ItemStack} into Base64
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
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
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
            return Base64.getEncoder().encodeToString(buf.toByteArray());
        } catch (IOException ignored) {
            return null;
        }
    }

    public static CompoundTag deserializeNbt(String serializeNBT) {
        if (serializeNBT != null) {
            ByteArrayInputStream buf = new ByteArrayInputStream(Base64.getDecoder().decode(serializeNBT));
            try {
                return NbtIo.readCompressed(buf);
            } catch (IOException e) {
                MohistMC.LOGGER.error("Reading nbt ", e);
            }
        }
        return null;
    }

    public static byte[] nbtToByte(CompoundTag nbt){
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            nbt.write(dos);
            byte[] outputByteArray = baos.toByteArray();
            dos.close();
            baos.close();
            return outputByteArray;
        } catch (IOException e) {
            MohistMC.LOGGER.error("nbtToByte ", e);
            return null;
        }
    }

    public static CompoundTag byteToNbt(byte[] nbtByte) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(nbtByte, 0, nbtByte.length);
            DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
            CompoundTag reconstructedCompoundTag = NbtIo.read(dataInputStream);
            dataInputStream.close();
            byteArrayInputStream.close();
            return reconstructedCompoundTag;
        } catch (IOException e) {
            MohistMC.LOGGER.error("byteToNbt ", e);
            return null;
        }
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

    @Deprecated
    public static TextComponent show(ItemStack itemStack) {
        net.minecraft.world.item.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        CompoundTag compound = new CompoundTag();
        nmsItemStack.save(compound);
        String json = compound.toString();
        BaseComponent[] hoverEventComponents = new BaseComponent[]{
                new TextComponent(json)
        };
        TextComponent component = new TextComponent(itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName() ? itemStack.getItemMeta().getDisplayName() : itemStack.getTranslationKey());
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, hoverEventComponents));
        return component;
    }

    public static boolean isBan(ItemStack itemStack) {
        if (itemStack == null) return false;
        return MohistConfig.ban_item_materials.contains(itemStack.getType().name());
    }

    public static Material getEggMaterial(EntityType entitytype) {
        try {
            if (entitytype == EntityType.PLAYER) {
                return Material.PLAYER_HEAD;
            }
            String getMaterial = entitytype + "_SPAWN_EGG";
            return Material.valueOf(entitytype.toString().equals("MUSHROOM_COW") ? "MOOSHROOM_SPAWN_EGG" : getMaterial);
        } catch (Exception e) {
            try {
                return Material.valueOf(entitytype.getName().toUpperCase());
            } catch (Exception e1) {
                return Material.SPAWNER;
            }
        }
    }

    public static Material getMaterial(String type) {
        try {
            return Material.valueOf(type);
        } catch (Exception e) {
            return Material.AIR;
        }
    }

    public static Enchantment getEnchantmentByName(String name) {
        try {
            return Enchantment.getByName(name);
        } catch (Exception e) {
            return null;
        }
    }

    public static Enchantment getEnchantmentByKey(String key) {
        try {
            return Enchantment.getByKey(NamespacedKey.fromString(key));
        } catch (Exception e) {
            return getEnchantmentByName(key);
        }
    }
}