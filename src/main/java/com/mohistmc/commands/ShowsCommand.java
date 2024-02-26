package com.mohistmc.commands;

import com.mohistmc.api.ItemAPI;
import com.mohistmc.api.gui.GUIItem;
import com.mohistmc.api.gui.ItemStackFactory;
import com.mohistmc.api.gui.Warehouse;
import com.mohistmc.util.I18n;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author Mgazul by MohistMC
 * @date 2023/8/1 20:00:00
 */
public class ShowsCommand extends Command {

    public ShowsCommand(String name) {
        super(name);
        this.description = "Mohist shows commands";
        this.usageMessage = "/shows [sound|entitys|blockentitys]";
        this.setPermission("mohist.command.shows");
    }

    private final List<String> params = List.of("sound", "entitys", "blockentitys");

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
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
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!testPermission(sender)) {
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }


        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + I18n.as("error.notplayer"));
            return false;
        }

        switch (args[0].toLowerCase(Locale.ENGLISH)) {
            case "sound" -> {
                Warehouse wh = new Warehouse("Sounds");
                wh.getGUI().setItem(47, new GUIItem(new ItemStackFactory(Material.REDSTONE)
                        .setDisplayName("§cStop all sounds")
                        .toItemStack()) {
                    @Override
                    public void ClickAction(ClickType type, Player u, ItemStack itemStack) {
                        u.stopAllSounds();
                    }
                });
                for (Sound s : Sound.values()) {
                    wh.addItem(new GUIItem(new ItemStackFactory(Material.NOTE_BLOCK)
                            .setDisplayName(s.name())
                            .toItemStack()) {
                        @Override
                        public void ClickAction(ClickType type, Player u, ItemStack itemStack) {
                            player.playSound(player.getLocation(), s, 1f, 1.0f);
                        }
                    });
                }
                wh.openGUI(player);
                return true;
            }
            case "entitys" -> {

                Map<EntityType, Integer> collect = player.getWorld().getEntities().stream().collect(Collectors.toMap(Entity::getType, entity -> 1, Integer::sum));

                List<Map.Entry<EntityType, Integer>> infoIds = new ArrayList<>(collect.entrySet());
                infoIds.sort((o1, o2) -> {
                    Integer p1 = o1.getValue();
                    Integer p2 = o2.getValue();
                    return p2 - p1;
                });

                LinkedHashMap<EntityType, Integer> newMap = new LinkedHashMap<>();
                AtomicInteger allSize = new AtomicInteger(0);
                for (Map.Entry<EntityType, Integer> entity : infoIds) {
                    newMap.put(entity.getKey(), entity.getValue());
                    allSize.addAndGet(entity.getValue());
                }

                Warehouse wh = new Warehouse("Entitys: " + allSize.getAndSet(0));
                for (Map.Entry<EntityType, Integer> s : newMap.entrySet()) {
                    wh.addItem(new GUIItem(new ItemStackFactory(ItemAPI.getEggMaterial(s.getKey()))
                            .setDisplayName("§6Size: §4" + s.getValue())
                            .setLore(List.of("§7EntityType: §2" + s.getKey().name()))
                            .toItemStack())
                    );
                }
                wh.openGUI(player);
                return true;
            }
            case "blockentitys" -> {

                Map<Material, Integer> collect = Arrays.stream(player.getWorld().getLoadedChunks()).flatMap(chunk -> Arrays.stream(chunk.getTileEntities())).map(BlockState::getBlock).collect(Collectors.toMap(Block::getType, block -> 1, Integer::sum));

                List<Map.Entry<Material, Integer>> infoIds = new ArrayList<>(collect.entrySet());
                infoIds.sort((o1, o2) -> {
                    Integer p1 = o1.getValue();
                    Integer p2 = o2.getValue();
                    return p2 - p1;
                });

                LinkedHashMap<Material, Integer> newMap = new LinkedHashMap<>();
                AtomicInteger allSize = new AtomicInteger(0);
                for (Map.Entry<Material, Integer> entity : infoIds) {
                    newMap.put(entity.getKey(), entity.getValue());
                    allSize.addAndGet(entity.getValue());
                }

                Warehouse wh = new Warehouse("BlockEntitys: " + allSize.getAndSet(0));
                for (Map.Entry<Material, Integer> s : newMap.entrySet()) {
                    Material material = s.getKey().name().contains("_WALL") ? Material.getMaterial(s.getKey().name().replace("_WALL", "")) : s.getKey();
                    wh.addItem(new GUIItem(new ItemStackFactory(material)
                            .setDisplayName("§6Size: §4" + s.getValue())
                            .setLore(List.of("§7BlockEntity: §2" + s.getKey()))
                            .toItemStack())
                    );
                }
                wh.openGUI(player);
                return true;
            }
            default -> {
                sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
                return false;
            }
        }
    }
}
