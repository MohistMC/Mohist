package org.spigotmc;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import red.mohist.Mohist;
import red.mohist.util.i18n.Message;

import java.util.List;

public class SpigotWorldConfig {

    private final String worldName;
    private final YamlConfiguration config;
    // Crop growth rates
    public int cactusModifier;
    public int caneModifier;
    public int melonModifier;
    public int mushroomModifier;
    public int pumpkinModifier;
    public int saplingModifier;
    public int wheatModifier;
    public int wartModifier;
    public int vineModifier;
    public int cocoaModifier;
    public double itemMerge;
    public double expMerge;
    public int viewDistance;
    public byte mobSpawnRange;
    public int itemDespawnRate;
    public int animalActivationRange = 32;
    public int monsterActivationRange = 32;
    public int miscActivationRange = 16;
    public int waterActivationRange = 16; // Paper
    public boolean tickInactiveVillagers = true;
    public int playerTrackingRange = 48;
    public int animalTrackingRange = 48;
    public int monsterTrackingRange = 48;
    public int miscTrackingRange = 32;
    public int otherTrackingRange = 64;
    public int hopperTransfer;
    public int hopperCheck;
    public int hopperAmount;
    public boolean randomLightUpdates;
    public boolean saveStructureInfo;
    public int arrowDespawnRate;
    public boolean zombieAggressiveTowardsVillager;
    public boolean nerfSpawnerMobs;
    public boolean enableZombiePigmenPortalSpawns;
    public int dragonDeathSoundRadius;
    public int witherSpawnSoundRadius;
    public int villageSeed;
    public int largeFeatureSeed;
    public int monumentSeed;
    public int slimeSeed;
    public float jumpWalkExhaustion;
    public float jumpSprintExhaustion;
    public float combatExhaustion;
    public float regenExhaustion;
    public float swimMultiplier;
    public float sprintMultiplier;
    public float otherMultiplier;
    public int currentPrimedTnt = 0;
    public int maxTntTicksPerTick;
    public int hangingTickFrequency;
    public int tileMaxTickTime = 1000;
    public int entityMaxTickTime = 1000;
    public double squidSpawnRangeMin;
    private boolean verbose;

    public SpigotWorldConfig(String worldName) {
        this.worldName = worldName;
        this.config = SpigotConfig.config;
        init();
    }

    public void init() {
        this.verbose = getBoolean("verbose", true);

        Object[] p = {worldName};
        Mohist.LOGGER.info(Message.getFormatString("world.settings", p));
        SpigotConfig.readConfig(SpigotWorldConfig.class, this);
    }

    private void log(String s) {
        if (verbose) {
            Bukkit.getLogger().info(s);
        }
    }

    private void set(String path, Object val) {
        config.set("world-settings.default." + path, val);
    }

    public boolean getBoolean(String path, boolean def) {
        config.addDefault("world-settings.default." + path, def);
        return config.getBoolean("world-settings." + worldName + "." + path, config.getBoolean("world-settings.default." + path));
    }

    public double getDouble(String path, double def) {
        config.addDefault("world-settings.default." + path, def);
        return config.getDouble("world-settings." + worldName + "." + path, config.getDouble("world-settings.default." + path));
    }

    public int getInt(String path, int def) {
        config.addDefault("world-settings.default." + path, def);
        return config.getInt("world-settings." + worldName + "." + path, config.getInt("world-settings.default." + path));
    }

    public <T> List getList(String path, T def) {
        config.addDefault("world-settings.default." + path, def);
        return config.getList("world-settings." + worldName + "." + path, config.getList("world-settings.default." + path));
    }

    public String getString(String path, String def) {
        config.addDefault("world-settings.default." + path, def);
        return config.getString("world-settings." + worldName + "." + path, config.getString("world-settings.default." + path));
    }

    private int getAndValidateGrowth(String crop) {
        int modifier = getInt("growth." + crop.toLowerCase(java.util.Locale.ENGLISH) + "-modifier", 100);
        if (modifier == 0) {
            log(Message.getFormatString("growth.modifier.defaulting", new Object[]{crop}));
            modifier = 100;
        }
        log(Message.getFormatString("growth.modifier", new Object[]{crop, modifier}));

        return modifier;
    }

