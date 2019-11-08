package red.mohist.command;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import red.mohist.api.CustomNameAPI;

public class ItemCommand{

    public static void info(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            Item item = CraftItemStack.asNMSCopy(itemStack).getItem();
            // item name and i18n name
            player.sendMessage(ChatColor.GRAY + "Name - " + ChatColor.GREEN + itemStack.getType().toString() + " (" + CustomNameAPI.getItemName(itemStack) + ")");
            // mcp and bukkit
            player.sendMessage(ChatColor.GRAY + "ID - " + ChatColor.GREEN + Item.getIdFromItem(item) + ":" + itemStack.getDurability() + " ("
            + itemStack.getTypeId() + ":" + itemStack.getDurability() + ")");
            if (item instanceof ItemBlock) {
                player.sendMessage(ChatColor.GRAY + "Block ID - " + ChatColor.GREEN + Block.getIdFromBlock(Block.getBlockFromItem(item)));
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You must be a player to perform this command.");
        }
    }
}
