package com.mohistmc.mohist.forge;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import com.mohistmc.dynamicenum.MohistDynamEnum;
import com.mohistmc.mohist.Mohist;
import com.mohistmc.mohist.api.ServerAPI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatType;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraftforge.registries.ForgeRegistries;
import org.bukkit.Art;
import org.bukkit.Fluid;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.block.CraftBlockStates;
import org.bukkit.craftbukkit.block.CraftHangingSign;
import org.bukkit.craftbukkit.block.CraftSign;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.craftbukkit.util.CraftSpawnCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.entity.Villager;

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
    public static Map<NamespacedKey, EntityType> entityTypeMap = new HashMap<>();
    public static Map<NamespacedKey, Particle> particleMap = new HashMap<>();


    public static void init() {
        addEnumMaterialInItems();
        addEnumMaterialsInBlocks();
        addEnumBiome();
        addFluid();
        addEnumEntity();
        addEnumVillagerProfession();
        addEnumArt();
        addEnumParticle();
        addStatistic();
        addEnumEnvironment();
        loadSpawnCategory();
        addPose();
    }


    public static void addEnumMaterialInItems() {
        var registry = ForgeRegistries.ITEMS;
        for (Item item : registry) {
            ResourceLocation resourceLocation = registry.getKey(item);
            if (isMods(resourceLocation)) {
                // inject item materials into Bukkit for FML
                int id = Item.getId(item);
                Material material = Material.addMaterial(resourceLocation.toString(), id, item.getDefaultMaxStackSize(), false, true, resourceLocation);

                CraftMagicNumbers.ITEM_MATERIAL.put(item, material);
                CraftMagicNumbers.MATERIAL_ITEM.put(material, item);
                Mohist.LOGGER.debug("Save-ITEM: {} - {}", material.name(), material.key);
            }
        }
    }

    public static void addEnumMaterialsInBlocks() {
        var registry = ForgeRegistries.BLOCKS;
        for (Block block : registry) {
            ResourceLocation resourceLocation = registry.getKey(block);
            if (isMods(resourceLocation)) {
                // inject block materials into Bukkit for FML
                String materialName = resourceLocation.toString();
                int id = Item.getId(block.asItem());
                Item item = Item.byId(id);
                Material material = Material.addMaterial(materialName, id, item.getDefaultMaxStackSize(), true, false, resourceLocation);

                if (material != null) {
                    CraftMagicNumbers.BLOCK_MATERIAL.put(block, material);
                    CraftMagicNumbers.MATERIAL_BLOCK.put(material, block);
                    if (block.defaultBlockState().is(BlockTags.SIGNS)) {
                        CraftBlockStates.register(material, CraftSign.class, CraftSign::new, SignBlockEntity::new);
                    } else if (block.defaultBlockState().is(BlockTags.ALL_HANGING_SIGNS)) {
                        CraftBlockStates.register(material, CraftHangingSign.class, CraftHangingSign::new, HangingSignBlockEntity::new);
                    } else if (block instanceof SignBlock signBlock) {
                        BlockEntity blockEntity = signBlock.newBlockEntity(BlockPos.ZERO, block.defaultBlockState());
                        if (blockEntity instanceof HangingSignBlockEntity) {
                            CraftBlockStates.register(material, CraftHangingSign.class, CraftHangingSign::new, HangingSignBlockEntity::new);
                        } else if (blockEntity instanceof SignBlockEntity) {
                            CraftBlockStates.register(material, CraftSign.class, CraftSign::new, SignBlockEntity::new);
                        }
                    }
                    Mohist.LOGGER.debug("Save-BLOCK:{} - {}", material.name(), material.key);
                }
            }
        }
    }

    public static void addEnumParticle() {
        var registry = ForgeRegistries.PARTICLE_TYPES;
        for (ParticleType<?> particleType : ForgeRegistries.PARTICLE_TYPES) {
            ResourceLocation resourceLocation = registry.getKey(particleType);
            if (isMods(resourceLocation)) {
                String name = resourceLocation.toString();
                NamespacedKey namespacedKey = CraftNamespacedKey.fromMinecraft(resourceLocation);
                Particle particle = MohistDynamEnum.addEnum(Particle.class, name, List.of(String.class), List.of(namespacedKey.toString()));
                if (particle != null) {
                    particle.key = namespacedKey;
                    particleMap.put(namespacedKey, particle);
                    Mohist.LOGGER.debug("Save-ParticleType:{} - {}", name, particle.name());
                }
            }
        }
    }

    public static void addEnumBiome() {
        List<String> map = new ArrayList<>();
        var registry = ForgeRegistries.BIOMES;
        for (net.minecraft.world.level.biome.Biome biome : registry) {
            ResourceLocation resourceLocation = registry.getKey(biome);
            if (isMods(resourceLocation)) {
                String biomeName = resourceLocation.toString();
                if (!map.contains(biomeName)) {
                    map.add(biomeName);
                    org.bukkit.block.Biome biomeCB = MohistDynamEnum.addEnum(org.bukkit.block.Biome.class, biomeName);
                    biomeBiomeMap.put(biome, biomeCB);
                    Mohist.LOGGER.debug("Save-BIOME:{} - {}", biomeCB.name(), biomeName);
                }
            }
        }
        map.clear();
    }


    public static void addEnumEnvironment() {
        int i = World.Environment.values().length;
        var registry = ServerAPI.getNMSServer().registryAccess().registryOrThrow(Registries.LEVEL_STEM);
        for (Entry<ResourceKey<LevelStem>, LevelStem> entry : registry.entrySet()) {
            ResourceKey<LevelStem> key = entry.getKey();
            World.Environment environment1 = environment.get(key);
            if (environment1 == null) {
                int id = i - 1;
                environment1 = MohistDynamEnum.addEnum(World.Environment.class, key.location().toString(), List.of(Integer.TYPE), List.of(id));
                environment.put(key, environment1);
                environment0.put(environment1, key);
                Mohist.LOGGER.debug("Registered forge DimensionType as environment {}", environment1);
                i++;
            }
        }
    }

    public static void addEnumEntity() {
        var registry = ForgeRegistries.ENTITY_TYPES;
        for (net.minecraft.world.entity.EntityType<?> entity : registry) {
            ResourceLocation resourceLocation = registry.getKey(entity);
            if (isMods(resourceLocation)) {
                NamespacedKey key = CraftNamespacedKey.fromMinecraft(resourceLocation);
                String entityType = resourceLocation.toString();
                int typeId = entityType.hashCode();
                EntityType bukkitType = MohistDynamEnum.addEnum(EntityType.class, entityType, List.of(String.class, Class.class, Integer.TYPE), List.of(resourceLocation.getPath(), Entity.class, typeId));
                Mohist.LOGGER.debug("Registered {} as entity {}", resourceLocation, bukkitType);
                if (bukkitType != null) {
                    bukkitType.key = key;
                    entityTypeMap.put(key, bukkitType);
                    EntityType.NAME_MAP.put(entityType.toLowerCase(), bukkitType);
                    EntityType.ID_MAP.put((short) typeId, bukkitType);
                }
            }
        }
    }

    public static void addEnumVillagerProfession() {
        var registry = ForgeRegistries.VILLAGER_PROFESSIONS;
        for (VillagerProfession villagerProfession : registry) {
            ResourceLocation resourceLocation = registry.getKey(villagerProfession);
            if (isMods(resourceLocation)) {
                Villager.Profession vp = MohistDynamEnum.addEnum(Villager.Profession.class, resourceLocation.toString());
                profession.put(vp, resourceLocation);
                Mohist.LOGGER.debug("Registered forge VillagerProfession as Profession {}", vp.name());
            }
        }
    }

    public static void addEnumAttribute() {
        var registry = ForgeRegistries.ATTRIBUTES;
        for (Attribute attribute : registry) {
            ResourceLocation resourceLocation = registry.getKey(attribute);
            if (isMods(resourceLocation)) {
                org.bukkit.attribute.Attribute ab = MohistDynamEnum.addEnum(org.bukkit.attribute.Attribute.class, resourceLocation.toString(), List.of(String.class), List.of());
                attributemap.put(ab, resourceLocation);
                Mohist.LOGGER.debug("Registered forge Attribute as Attribute(Bukkit) {}", ab.name());
            }
        }
    }

    public static void addFluid() {
        var registry = ForgeRegistries.FLUIDS;
        for (net.minecraft.world.level.material.Fluid fluidType : registry) {
            ResourceLocation resourceLocation = registry.getKey(fluidType);
            if (isMods(resourceLocation)) {
                Fluid fluid = MohistDynamEnum.addEnum(Fluid.class, resourceLocation.toString());
                Mohist.LOGGER.debug("Registered forge Fluid as Fluid(Bukkit) {}", fluid.name());
            }
        }
    }

    public static void addStatistic() {
        var registry = ForgeRegistries.STAT_TYPES;
        for (StatType<?> statType : registry) {
            ResourceLocation resourceLocation = registry.getKey(statType);
            if (isMods(resourceLocation)) {
                Statistic statistic = MohistDynamEnum.addEnum(Statistic.class, resourceLocation.toString());
                statisticMap.put(statType, statistic);
                Mohist.LOGGER.debug("Registered forge StatType as Statistic(Bukkit) {}", statistic.name());
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
                Mohist.LOGGER.debug("Registered forge MobCategory as SpawnCategory(Bukkit) {}", spawnCategory);
            }
        }
    }

    private static void addPose() {
        for (Pose pose : Pose.values()) {
            if (pose.ordinal() > 14) {
                org.bukkit.entity.Pose bukkit = MohistDynamEnum.addEnum(org.bukkit.entity.Pose.class, pose.name());
                Mohist.LOGGER.debug("Registered forge Pose as Pose(Bukkit) {}", bukkit);
            }
        }
    }

    public static void addEnumArt() {
        int i = Art.values().length;
        for (var entry : ForgeRegistries.PAINTING_VARIANTS) {
            int width = entry.getWidth();
            int height = entry.getHeight();
            ResourceLocation resourceLocation = ForgeRegistries.PAINTING_VARIANTS.getKey(entry);
            if (isMods(resourceLocation)) {
                int id = i - 1;
                Art art = MohistDynamEnum.addEnum(Art.class, resourceLocation.toString(), List.of(Integer.TYPE, Integer.TYPE, Integer.TYPE), List.of(id, width, height));
                Art.BY_NAME.put(art.name(), art);
                Art.BY_ID.put(id, art);
                Mohist.LOGGER.debug("Registered forge PaintingType as Art {}", art);
                i++;
            }
        }
    }

    public static boolean isMods(ResourceLocation resourceLocation) {
        return resourceLocation != null && !resourceLocation.getNamespace().equals(NamespacedKey.MINECRAFT);
    }

    public static boolean isMods(NamespacedKey namespacedkey) {
        return !namespacedkey.getNamespace().equals(NamespacedKey.MINECRAFT);
    }
}