    private void growthModifiers() {
        cactusModifier = getAndValidateGrowth("Cactus");
        caneModifier = getAndValidateGrowth("Cane");
        melonModifier = getAndValidateGrowth("Melon");
        mushroomModifier = getAndValidateGrowth("Mushroom");
        pumpkinModifier = getAndValidateGrowth("Pumpkin");
        saplingModifier = getAndValidateGrowth("Sapling");
        wheatModifier = getAndValidateGrowth("Wheat");
        wartModifier = getAndValidateGrowth("NetherWart");
        vineModifier = getAndValidateGrowth("Vine");
        cocoaModifier = getAndValidateGrowth("Cocoa");
    }

    private void itemMerge() {
        itemMerge = getDouble("merge-radius.item", 2.5);
        log(Message.getFormatString("merge.radius.item", new Object[]{itemMerge}));
    }

    private void expMerge() {
        expMerge = getDouble("merge-radius.exp", 3.0);
        log(Message.getFormatString("merge.radius.exp", new Object[]{expMerge}));
    }

    private void viewDistance() {
        viewDistance = getInt("view-distance", Bukkit.getViewDistance());
        log(Message.getFormatString("view.distance", new Object[]{viewDistance}));
    }

    private void mobSpawnRange() {
        mobSpawnRange = (byte) getInt("mob-spawn-range", 4);
        log(Message.getFormatString("mob.spawn.range", new Object[]{mobSpawnRange}));
    }

    private void itemDespawnRate() {
        itemDespawnRate = getInt("item-despawn-rate", 6000);
        log(Message.getFormatString("item.despawn.rate", new Object[]{itemDespawnRate}));
    }

    private void activationRange() {
        animalActivationRange = getInt("entity-activation-range.animals", animalActivationRange);
        monsterActivationRange = getInt("entity-activation-range.monsters", monsterActivationRange);
        miscActivationRange = getInt("entity-activation-range.misc", miscActivationRange);
        waterActivationRange = getInt("entity-activation-range.water", waterActivationRange); // Paper
        tickInactiveVillagers = getBoolean("entity-activation-range.tick-inactive-villagers", tickInactiveVillagers);
        log(Message.getFormatString("entity.activation.range", new Object[]{animalActivationRange, monsterActivationRange, miscActivationRange, tickInactiveVillagers}));
    }

    private void trackingRange() {
        playerTrackingRange = getInt("entity-tracking-range.players", playerTrackingRange);
        animalTrackingRange = getInt("entity-tracking-range.animals", animalTrackingRange);
        monsterTrackingRange = getInt("entity-tracking-range.monsters", monsterTrackingRange);
        miscTrackingRange = getInt("entity-tracking-range.misc", miscTrackingRange);
        otherTrackingRange = getInt("entity-tracking-range.other", otherTrackingRange);
        log(Message.getFormatString("entity.tracking.range", new Object[]{playerTrackingRange, animalTrackingRange, monsterTrackingRange, miscTrackingRange, otherTrackingRange}));
    }

    private void hoppers() {
        // Set the tick delay between hopper item movements
        hopperTransfer = getInt("ticks-per.hopper-transfer", 8);
        if (SpigotConfig.version < 11) {
            set("ticks-per.hopper-check", 1);
        }
        hopperCheck = getInt("ticks-per.hopper-check", 1);
        hopperAmount = getInt("hopper-amount", 1);
        log(Message.getFormatString("ticks.per.hopper.transfer", new Object[]{hopperTransfer, hopperCheck, hopperAmount}));
    }

    private void lightUpdates() {
        randomLightUpdates = getBoolean("random-light-updates", false);
        log(Message.getFormatString("random.light.updates", new Object[]{randomLightUpdates}));
    }

