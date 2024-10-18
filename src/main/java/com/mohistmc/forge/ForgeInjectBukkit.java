package com.mohistmc.forge;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import com.mohistmc.MohistMC;
import com.mohistmc.api.ServerAPI;
import com.mohistmc.dynamicenum.MohistDynamEnum;
import com.mohistmc.bukkit.potion.MohistPotionEffect;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatType;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.TrappedChestBlockEntity;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.bukkit.Art;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_18_R2.block.CraftBlockStates;
import org.bukkit.craftbukkit.v1_18_R2.block.CraftChest;
import org.bukkit.craftbukkit.v1_18_R2.block.CraftSign;
import org.bukkit.craftbukkit.v1_18_R2.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.v1_18_R2.potion.CraftPotionUtil;
import org.bukkit.craftbukkit.v1_18_R2.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_18_R2.util.CraftNamespacedKey;
import org.bukkit.craftbukkit.v1_18_R2.util.CraftSpawnCategory;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.entity.Villager;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class ForgeInjectBukkit {

    public static BiMap<ResourceKey<LevelStem>, World.Environment> environment =
            HashBiMap.create(ImmutableMap.<ResourceKey<LevelStem>, World.Environment>builder()
                    .put(LevelStem.OVERWORLD, World.Environment.NORMAL)
                    .put(LevelStem.NETHER, World.Environment.NETHER)
                    .put(LevelStem.END, World.Environment.THE_END)
                    .build());

    public static BiMap<World.Environment, ResourceKey<LevelStem>> environment0 =
            HashBiMap.create(ImmutableMap.<World.Environment, ResourceKey<LevelStem>>builder()
                    .put(World.Environment.NORMAL, LevelStem.OVERWORLD)
                    .put(World.Environment.NETHER, LevelStem.NETHER)
                    .put(World.Environment.THE_END, LevelStem.END)
                    .build());

    public static Map<Villager.Profession, ResourceLocation> profession = new HashMap<>();
    public static Map<org.bukkit.attribute.Attribute, ResourceLocation> attributemap = new HashMap<>();
    public static Map<StatType<?>, Statistic> statisticMap = new HashMap<>();
    public static Map<net.minecraft.world.level.biome.Biome, Biome> biomeBiomeMap = new HashMap<>();


    public static void init() {
        addEnumMaterialInItems();
        addEnumMaterialsInBlocks();
        addEnumBiome();
        addEnumEnchantment();
        addEnumEffectAndPotion();
        addEnumEntity();
        addEnumVillagerProfession();
        addEnumArt();
        addEnumParticle();
        addStatistic();
        loadSpawnCategory();
        addPose();
    }


    public static void addEnumMaterialInItems() {
        var registry = ForgeRegistries.ITEMS;
        for (Item item : registry) {
            ResourceLocation resourceLocation = registry.getKey(item);
            if (isMods(resourceLocation)) {
                // inject item materials into Bukkit for FML
                String materialName = normalizeName(resourceLocation.toString());
                int id = Item.getId(item);
                Material material = Material.addMaterial(materialName, id, item.getMaxStackSize(), false, true, resourceLocation);

                CraftMagicNumbers.ITEM_MATERIAL.put(item, material);
                CraftMagicNumbers.MATERIAL_ITEM.put(material, item);
                MohistMC.LOGGER.debug("Save-ITEM: " + material.name() + " - " + material.key);
            }
        }
    }


    public static void addEnumMaterialsInBlocks() {
        var registry = ForgeRegistries.BLOCKS;
        for (Block block : registry) {
            ResourceLocation resourceLocation = registry.getKey(block);
            if (isMods(resourceLocation)) {
                // inject block materials into Bukkit for FML
                String materialName = normalizeName(resourceLocation.toString());
                int id = Item.getId(block.asItem());
                Item item = Item.byId(id);
                Material material = Material.addMaterial(materialName, id, item.getMaxStackSize(), true, false, resourceLocation);

                if (material != null) {
                    CraftMagicNumbers.BLOCK_MATERIAL.put(block, material);
                    CraftMagicNumbers.MATERIAL_BLOCK.put(material, block);
                    if (block.defaultBlockState().is(BlockTags.SIGNS)) {
                        CraftBlockStates.register(material, CraftSign.class, CraftSign::new, SignBlockEntity::new);
                    }else if (block instanceof SignBlock signBlock) {
                        BlockEntity blockEntity = signBlock.newBlockEntity(BlockPos.ZERO, block.defaultBlockState());
                        if (blockEntity instanceof SignBlockEntity) {
                            CraftBlockStates.register(material, CraftSign.class, CraftSign::new, SignBlockEntity::new);
                        }
                    } else if (block instanceof ChestBlock chestBlock) {
                        BlockEntity blockEntity = chestBlock.newBlockEntity(BlockPos.ZERO, block.defaultBlockState());
                        if (blockEntity instanceof TrappedChestBlockEntity) {
                            CraftBlockStates.register(material, CraftChest.class, CraftChest::new, TrappedChestBlockEntity::new);
                        } else if (blockEntity instanceof ChestBlockEntity) {
                            CraftBlockStates.register(material, CraftChest.class, CraftChest::new, ChestBlockEntity::new);
                        }
                    }
                    MohistMC.LOGGER.debug("Save-BLOCK:" + material.name() + " - " + material.key);
                }
            }
        }
    }

    public static void addEnumEnchantment() {
        // Enchantment
        for (Enchantment enchantment : ForgeRegistries.ENCHANTMENTS) {
            org.bukkit.enchantments.Enchantment.registerEnchantment(new CraftEnchantment(enchantment));
        }
        org.bukkit.enchantments.Enchantment.stopAcceptingRegistrations();
    }

    public static void addEnumEffectAndPotion() {
        // Points
        for (MobEffect effect : ForgeRegistries.MOB_EFFECTS) {
            try {
                String name = normalizeName(effect.getRegistryName().toString());
                MohistPotionEffect pet = new MohistPotionEffect(effect, name);
                PotionEffectType.registerPotionEffectType(pet);
                MohistMC.LOGGER.debug("Registered {} as potion {}", effect.getRegistryName(), effect);
            } catch (Exception e) {
                MohistMC.LOGGER.error("Failed to register potion type {}: {}", effect.getRegistryName(), e);
            }
        }
        PotionEffectType.stopAcceptingRegistrations();
        var registry = ForgeRegistries.POTIONS;
        for (Potion potion : ForgeRegistries.POTIONS) {
            ResourceLocation resourceLocation = registry.getKey(potion);
            if (CraftPotionUtil.toBukkit(resourceLocation.toString()).getType() == PotionType.UNCRAFTABLE && potion != Potions.EMPTY) {
                String name = normalizeName(resourceLocation.toString());
                MobEffectInstance effectInstance = potion.getEffects().isEmpty() ? null : potion.getEffects().get(0);
                PotionType potionType = MohistDynamEnum.addEnum(PotionType.class, name, Arrays.asList(PotionEffectType.class, Boolean.TYPE, Boolean.TYPE),  Arrays.asList(effectInstance == null ? null : PotionEffectType.getById(MobEffect.getId(effectInstance.getEffect())), false, false));
                if (potionType != null) {
                    MohistMC.LOGGER.debug("Save-PotionType:" + name + " - " + potionType.name());
                }
            }
        }
    }

    public static void addEnumParticle() {
        var registry = ForgeRegistries.PARTICLE_TYPES;
        for (ParticleType<?> particleType : ForgeRegistries.PARTICLE_TYPES) {
            ResourceLocation resourceLocation = registry.getKey(particleType);
            String name = normalizeName(resourceLocation.toString());
            if (!resourceLocation.getNamespace().equals(NamespacedKey.MINECRAFT)) {
                Particle particle = MohistDynamEnum.addEnum(Particle.class, name);
                if (particle != null) {
                    org.bukkit.craftbukkit.v1_18_R2.CraftParticle.putParticles(particle, resourceLocation);
                    MohistMC.LOGGER.debug("Save-ParticleType:" + name + " - " + particle.name());
                }
            }
        }
    }

    public static void addEnumBiome() {
        List<String> map = new ArrayList<>();
        var registry = ForgeRegistries.BIOMES;
        for (net.minecraft.world.level.biome.Biome biome : registry) {
            ResourceLocation resourceLocation = registry.getKey(biome);
            String biomeName = normalizeName(resourceLocation.toString());
            if (isMods(resourceLocation) && !map.contains(biomeName)) {
                map.add(biomeName);
                org.bukkit.block.Biome biomeCB = MohistDynamEnum.addEnum(org.bukkit.block.Biome.class, biomeName);
                biomeBiomeMap.put(biome, biomeCB);
                MohistMC.LOGGER.debug("Save-BIOME:" + biomeCB.name() + " - " + biomeName);
            }
        }
        map.clear();
    }


    public static void addEnumEnvironment(Registry<LevelStem> registry) {
        int i = World.Environment.values().length;
        for (Map.Entry<ResourceKey<LevelStem>, LevelStem> entry : registry.entrySet()) {
            ResourceKey<LevelStem> key = entry.getKey();
            World.Environment environment1 = environment.get(key);
            if (environment1 == null) {
                String name = normalizeName(key.location().toString());
                int id = i - 1;
                environment1 = MohistDynamEnum.addEnum(World.Environment.class, name, List.of(Integer.TYPE), List.of(id));
                environment.put(key, environment1);
                environment0.put(environment1, key);
                MohistMC.LOGGER.debug("Registered forge DimensionType as environment {}", environment1);
                i++;
            }
        }
    }


    // TODO Get mods type?
    public static WorldType addEnumWorldType(String name) {
        WorldType worldType = MohistDynamEnum.addEnum(WorldType.class, name, List.of(String.class), List.of(name));
        Map<String, WorldType> BY_NAME = ObfuscationReflectionHelper.getPrivateValue(WorldType.class, null, "BY_NAME");
        BY_NAME.put(name.toUpperCase(), worldType);
        return worldType;
    }

    public static void addEnumEntity() {
        var registry = ForgeRegistries.ENTITIES;
        for (net.minecraft.world.entity.EntityType<?> entity : registry) {
            ResourceLocation resourceLocation = registry.getKey(entity);
            NamespacedKey key = CraftNamespacedKey.fromMinecraft(resourceLocation);
            String entityType = normalizeName(resourceLocation.toString());
            if (isMods(resourceLocation)) {
                int typeId = entityType.hashCode();
                EntityType bukkitType = MohistDynamEnum.addEnum(EntityType.class, entityType, List.of(String.class, Class.class, Integer.TYPE, Boolean.TYPE), List.of(entityType.toLowerCase(), Entity.class, typeId, false));
                if (bukkitType != null) {
                    bukkitType.key = key;
                    EntityType.NAME_MAP.put(entityType.toLowerCase(), bukkitType);
                    EntityType.ID_MAP.put((short) typeId, bukkitType);
                    ServerAPI.entityTypeMap.put(entity, entityType);
                }
            } else {
                ServerAPI.entityTypeMap.put(entity, normalizeName(resourceLocation.getPath().toString()));
            }
        }
    }

    public static void addEnumVillagerProfession() {
        var registry = ForgeRegistries.PROFESSIONS;
        for (VillagerProfession villagerProfession : registry) {
            ResourceLocation resourceLocation = registry.getKey(villagerProfession);
            if (isMods(resourceLocation)) {
                String name = normalizeName(resourceLocation.toString());
                Villager.Profession vp = MohistDynamEnum.addEnum(Villager.Profession.class, name);
                profession.put(vp, resourceLocation);
                MohistMC.LOGGER.debug("Registered forge VillagerProfession as Profession {}", vp.name());
            }
        }
    }

    public static void addEnumAttribute() {
        var registry = ForgeRegistries.ATTRIBUTES;
        for (Attribute attribute : registry) {
            ResourceLocation resourceLocation = registry.getKey(attribute);
            String name = normalizeName(resourceLocation.getPath());
            if (isMods(resourceLocation)) {
                org.bukkit.attribute.Attribute ab = MohistDynamEnum.addEnum(org.bukkit.attribute.Attribute.class, name, List.of(String.class), List.of());
                attributemap.put(ab, resourceLocation);
                MohistMC.LOGGER.debug("Registered forge Attribute as Attribute(Bukkit) {}", ab.name());
            }
        }
    }

    public static void addStatistic() {
        var registry = ForgeRegistries.STAT_TYPES;
        for (StatType<?> statType : registry) {
            ResourceLocation resourceLocation = registry.getKey(statType);
            String name = normalizeName(resourceLocation.getPath());
            if (isMods(resourceLocation)) {
                Statistic statistic = MohistDynamEnum.addEnum(Statistic.class, name);
                statisticMap.put(statType, statistic);
                MohistMC.LOGGER.debug("Registered forge StatType as Statistic(Bukkit) {}", statistic.name());
            }
        }
    }

    private static void loadSpawnCategory() {
        for (MobCategory category : MobCategory.values()) {
            try {
                CraftSpawnCategory.toBukkit(category);
            } catch (Exception e) {
                String name = category.name();
                SpawnCategory spawnCategory = MohistDynamEnum.addEnum(SpawnCategory.class, name);
                MohistMC.LOGGER.debug("Registered forge MobCategory as SpawnCategory(Bukkit) {}", spawnCategory);
            }
        }
    }

    private static void addPose() {
        for (Pose pose : Pose.values()) {
            if (pose.ordinal() > 7) {
                org.bukkit.entity.Pose bukkit = MohistDynamEnum.addEnum(org.bukkit.entity.Pose.class, pose.name());
                MohistMC.LOGGER.debug("Registered forge Pose as Pose(Bukkit) {}", bukkit);
            }
        }
    }

    public static void addEnumArt() {
        int i = Art.values().length;
        for (var entry : ForgeRegistries.PAINTING_TYPES) {
            int width = entry.getWidth();
            int height = entry.getHeight();
            ResourceLocation resourceLocation = ForgeRegistries.PAINTING_TYPES.getKey(entry);
            if (!resourceLocation.getNamespace().equals(NamespacedKey.MINECRAFT)) {
                String name = normalizeName(resourceLocation.toString());
                String lookupName = resourceLocation.getPath().toLowerCase(Locale.ROOT);
                int id = i - 1;
                Art art = MohistDynamEnum.addEnum(Art.class, name, List.of(Integer.TYPE, Integer.TYPE, Integer.TYPE), List.of(id, width, height));
                Art.BY_NAME.put(lookupName, art);
                Art.BY_ID.put(id, art);
                MohistMC.LOGGER.debug("Registered forge PaintingType as Art {}", art);
                i++;
            }
        }
    }

    public static String normalizeName(String name) {
        return name.toUpperCase(java.util.Locale.ENGLISH).replaceAll("(:|\\s)", "_").replaceAll("\\W", "");
    }

    public static boolean isMods(ResourceLocation resourceLocation) {
        return !resourceLocation.getNamespace().equals(NamespacedKey.MINECRAFT);
    }
}
