package red.mohist.command.defaultcomamnd;

import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import red.mohist.MohistConfig;
import red.mohist.MohistThreadCost;
import red.mohist.api.PlayerAPI;
import red.mohist.api.ServerAPI;
import red.mohist.i18n.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MohistCommand extends Command {

    public MohistCommand(String name) {
        super(name);
        this.description = "Mohist related commands";
        this.usageMessage = "/mohist [potions|enchants|materials|commands|mods|playermods|entitytypes|biomes|printthreadcost]";
    }

    private List<String> params = Arrays.asList("potions", "enchants", "materials", "commands", "mods", "playermods", "entitytypes", "biomes", "printthreadcost");

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        List<String> list = new ArrayList<>();
        if (args.length == 1 && sender.isOp()) {
            for (String param : params) {
                if (param.toLowerCase().startsWith(args[0].toLowerCase())) {
                    list.add(param);
                }
            }
        }

        return list;
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
            case "potions":
                // Not recommended for use in games, only test output
                getPotions(sender);
                break;
            case "enchants":
                // Not recommended for use in games, only test output
                getEnchant(sender);
                break;
            case "materials":
                // Not recommended for use in games, only test output
                getMaterials(sender);
                break;
            case "entitytypes":
                // Not recommended for use in games, only test output
                getEntityTypes(sender);
                break;
            case "commands":
                // Not recommended for use in games, only test output
                getCommands(sender);
                break;
            case "biomes":
                // Not recommended for use in games, only test output
                getBiomes(sender);
                break;
            case "mods":
                // Not recommended for use in games, only test output
                sender.sendMessage(ChatColor.GREEN + "" + ServerAPI.getModSize() + " " + ServerAPI.getModList());
                break;
            case "playermods":
                // Not recommended for use in games, only test output
                if (args.length == 1) {
                    sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
                    return false;
                }
                Player player = Bukkit.getPlayer(args[1].toString());
                if (player != null) {
                    sender.sendMessage(ChatColor.GREEN + "" + PlayerAPI.getModSize(player) + " " + PlayerAPI.getModlist(player));
                } else {
                    sender.sendMessage(ChatColor.RED + "The player [" + args[1] + "] is not online.");
                }
                break;
            case "reload":
                MohistConfig.reload();
                sender.sendMessage(ChatColor.GREEN + "Config reload success");
                break;
            case "printthreadcost":
                MohistThreadCost.dumpThreadCpuTime();
                break;
            default:
                sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
                return false;
        }

        return true;
    }

    private void getPotions(CommandSender sender) {
        for (PotionEffectType pet : PotionEffectType.values()) {
            if (pet != null) {
                sender.sendMessage(pet.toString());
            }
        }
    }

    private void getEnchant(CommandSender sender) {
        for (Enchantment ench : Enchantment.values()) {
            sender.sendMessage(ench.toString());
        }
    }

    private void getMaterials(CommandSender sender) {
        for (Material mat : Material.values()) {
            sender.sendMessage(mat.toString());
        }
    }

    private void getEntityTypes(CommandSender sender) {
        for (EntityType ent : EntityType.values()) {
            sender.sendMessage(ent.toString());
        }
    }

    private void getCommands(CommandSender sender) {
        for (Command per : MinecraftServer.getServerInst().server.getCommandMap().getCommands()) {
            sender.sendMessage(per.getName() + ": " + per.getPermission());
        }
    }

    private void getBiomes(CommandSender sender) {
        for (Biome biome : Biome.values()) {
            sender.sendMessage(biome.name());
        }
    }
}