    private void structureInfo() {
        saveStructureInfo = getBoolean("save-structure-info", true);
        log(Message.getFormatString("save.structure.info", new Object[]{saveStructureInfo}));
        if (!saveStructureInfo) {
            log(Message.getString("save.structure.info.error"));
            log(Message.getString("save.structure.info.error"));
        }
    }

    private void arrowDespawnRate() {
        arrowDespawnRate = getInt("arrow-despawn-rate", 1200);
        log(Message.getFormatString("arrow.despawn.rate", new Object[]{String.valueOf(arrowDespawnRate)}));
    }

    private void zombieAggressiveTowardsVillager() {
        zombieAggressiveTowardsVillager = getBoolean("zombie-aggressive-towards-villager", true);
        log(Message.getFormatString("zombie.aggressive.towards.villager", new Object[]{zombieAggressiveTowardsVillager}));
    }

    private void nerfSpawnerMobs() {
        nerfSpawnerMobs = getBoolean("nerf-spawner-mobs", false);
        log(Message.getFormatString("nerf.spawner.mobs", new Object[]{nerfSpawnerMobs}));
    }

    private void enableZombiePigmenPortalSpawns() {
        enableZombiePigmenPortalSpawns = getBoolean("enable-zombie-pigmen-portal-spawns", true);
        log(Message.getFormatString("enable.zombie.pigmen.portal.spawns", new Object[]{enableZombiePigmenPortalSpawns}));
    }

    private void keepDragonDeathPerWorld() {
        dragonDeathSoundRadius = getInt("dragon-death-sound-radius", 0);
    }

    private void witherSpawnSoundRadius() {
        witherSpawnSoundRadius = getInt("wither-spawn-sound-radius", 0);
    }

    private void initWorldGenSeeds() {
        villageSeed = getInt("seed-village", 10387312);
        largeFeatureSeed = getInt("seed-feature", 14357617);
        monumentSeed = getInt("seed-monument", 10387313);
        slimeSeed = getInt("seed-slime", 987234911);
        log(Message.getFormatString("custom.map.seeds", new Object[]{String.valueOf(villageSeed), String.valueOf(largeFeatureSeed), String.valueOf(monumentSeed), String.valueOf(slimeSeed)}));
    }

    private void initHunger() {
        if (SpigotConfig.version < 10) {
            set("hunger.walk-exhaustion", null);
            set("hunger.sprint-exhaustion", null);
            set("hunger.combat-exhaustion", 0.1);
            set("hunger.regen-exhaustion", 6.0);
        }

        jumpWalkExhaustion = (float) getDouble("hunger.jump-walk-exhaustion", 0.05);
        jumpSprintExhaustion = (float) getDouble("hunger.jump-sprint-exhaustion", 0.2);
        combatExhaustion = (float) getDouble("hunger.combat-exhaustion", 0.1);
        regenExhaustion = (float) getDouble("hunger.regen-exhaustion", 6.0);
        swimMultiplier = (float) getDouble("hunger.swim-multiplier", 0.01);
        sprintMultiplier = (float) getDouble("hunger.sprint-multiplier", 0.1);
        otherMultiplier = (float) getDouble("hunger.other-multiplier", 0.0);
    }

    private void maxTntPerTick() {
        if (SpigotConfig.version < 7) {
            set("max-tnt-per-tick", 100);
        }
        maxTntTicksPerTick = getInt("max-tnt-per-tick", 100);
        log(Message.getFormatString("max-tnt.per.tick", new Object[]{maxTntTicksPerTick}));
    }

    private void hangingTickFrequency() {
        hangingTickFrequency = getInt("hanging-tick-frequency", 100);
    }

    private void maxTickTimes() {
        tileMaxTickTime = getInt("max-tick-time.tile", 1000);
        entityMaxTickTime = getInt("max-tick-time.entity", 1000);
        log(Message.getFormatString("max.tick.time.tile", new Object[]{tileMaxTickTime, entityMaxTickTime}));
    }

    private void squidSpawnRange() {
        squidSpawnRangeMin = getDouble("squid-spawn-range.min", 45.0D);
    }
}
