package red.mohist.command;

import java.util.Locale;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import red.mohist.api.CustomNameAPI;
import red.mohist.util.i18n.Message;

public class ItemCommand extends Command {

    public ItemCommand(String name) {
        super(name);
        this.description = "Item commands";
        this.usageMessage = "/item [info]";
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(Message.getString("command.nopermission"));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }
        switch (args[0].toLowerCase(Locale.ENGLISH)) {
            case "info":
                // Not recommended for use in games, only test output
                info(sender);
                break;
        }
        return false;
    }

    private void info(CommandSender sender) {
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
