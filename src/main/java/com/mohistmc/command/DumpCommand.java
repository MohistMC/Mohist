package com.mohistmc.command;

import com.mohistmc.api.ChatComponentAPI;
import com.mohistmc.api.ServerAPI;
import com.mohistmc.util.HasteUtils;
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
import org.bukkit.Material;
import org.bukkit.WorldType;
import org.bukkit.block.Biome;
import org.bukkit.block.banner.PatternType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class DumpCommand extends Command {
    public DumpCommand(String name) {
        super(name);
        this.description = "Universal Dump, which will print the information you need locally!";
        this.usageMessage = "/dump <file|web> [potions|enchants|cbcmds|modscmds|entitytypes|biomes|pattern|worldgen|worldtype|material]";
        this.setPermission("mohist.command.dump");
    }

    private final List<String> tab_cmd = Arrays.asList("potions", "enchants", "cbcmds", "modscmds", "entitytypes", "biomes", "pattern", "worldgen", "worldtype", "material");
    private final List<String> tab_mode = Arrays.asList("file", "web");

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        List<String> list = new ArrayList<>();
        if ((sender.isOp() || testPermission(sender))) {
            switch (args.length) {
                case 2:
                    for (String param : tab_cmd) {
                        if (param.toLowerCase().startsWith(args[1].toLowerCase())) {
                            list.add(param);
                        }
                    }
                    break;
                case 1:
                    for (String param : tab_mode) {
                        if (param.toLowerCase().startsWith(args[0].toLowerCase())) {
                            list.add(param);
                        }
                    }
                    break;
            }
        }

        return list;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) return true;
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }
        if (args.length == 2) {
            String mode = args[0];
            switch (args[1].toLowerCase(Locale.ENGLISH)) {
                case "potions":
                    dumpPotions(sender, mode);
                    break;
                case "enchants":
                    dumpEnchant(sender, mode);
                    break;
                case "cbcmds":
                    dumpCBCommands(sender, mode);
                    break;
                case "modscmds":
                    dumpModsCommands(sender, mode);
                    break;
                case "entitytypes":
                    dumpEntityTypes(sender, mode);
                    break;
                case "biomes":
                    dumpBiomes(sender, mode);
                    break;
                case "pattern":
                    dumpPattern(sender, mode);
                    break;
                /*case "worldgen":
                    dumpWorldGen(sender, mode);
                    break;*/
                case "worldtype":
                    dumpWorldType(sender, mode);
                    break;
                case "material":
                    dumpMaterial(sender, mode);
                    break;
                default:
                    sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
                    return false;
            }
        }
        return false;
    }

    private void dumpPotions(CommandSender sender, String mode) {
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
        dump(sender, "potions", sb, mode);
    }

    private void dumpEnchant(CommandSender sender, String mode) {
        StringBuilder sb = new StringBuilder();
        for (Enchantment ench : Enchantment.values()) {
            sb.append(ench.toString()).append("\n");
        }
        dump(sender, "enchants", sb, mode);
    }

    private void dumpEntityTypes(CommandSender sender, String mode) {
        StringBuilder sb = new StringBuilder();
        for (EntityType ent : EntityType.values()) {
            sb.append(ent.toString()).append("\n");
        }
        dump(sender, "entitytypes", sb, mode);
    }

    private void dumpCBCommands(CommandSender sender, String mode) {
        StringBuilder sb = new StringBuilder();
        for (Command per : MinecraftServer.getServer().server.getCommandMap().getCommands()) {
            // Do not print if there is no permission
            if (per.getPermission() == null) {
                continue;
            }
            sb.append(per.getName()).append(": ").append(per.getPermission()).append("\n");
        }
        dump(sender, "cbcommands", sb, mode);
    }

    private void dumpModsCommands(CommandSender sender, String mode) {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String,String> m: ServerAPI.forgecmdper.entrySet()){
            sb.append(m.getKey()).append(": ").append(m.getValue()).append("\n");
        }
        dump(sender, "modscommands", sb, mode);
    }

    private void dumpBiomes(CommandSender sender, String mode) {
        StringBuilder sb = new StringBuilder();
        for (Biome biome : Biome.values()) {
            sb.append(biome.toString()).append("\n");
        }
        dump(sender, "biomes", sb, mode);
    }

    private void dumpPattern(CommandSender sender, String mode){
        StringBuilder sb = new StringBuilder();
        for (PatternType patternType : PatternType.values()) {
            String key = patternType.getIdentifier();
            sb.append(key).append("_").append(PatternType.getByIdentifier(key)).append("\n");
        }
        dump(sender, "pattern", sb, mode);
    }

    /*
    private void dumpWorldGen(CommandSender sender, String mode){
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : GameRegistry..worldGenMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                sb.append("worldgen-").append(value).append("-").append(key).append("\n");
        }
        dump(sender, "worldgen", sb, mode);
    }
     */

    private void dumpWorldType(CommandSender sender, String mode){
        StringBuilder sb = new StringBuilder();
        for (WorldType type : WorldType.values()) {
            String key = type.getName();
            sb.append(type).append("-").append(key).append("\n");
        }
        dump(sender, "worldtype", sb, mode);
    }

    private void dumpMaterial(CommandSender sender, String mode){
        StringBuilder sb = new StringBuilder();
        for (Material material : Material.values()) {
            String key = material.name();
            sb.append(material).append("-").append(key).append("\n");
        }
        dump(sender, "material", sb, mode);
    }

    private void dumpmsg(CommandSender sender, File file, String type){
        sender.sendMessage("Successfully dump " + type + ", output path: " + file.getAbsolutePath());
    }

    private void dumpmsg(CommandSender sender, String web, String type){
        sender.sendMessage("Successfully dump " + type + ", output path: " + web);
    }

  private void dump(CommandSender sender, String type, StringBuilder sb, String mode) {
    switch (mode) {
      case "file":
        saveToF("dump", type + ".red", sb, sender);
        break;
      case "web":
        try {
          String url = HasteUtils.pasteMohist(sb.toString());
          if(sender instanceof Player) {
            Player p = (Player) sender;
            ChatComponentAPI.sendClickOpenURLChat(p, "Successfully dump " + type + ", output path: " + url, url, url);
          } else {
            dumpmsg(sender, url, type);
          }
        } catch (IOException e) {
          sender.sendMessage("Failed to upload to hastebin.");
          saveToF("dump", type + ".red", sb, sender);
        }
        break;
    }
  }

  private void saveToF(String parent, String child, StringBuilder sb, CommandSender sender) {
    File file = new File(parent, child);
    writeByteArrayToFile(file, sb);
    dumpmsg(sender, file, child.replace(".red", ""));
  }

    protected void writeByteArrayToFile(File file, StringBuilder sb){
        try {
            FileUtils.writeByteArrayToFile(file, sb.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
