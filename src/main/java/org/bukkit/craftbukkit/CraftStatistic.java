package org.bukkit.craftbukkit;

import com.google.common.base.CaseFormat;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityList.EntityEggInfo;
import net.minecraft.stats.StatList;
import org.bukkit.Achievement;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;

public class CraftStatistic {
    private static final BiMap<String, org.bukkit.Statistic> statistics;
    private static final BiMap<String, org.bukkit.Achievement> achievements;

    static {
        ImmutableMap<String, org.bukkit.Achievement> specialCases = ImmutableMap.<String, org.bukkit.Achievement> builder()
                .put("achievement.buildWorkBench", Achievement.BUILD_WORKBENCH)
                .put("achievement.diamonds", Achievement.GET_DIAMONDS)
                .put("achievement.portal", Achievement.NETHER_PORTAL)
                .put("achievement.ghast", Achievement.GHAST_RETURN)
                .put("achievement.theEnd", Achievement.END_PORTAL)
                .put("achievement.theEnd2", Achievement.THE_END)
                .put("achievement.blazeRod", Achievement.GET_BLAZE_ROD)
                .put("achievement.potion", Achievement.BREW_POTION)
                .build();
        ImmutableBiMap.Builder<String, org.bukkit.Statistic> statisticBuilder = ImmutableBiMap.<String, org.bukkit.Statistic>builder();
        ImmutableBiMap.Builder<String, org.bukkit.Achievement> achievementBuilder = ImmutableBiMap.<String, org.bukkit.Achievement>builder();
        for (Statistic statistic : Statistic.values()) {
            if (statistic == Statistic.PLAY_ONE_TICK) {
                statisticBuilder.put("stat.playOneMinute", statistic);
            } else {
                statisticBuilder.put("stat." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, statistic.name()), statistic);
            }
        }
        for (Achievement achievement : Achievement.values()) {
            if (specialCases.values().contains(achievement)) {
                continue;
            }
            achievementBuilder.put("achievement." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, achievement.name()), achievement);
        }

        achievementBuilder.putAll(specialCases);

        statistics = statisticBuilder.build();
        achievements = achievementBuilder.build();
    }

    private CraftStatistic() {}

    public static org.bukkit.Achievement getBukkitAchievement(net.minecraft.stats.Achievement achievement) {
        return getBukkitAchievementByName(achievement.statId);
    }

    public static org.bukkit.Achievement getBukkitAchievementByName(String name) {
        return achievements.get(name);
    }

    public static org.bukkit.Statistic getBukkitStatistic(net.minecraft.stats.StatBase statistic) {
        return getBukkitStatisticByName(statistic.statId);
    }

    public static org.bukkit.Statistic getBukkitStatisticByName(String name) {
        if (name.startsWith("stat.killEntity")) {
            name = "stat.killEntity";
        }
        if (name.startsWith("stat.entityKilledBy")) {
            name = "stat.entityKilledBy";
        }
        if (name.startsWith("stat.breakItem")) {
            name = "stat.breakItem";
        }
        if (name.startsWith("stat.useItem")) {
            name = "stat.useItem";
        }
        if (name.startsWith("stat.mineBlock")) {
            name = "stat.mineBlock";
        }
        if (name.startsWith("stat.craftItem")) {
            name = "stat.craftItem";
        }
        return statistics.get(name);
    }

    public static net.minecraft.stats.StatBase getNMSStatistic(org.bukkit.Statistic statistic) {
        return StatList.func_151177_a(statistics.inverse().get(statistic));
    }

    public static net.minecraft.stats.Achievement getNMSAchievement(org.bukkit.Achievement achievement) {
        return (net.minecraft.stats.Achievement) StatList.func_151177_a(achievements.inverse().get(achievement));
    }

    public static net.minecraft.stats.StatBase getMaterialStatistic(org.bukkit.Statistic stat, Material material) {
        try {
            if (stat == Statistic.MINE_BLOCK) {
                return StatList.mineBlockStatArray[material.getId()];
            }
            if (stat == Statistic.CRAFT_ITEM) {
                return StatList.objectCraftStats[material.getId()];
            }
            if (stat == Statistic.USE_ITEM) {
                return StatList.objectUseStats[material.getId()];
            }
            if (stat == Statistic.BREAK_ITEM) {
                return StatList.objectBreakStats[material.getId()];
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
        return null;
    }

    public static net.minecraft.stats.StatBase getEntityStatistic(org.bukkit.Statistic stat, EntityType entity) {
        EntityEggInfo entityEggInfo = (EntityEggInfo) EntityList.entityEggs.get(Integer.valueOf(entity.getTypeId()));

        if (entityEggInfo != null) {
            return entityEggInfo.field_151512_d;
        }
        return null;
    }

    public static EntityType getEntityTypeFromStatistic(net.minecraft.stats.StatBase statistic) {
        String statisticString = statistic.statId;
        return EntityType.fromName(statisticString.substring(statisticString.lastIndexOf(".") + 1));
    }

    public static Material getMaterialFromStatistic(net.minecraft.stats.StatBase statistic) {
        String statisticString = statistic.statId;
        int id;
        try {
            id = Integer.valueOf(statisticString.substring(statisticString.lastIndexOf(".") + 1));
        } catch (NumberFormatException e) {
            return null;
        }
        return Material.getMaterial(id);
    }
}