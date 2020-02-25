package red.mohist.api;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
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
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(base64));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
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
    public ItemStack NBTBase64ToBukkit(String base64) {
        DataInput input = new DataInputStream(new ByteArrayInputStream(Base64Coder.decodeLines(base64)));
        NBTTagCompound tag = new NBTTagCompound();

        try {
            tag.read0(input, 0, NBTSizeTracker.INFINITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        net.minecraft.item.ItemStack handle = new net.minecraft.item.ItemStack(tag);
        return CraftItemStack.asBukkitCopy(handle);
    }

    /**
     * For mod items and {@link org.bukkit.inventory.ItemStack} with NBT
     *
     * @param stack
     * @return
     */
    public String BukkitToNBTBase64(ItemStack stack) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            CraftItemStack copy = CraftItemStack.asCraftCopy(stack);
            NBTTagCompound tag = new NBTTagCompound();
            copy.getHandle().writeToNBT(tag);
            tag.write0(new DataOutputStream(out));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64Coder.encodeLines(out.toByteArray());
    }
}
