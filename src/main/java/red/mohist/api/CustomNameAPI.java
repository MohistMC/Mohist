package red.mohist.api;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class CustomNameAPI {

    /**
     *  Get form minecraft i18n name (zh_cn and en_us)
     * @param itemStackcb org.bukkit.inventory.ItemStack
     */
    public static String getItemName(ItemStack itemStackcb) {
        net.minecraft.item.ItemStack itemStack = CraftItemStack.asNMSCopy(itemStackcb);
        Object textc = itemStack.getTextComponent();
        String name = String.valueOf(textc);
        String string = name.substring(0, name.indexOf("', siblings=[]"));

        return string.substring(string.lastIndexOf("'") + 1);
    }

    /**
     *  Get form minecraft i18n name (zh_cn and en_us)
     * @param materialcb org.bukkit.Material
     */
    public static String getItemName(Material materialcb) {
        ItemStack itemStackcb = new ItemStack(materialcb);
        net.minecraft.item.ItemStack itemStack = CraftItemStack.asNMSCopy(itemStackcb);
        Object textc = itemStack.getTextComponent();
        String name = String.valueOf(textc);
        String string = name.substring(0, name.indexOf("', siblings=[]"));

        return string.substring(string.lastIndexOf("'") + 1);
    }
}
