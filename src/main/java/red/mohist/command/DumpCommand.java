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
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.commons.io.FileUtils;
import org.bukkit.ChatColor;
import org.bukkit.block.Biome;
import org.bukkit.block.banner.PatternType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import red.mohist.util.i18n.Message;

public class DumpCommand extends Command {
    public DumpCommand(String name) {
        super(name);
        this.description = "Universal Dump, which will print the information you need locally!";
        this.usageMessage = "/dump [potions|enchants|cbcmds|entitytypes|biomes|pattern|worldgen]";
        this.setPermission("mohist.command.dump");
    }

    private List<String> params = Arrays.asList("potions", "enchants", "cbcmds", "entitytypes", "biomes", "pattern", "worldgen");

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        List<String> list = new ArrayList<>();
        if (args.length == 1 && (sender.isOp() || testPermission(sender))) {
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
        if (!sender.isOp() || !testPermission(sender)) {
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
            case "entitytypes":
                dumpEntityTypes(sender);
                break;
            case "biomes":
                dumpBiomes(sender);
                break;
            case "pattern":
                dumpPattern(sender);
                break;
            case "worldgen":
                dumpWorldGen(sender);
                break;
            default:
                sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
                return false;
        }
        return false;
    }

    private void dumpPotions(CommandSender sender){
        StringBuilder sb = new StringBuilder();
        for (PotionEffectType pet : PotionEffectType.values()) {
            if (pet != null) {
                sb.append(pet.toString()).append("\n");
            }
        }
        for (PotionType pet : PotionType.values()) {
            if (pet != null) {
                sb.append(pet.toString()).append("\n");
            }
        }
        try{
            File file = new File("dump", "potions.red");
            FileUtils.writeByteArrayToFile(file, sb.toString().getBytes(StandardCharsets.UTF_8));
            dumpmsg(sender, file, "potions");
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    private void dumpEnchant(CommandSender sender) {
        StringBuilder sb = new StringBuilder();
        for (Enchantment ench : Enchantment.values()) {
            sb.append(ench.toString()).append("\n");
        }
        try {
            File file = new File("dump", "enchants.red");
            FileUtils.writeByteArrayToFile(file, sb.toString().getBytes(StandardCharsets.UTF_8));
            dumpmsg(sender, file, "enchants");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void dumpEntityTypes(CommandSender sender) {
        StringBuilder sb = new StringBuilder();
        for (EntityType ent : EntityType.values()) {
            sb.append(ent.toString()).append("\n");
        }
        try {
            File file = new File("dump", "entitytypes.red");
            FileUtils.writeByteArrayToFile(file, sb.toString().getBytes(StandardCharsets.UTF_8));
            dumpmsg(sender, file, "entitytypes");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void dumpCBCommands(CommandSender sender) {
        StringBuilder sb = new StringBuilder();
        for (Command per : MinecraftServer.getServerInst().server.getCommandMap().getCommands()) {
            // Do not print if there is no permission
            if (per.getPermission() == null) {
                continue;
            }
            sb.append(per.getName()).append(": ").append(per.getPermission()).append("\n");
        }
        try {
            File file = new File("dump", "cbcommands.red");
            FileUtils.writeByteArrayToFile(file, sb.toString().getBytes(StandardCharsets.UTF_8));
            dumpmsg(sender, file, "cbcommands");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void dumpBiomes(CommandSender sender) {
        StringBuilder sb = new StringBuilder();
        for (Biome biome : Biome.values()) {
            sb.append(biome.toString()).append("\n");
        }
        try {
            File file = new File("dump", "biomes.red");
            FileUtils.writeByteArrayToFile(file, sb.toString().getBytes(StandardCharsets.UTF_8));
            dumpmsg(sender, file, "biomes");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void dumpPattern(CommandSender sender){
        StringBuilder sb = new StringBuilder();
        for (PatternType patternType : PatternType.values()) {
            String key = patternType.getIdentifier();
            sb.append(key + "_" + patternType.getByIdentifier(key)).append("\n");
        }
        try {
            File file = new File("dump", "pattern.red");
            FileUtils.writeByteArrayToFile(file, sb.toString().getBytes(StandardCharsets.UTF_8));
            dumpmsg(sender, file, "pattern");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void dumpWorldGen(CommandSender sender){
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : GameRegistry.worldGenMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                sb.append("worldgen-" + value + "-" + key).append("\n");
        }
        try {
            File file = new File("dump", "worldgen.red");
            FileUtils.writeByteArrayToFile(file, sb.toString().getBytes(StandardCharsets.UTF_8));
            dumpmsg(sender, file, "worldgen");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void dumpmsg(CommandSender sender, File file, String type){
        sender.sendMessage("Successfully dump " + type + ", output path: " + file.getAbsolutePath());
    }
}
