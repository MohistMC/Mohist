package com.mohistmc.api;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IItemProvider;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class ItemAPI {

    public static net.minecraft.item.ItemStack toNMSItem(Material materialcb) {
        ItemStack itemStackcb = new ItemStack(materialcb);
        return CraftItemStack.asNMSCopy(itemStackcb);
    }

    public static ItemStack getBukkit(Material material) {
        return CraftItemStack.asBukkitCopy(toNMSItem(material));
    }

    /**
     * Parse Base64 into {@link org.bukkit.inventory.ItemStack}
     * it should be noted that this method is only used for ItemStack without any NBT
     *
     * @param base64
     * @return
     * @throws IOException
     */
    public static ItemStack Base64ToBukkit(String base64) throws IOException {
        try {
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(new ByteArrayInputStream(Base64Coder.decodeLines(base64)));
            try {
                return (ItemStack) dataInput.readObject();
            } finally {
                dataInput.close();
            }
        } catch (ClassNotFoundException | IOException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

    /**
     * Parse {@link org.bukkit.inventory.ItemStack} into Base64
     * it should be noted that this method is only used for ItemStack without any NBT
     *
     * @param stack
     * @return
     */
    public static String BukkitToBase64(ItemStack stack) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(stack);

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stack.", e);
        }
    }

    /**
     * For mod items and {@link org.bukkit.inventory.ItemStack} with NBT
     *
     * @param base64
     * @return
     */
    public static ItemStack NBTBase64ToBukkit(String base64) {
        DataInput input = new DataInputStream(new ByteArrayInputStream(Base64Coder.decodeLines(base64)));
        CompoundNBT tag = new CompoundNBT();

        try {
            CompoundNBT.readType(input,  NBTSizeTracker.INFINITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        net.minecraft.item.ItemStack handle = new net.minecraft.item.ItemStack((IItemProvider) tag);
        return CraftItemStack.asBukkitCopy(handle);
    }

    /**
     * For mod items and {@link org.bukkit.inventory.ItemStack} with NBT
     *
     * @param stack
     * @return
     */
    public static String BukkitToNBTBase64(ItemStack stack) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            CraftItemStack copy = CraftItemStack.asCraftCopy(stack);
            CompoundNBT tag = new CompoundNBT();
            copy.getHandle().write(tag);
            tag.write(new DataOutputStream(out));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64Coder.encodeLines(out.toByteArray());
    }

    /**
     *
     * Get the byte of {@link org.bukkit.inventory.ItemStack}
     *
     * @param iStack
     * @return
     */
    public static byte[] getNBTBytes(ItemStack iStack) {
        try{
            net.minecraft.item.ItemStack is = CraftItemStack.asNMSCopy(iStack);
            CompoundNBT itemCompound = new CompoundNBT();
            itemCompound = is.write(itemCompound);
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            DataOutputStream dataOut = new DataOutputStream(new GZIPOutputStream(byteOut));
            try {
                net.minecraft.nbt.CompressedStreamTools.writeCompressed(itemCompound, dataOut);
            } finally {
                dataOut.close();
            }
            return byteOut.toByteArray();
        }catch(Exception e){
            return new byte[0];
        }
    }

    /**
     *
     * Parse byte as {@link org.bukkit.inventory.ItemStack}
     *
     * @param bytes
     * @return
     */
    public static ItemStack getItemStackInNBTBytes(byte[] bytes) {
        try{
            DataInputStream dataIn = new DataInputStream(new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(bytes))));
            CompoundNBT tag;
            try {
                tag = net.minecraft.nbt.CompressedStreamTools.read(dataIn, null);
            } finally {
                dataIn.close();
            }
            net.minecraft.item.ItemStack is = new net.minecraft.item.ItemStack((IItemProvider) tag);
            return CraftItemStack.asBukkitCopy(is);
        }catch(Exception e){
            return new ItemStack(Material.AIR);
        }
    }

    public static String serializeNBT(CompoundNBT nbtTagCompound) {
        try {
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            CompressedStreamTools.writeCompressed(nbtTagCompound, buf);
            return Base64Coder.encodeLines(buf.toByteArray());
        } catch (IOException ignored) {
        }
        return null;
    }

    public static CompoundNBT deserializeNBT(String serializeNBT) {
        if (serializeNBT != null) {
            ByteArrayInputStream buf = new ByteArrayInputStream(Base64Coder.decodeLines(serializeNBT));
            try {
                return CompressedStreamTools.readCompressed(buf);
            } catch (IOException ignored) {
            }
        }
        return null;
    }
}
