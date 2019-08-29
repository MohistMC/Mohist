package red.mohist.command;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.io.FileUtils;
import org.bukkit.ChatColor;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import red.mohist.api.ServerAPI;
import red.mohist.util.i18n.Message;

public class DumpCommand extends Command {
    public DumpCommand(String name) {
        super(name);
        this.description = "Universal Dump, which will print the information you need locally!";
        this.usageMessage = "/mohist *";
    }

    private List<String> params = Arrays.asList("potions", "enchants", "cbcmds", "modscmds", "entitytypes", "biomes");

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
                dumpPotions(sender);
                break;
            case "enchants":
                dumpEnchant(sender);
                break;
            case "cbcmds":
                dumpCBCommands(sender);
                break;
            case "modscmds":
                dumpModsCommands(sender);
                break;
            case "entitytypes":
                dumpEntityTypes(sender);
                break;
            case "biomes":
                dumpBiomes(sender);
                break;
            default:
                sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
                return false;
        }
        return false;
    }

    public static void dumpPotions(CommandSender sender){
        StringBuilder sb = new StringBuilder();
        for (PotionEffectType pet : PotionEffectType.values()) {
            if (pet != null) {
                sb.append(pet.toString() + "\n");
            }
        }
        for (PotionType pet : PotionType.values()) {
            if (pet != null) {
                sb.append(pet.toString() + "\n");
            }
        }
        try{
            FileUtils.writeByteArrayToFile(new File("dump", "potions.mo"), sb.toString().getBytes(StandardCharsets.UTF_8));
            sender.sendMessage("Ok");
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    private void dumpEnchant(CommandSender sender) {
        StringBuilder sb = new StringBuilder();
        for (Enchantment ench : Enchantment.values()) {
            sb.append(ench.toString() + "\n");
        }
        try {
            FileUtils.writeByteArrayToFile(new File("dump", "enchant.mo"), sb.toString().getBytes(StandardCharsets.UTF_8));
            sender.sendMessage("Ok");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void dumpEntityTypes(CommandSender sender) {
        StringBuilder sb = new StringBuilder();
        for (EntityType ent : EntityType.values()) {
            sb.append(ent.toString() + "\n");
        }
        try {
            FileUtils.writeByteArrayToFile(new File("dump", "entitytypes.mo"), sb.toString().getBytes(StandardCharsets.UTF_8));
            sender.sendMessage("Ok");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void dumpCBCommands(CommandSender sender) {
        StringBuilder sb = new StringBuilder();
        for (Command per : MinecraftServer.getServerInst().server.getCommandMap().getCommands()) {
            sb.append(per.getName() + ": " + per.getPermission() + "\n");
        }
        try {
            FileUtils.writeByteArrayToFile(new File("dump", "cbcommands.mo"), sb.toString().getBytes(StandardCharsets.UTF_8));
            sender.sendMessage("Ok");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void dumpModsCommands(CommandSender sender) {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String,String> m: ServerAPI.forgecmdper.entrySet()){
            sb.append(m.getKey() + ": " + m.getValue() + "\n");
        }
        try {
            FileUtils.writeByteArrayToFile(new File("dump", "modscommands.mo"), sb.toString().getBytes(StandardCharsets.UTF_8));
            sender.sendMessage("Ok");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void dumpBiomes(CommandSender sender) {
        StringBuilder sb = new StringBuilder();
        for (Biome biome : Biome.values()) {
            sb.append(biome.toString() + "\n");
        }
        try {
            FileUtils.writeByteArrayToFile(new File("dump", "biomes.mo"), sb.toString().getBytes(StandardCharsets.UTF_8));
            sender.sendMessage("Ok");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
