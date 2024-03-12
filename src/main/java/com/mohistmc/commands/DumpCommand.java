/*
 * Mohist - MohistMC
 * Copyright (C) 2018-2024.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.mohistmc.commands;

import com.mohistmc.MohistMC;
import com.mohistmc.api.ChatComponentAPI;
import com.mohistmc.api.ServerAPI;
import com.mohistmc.tools.HasteUtils;
import com.mohistmc.util.I18n;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.minecraft.advancements.Advancement;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.io.FileUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
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
import org.jetbrains.annotations.NotNull;

public class DumpCommand extends Command {
    private final List<String> tab_cmd = Arrays.asList("potions", "effect", "particle", "enchants", "cbcmds", "modscmds", "entitytypes", "biomes", "pattern", "worldgen", "worldtype", "material", "channels", "advancements");
    private final List<String> tab_mode = Arrays.asList("file", "web");

    public DumpCommand(String name) {
        super(name);
        this.description = I18n.as("dumpcmd.description");
        this.usageMessage = "/dump <file|web> [potions|enchants|cbcmds|modscmds|entitytypes|biomes|pattern|worldgen|worldtype|material|channels|advancements]";
        this.setPermission("mohist.command.dump");
    }

    @Override
    public @NotNull List<String> tabComplete(CommandSender sender, @NotNull String alias, String[] args) {
        List<String> list = new ArrayList<>();
        if ((sender.isOp() || testPermission(sender))) {
            switch (args.length) {
                case 2 -> {
                    for (String param : tab_cmd) {
                        if (param.toLowerCase().startsWith(args[1].toLowerCase())) {
                            list.add(param);
                        }
                    }
                }
                case 1 -> {
                    for (String param : tab_mode) {
                        if (param.toLowerCase().startsWith(args[0].toLowerCase())) {
                            list.add(param);
                        }
                    }
                }
            }
        }

        return list;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {
        if (!testPermission(sender)) {
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }
        if (args.length == 2) {
            String mode = args[0];
            switch (args[1].toLowerCase(Locale.ENGLISH)) {
                case "potions" -> dumpPotions(sender, mode);
                case "effect" -> dumpEffect(sender, mode);
                case "particle" -> dumpParticle(sender, mode);
                case "enchants" -> dumpEnchant(sender, mode);
                case "cbcmds" -> dumpCBCommands(sender, mode);
                case "modscmds" -> dumpModsCommands(sender, mode);
                case "entitytypes" -> dumpEntityTypes(sender, mode);
                case "biomes" -> dumpBiomes(sender, mode);
                case "pattern" -> dumpPattern(sender, mode);

                /*case "worldgen":
                    dumpWorldGen(sender, mode);
                    break;*/
                case "worldtype" -> dumpWorldType(sender, mode);
                case "material" -> dumpMaterial(sender, mode);
                case "channels" -> dumpChannels(sender, mode);
                case "advancements" -> dumpAdvancements(sender, mode);
                default -> {
                    sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
                    return false;
                }
            }
        }
        return false;
    }

    private void dumpEffect(CommandSender sender, String mode) {
        StringBuilder sb = new StringBuilder();
        for (PotionEffectType pet : PotionEffectType.values()) {
            sb.append(pet).append("\n");
        }
        dump(sender, "effect", sb, mode);
    }

    private void dumpPotions(CommandSender sender, String mode) {
        StringBuilder sb = new StringBuilder();
        for (PotionType pet : PotionType.values()) {
            if (pet != null) {
                sb.append(pet).append("\n");
            }
        }
        dump(sender, "potions", sb, mode);
    }

    private void dumpParticle(CommandSender sender, String mode) {
        StringBuilder sb = new StringBuilder();
        for (Particle pet : Particle.values()) {
            if (pet != null) {
                sb.append(pet).append("\n");
            }
        }
        dump(sender, "particle", sb, mode);
    }

    private void dumpEnchant(CommandSender sender, String mode) {
        StringBuilder sb = new StringBuilder();
        for (Enchantment ench : Enchantment.values()) {
            sb.append(ench).append("\n");
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
        for (Map.Entry<String, String> m : ServerAPI.forgecmdper.entrySet()) {
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

    private void dumpPattern(CommandSender sender, String mode) {
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

    private void dumpWorldType(CommandSender sender, String mode) {
        StringBuilder sb = new StringBuilder();
        for (WorldType type : WorldType.values()) {
            String key = type.getName();
            sb.append(type).append("-").append(key).append("\n");
        }
        dump(sender, "worldtype", sb, mode);
    }

    private void dumpMaterial(CommandSender sender, String mode) {
        StringBuilder sb = new StringBuilder();
        for (Material material : Material.values()) {
            if (!material.isLegacy()) {
                String key = material.getKey().toString();
                sb.append(material).append("-").append(key).append("\n");
            }
        }
        dump(sender, "material", sb, mode);
    }

    private void dumpChannels(CommandSender sender, String mode) {
        StringBuilder sb = new StringBuilder();
        for (String channel : ServerAPI.channels_Outgoing()) {
            sb.append(channel).append("\n");
        }
        for (String channel : ServerAPI.channels_Incoming()) {
            sb.append(channel).append("\n");
        }
        dump(sender, "channels", sb, mode);
    }

    private void dumpAdvancements(CommandSender sender, String mode) {
        StringBuilder sb = new StringBuilder();
        for (Advancement channel : ServerAPI.getNMSServer().getAdvancements().getAllAdvancements()) {
            sb.append(channel.getId()).append("\n");
        }
        dump(sender, "advancements", sb, mode);
    }

    private void dumpmsg(CommandSender sender, String path, String type) {
        sender.sendMessage(ChatColor.GREEN + "Successfully dump " + type + ", output path: " + path);
    }

    private void dump(CommandSender sender, String type, StringBuilder sb, String mode) {
        switch (mode) {
            case "file" -> saveToF(type, sb, sender);
            case "web" -> {
                try {
                    String url = HasteUtils.pasteMohist(sb.toString());
                    if (sender instanceof Player p) {
                        ChatComponentAPI.sendClickOpenURLChat(p, ChatColor.GREEN + "Successfully dump " + type + ", output path: " + ChatColor.DARK_GRAY + url, url, url);
                    } else {
                        dumpmsg(sender, url, type);
                    }
                } catch (IOException e) {
                    sender.sendMessage(ChatColor.RED + "Failed to upload to hastebin.");
                    saveToF(type, sb, sender);
                }
            }
        }
    }

    private void saveToF(String type, StringBuilder sb, CommandSender sender) {
        File file = new File("dump", type + ".txt");
        writeByteArrayToFile(file, sb);
        dumpmsg(sender, file.getAbsolutePath(), type);
    }

    protected void writeByteArrayToFile(File file, StringBuilder sb) {
        try {
            FileUtils.writeByteArrayToFile(file, sb.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            MohistMC.LOGGER.error(e);
        }
    }
}
