package com.mohistmc.api;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundUpdateAdvancementsPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerAPI {

    public static Map<SocketAddress, Integer> mods = new ConcurrentHashMap<>();
    public static Map<SocketAddress, List<String>> modlist = new ConcurrentHashMap<>();

    /**
     * Get Player ping
     *
     * @param player org.bukkit.entity.player
     */
    public static String getPing(Player player) {
        return String.valueOf(getNMSPlayer(player).latency);
    }

    public static ServerPlayer getNMSPlayer(Player player) {
        return ((CraftPlayer) player).getHandle();
    }

    public static Player getCBPlayer(ServerPlayer player) {
        return player.getBukkitEntity().getPlayer();
    }

    // Don't count the default number of mods
    public static int getModSize(Player player) {
        SocketAddress socketAddress = getRemoteAddress(player);
        return mods.get(socketAddress) == null ? 0 : mods.get(socketAddress) - 2;
    }

    public static List<String> getModlist(Player player) {
        SocketAddress socketAddress = getRemoteAddress(player);
        return modlist.get(socketAddress) == null ? Collections.emptyList() : modlist.get(socketAddress);
    }

    public static boolean hasMod(Player player, String modid) {
        return getModlist(player).contains(modid);
    }

    public static boolean isOp(ServerPlayer ep) {
        return getCBPlayer(ep).isOp();
    }

    public static SocketAddress getRemoteAddress(Player player) {
        return getNMSPlayer(player).connection.connection.getRemoteAddress();
    }

    public static void sendToast(Player player, ItemStack icon, String title) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        String impossible = "impossible";
        Optional<DisplayInfo> displayInfo = Optional.of(new DisplayInfo(
                net.minecraft.world.item.ItemStack.fromBukkitCopy(icon),
                Component.literal(title),
                Component.literal("."),
                null,
                FrameType.TASK,
                true,
                false,
                true));
        AdvancementRewards advancementRewards = AdvancementRewards.EMPTY;
        Optional<ResourceLocation> id = Optional.of(new ResourceLocation("custom", "custom"));
        Criterion impossibleTrigger = new Criterion(new ImpossibleTrigger.TriggerInstance());
        HashMap<String, Criterion> criteria = new HashMap<>() {{
            put(impossible, impossibleTrigger);
        }};
        String[][] requirements = {{impossible}};
        Advancement advancement = new Advancement(id.get(), null, displayInfo.get(), advancementRewards, criteria, requirements, false);
        Map<ResourceLocation, AdvancementProgress> advancementsToGrant = new HashMap<>();
        AdvancementProgress advancementProgress = new AdvancementProgress();
        advancementProgress.update(criteria, requirements);
        advancementProgress.getCriterion(impossible).grant();
        advancementsToGrant.put(id.get(), advancementProgress);
        ClientboundUpdateAdvancementsPacket packet = new ClientboundUpdateAdvancementsPacket(false, new ArrayList<>() {{
            add(advancement);
        }}, new HashSet<>(), advancementsToGrant);
        serverPlayer.connection.send(packet);
        ClientboundUpdateAdvancementsPacket packet2 = new ClientboundUpdateAdvancementsPacket(false, new ArrayList<>(), new HashSet<>() {{
            add(id.get());
        }}, new HashMap<>());
        serverPlayer.connection.send(packet2);
    }
}
