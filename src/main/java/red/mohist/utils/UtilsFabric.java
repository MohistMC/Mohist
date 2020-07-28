package red.mohist.utils;

import org.bukkit.Difficulty;

import net.minecraft.world.GameMode;

public class UtilsFabric {

    public static String getGitHash() {
        try {
            Class<?> version = Class.forName("com.fungus_soft.bukkitfabric.GitVersion");
            return (String) version.getField("GIT_SHA").get(null);
        } catch (ClassNotFoundException | NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            return "dev";
        }
    }

    public static GameMode toFabric(org.bukkit.GameMode arg0) {
        switch (arg0) {
            case ADVENTURE:
                return GameMode.ADVENTURE;
            case CREATIVE:
                return GameMode.CREATIVE;
            case SPECTATOR:
                return GameMode.SPECTATOR;
            case SURVIVAL:
                return GameMode.SURVIVAL;
            default:
                break;
        }
        return GameMode.NOT_SET;
    }

    public static org.bukkit.GameMode fromFabric(GameMode gm) {
        switch (gm) {
            case ADVENTURE:
                return org.bukkit.GameMode.ADVENTURE;
            case CREATIVE:
                return org.bukkit.GameMode.CREATIVE;
            case NOT_SET:
                return org.bukkit.GameMode.SURVIVAL;
            case SPECTATOR:
                return org.bukkit.GameMode.SPECTATOR;
            case SURVIVAL:
                return org.bukkit.GameMode.SURVIVAL;
            default:
                break;
        }
        return org.bukkit.GameMode.SURVIVAL;
    }

    public static Difficulty fromFabric(net.minecraft.world.Difficulty difficulty) {
        return Difficulty.valueOf(difficulty.name());
    }

}