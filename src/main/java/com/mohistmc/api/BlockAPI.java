package com.mohistmc.api;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.block.Biome;

public class BlockAPI {

    public static Map<net.minecraft.world.biome.Biome, String> biome = new ConcurrentHashMap();
}
