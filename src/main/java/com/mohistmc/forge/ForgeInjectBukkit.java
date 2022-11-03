package com.mohistmc.forge;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import com.mohistmc.MohistMC;
import com.mohistmc.api.ServerAPI;
import com.mohistmc.dynamicenumutil.MohistEnumHelper;
import com.mohistmc.entity.MohistModsEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.v1_19_R1.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.v1_19_R1.potion.CraftPotionEffectType;
import org.bukkit.craftbukkit.v1_19_R1.util.CraftMagicNumbers;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.potion.PotionEffectType;

public class ForgeInjectBukkit {

    public static BiMap<ResourceKey<LevelStem>, World.Environment> environment =
            HashBiMap.create(ImmutableMap.<ResourceKey<LevelStem>, World.Environment>builder()
                    .put(LevelStem.OVERWORLD, World.Environment.NORMAL)
                    .put(LevelStem.NETHER, World.Environment.NETHER)
                    .put(LevelStem.END, World.Environment.THE_END)
                    .build());

    public static Map<Villager.Profession, ResourceLocation> profession = new HashMap<>();
    public static Map<org.bukkit.attribute.Attribute, ResourceLocation> attributemap = new HashMap<>();


    public static void init(){
        addEnumMaterialInItems();
        addEnumMaterialsInBlocks();
        addEnumBiome();
        addEnumEnchantment();
        addEnumPotion();
        //addEnumPattern();
        addEnumEntity();
        addEnumVillagerProfession();
        //addEnumArt();
    }


    public static void addEnumMaterialInItems(){
        for (Item item : ForgeRegistries.ITEMS) {
            ResourceLocation resourceLocation = ForgeRegistries.ITEMS.getKey(item);
            if(!resourceLocation.getNamespace().equals(NamespacedKey.MINECRAFT)) {
                // inject item materials into Bukkit for FML
                String materialName = normalizeName(resourceLocation.toString());
                int id = Item.getId(item);
                Material material = Material.addMaterial(materialName, id, false, resourceLocation.getNamespace());
                CraftMagicNumbers.ITEM_MATERIAL.put(item, material);
                CraftMagicNumbers.MATERIAL_ITEM.put(material, item);
                if (material != null) {
                    MohistMC.LOGGER.debug("Save-ITEM: " + material.name() + " - " + materialName);
                }
            }
        }
    }


    public static void addEnumMaterialsInBlocks(){
        for (Map.Entry<ResourceKey<Block>, Block> entry : ForgeRegistries.BLOCKS.getEntries()) {
            ResourceLocation resourceLocation = entry.getKey().registry();
            if(!resourceLocation.getNamespace().equals(NamespacedKey.MINECRAFT)) {
                // inject block materials into Bukkit for FML
                Block block = entry.getValue();
                String materialName = normalizeName(entry.getKey().toString());
                int id = Item.getId(block.asItem());
                Material material = Material.addMaterial(materialName, id, true, resourceLocation.getNamespace());
                CraftMagicNumbers.BLOCK_MATERIAL.put(block, material);
                CraftMagicNumbers.MATERIAL_BLOCK.put(material, block);
                if (material != null) {
                    MohistMC.LOGGER.debug("Save-BLOCK:" + material.name() + " - " + materialName);
                }
            }
        }
    }


    public static void addEnumEnchantment() {
        // Enchantment
        for (Map.Entry<ResourceKey<Enchantment>, Enchantment> entry : ForgeRegistries.ENCHANTMENTS.getEntries()) {
            org.bukkit.enchantments.Enchantment.registerEnchantment(new CraftEnchantment(entry.getValue()));
        }
        org.bukkit.enchantments.Enchantment.stopAcceptingRegistrations();
    }

    public static void addEnumPotion() {
        // Points
        for (Map.Entry<ResourceKey<MobEffect>, MobEffect> entry : ForgeRegistries.MOB_EFFECTS.getEntries()) {
            PotionEffectType pet = new CraftPotionEffectType(entry.getValue());
            PotionEffectType.registerPotionEffectType(pet);
        }
        PotionEffectType.stopAcceptingRegistrations();
    }

    public static void addEnumBiome() {
        List<String> map = new ArrayList<>();
        for (Map.Entry<ResourceKey<Biome>, Biome> entry : ForgeRegistries.BIOMES.getEntries()) {
            String biomeName = entry.getKey().registry().getNamespace();
            if (!biomeName.equals(NamespacedKey.MINECRAFT) && !map.contains(biomeName)) {
                map.add(biomeName);
                org.bukkit.block.Biome biome = MohistEnumHelper.addEnum0(org.bukkit.block.Biome.class, biomeName, new Class[0]);
                MohistMC.LOGGER.debug("Save-BIOME:" + biome.name() + " - " + biomeName);
            }
        }
        map.clear();
    }


    public static void addEnumEnvironment(net.minecraft.core.Registry<LevelStem> registry) {
        int i = World.Environment.values().length;
        for (Map.Entry<ResourceKey<LevelStem>, LevelStem> entry : registry.entrySet()) {
            ResourceKey<LevelStem> key = entry.getKey();
            World.Environment environment1 = environment.get(key);
            if (environment1 == null) {
                String name = normalizeName(key.location().toString());
                int id = i - 1;
                environment1 = MohistEnumHelper.addEnum(World.Environment.class, name, new Class[]{Integer.TYPE}, new Object[]{id});
                environment.put(key, environment1);
                MohistMC.LOGGER.debug("Registered forge DimensionType as environment {}", environment1);
                i++;
            }
        }
    }

    public static WorldType addEnumWorldType(String name)
    {
        WorldType worldType = MohistEnumHelper.addEnum0(WorldType.class, name, new Class [] { String.class }, name);
        Map<String, WorldType> BY_NAME = ObfuscationReflectionHelper.getPrivateValue(WorldType.class, null, "BY_NAME");
        BY_NAME.put(name.toUpperCase(), worldType);
        return worldType;
    }

    public static void addEnumEntity() {
        for (Map.Entry<ResourceKey<net.minecraft.world.entity.EntityType<?>>, net.minecraft.world.entity.EntityType<?>> entity : ForgeRegistries.ENTITY_TYPES.getEntries()) {
            ResourceLocation resourceLocation = entity.getKey().registry();
            if (!resourceLocation.getNamespace().equals(NamespacedKey.MINECRAFT)) {
                String entityType = normalizeName(resourceLocation.toString());
                int typeId = entityType.hashCode();
                EntityType bukkitType = MohistEnumHelper.addEnum0(EntityType.class, entityType, new Class[]{String.class, Class.class, Integer.TYPE, Boolean.TYPE}, entityType.toLowerCase(), MohistModsEntity.class, typeId, false);
                EntityType.NAME_MAP.put(entityType.toLowerCase(), bukkitType);
                EntityType.ID_MAP.put((short) typeId, bukkitType);
                ServerAPI.entityTypeMap.put(entity.getValue(), entityType);
            }
        }
    }

    public static void addEnumVillagerProfession() {
        for (Map.Entry<ResourceKey<VillagerProfession>,VillagerProfession> villagerProfession : ForgeRegistries.VILLAGER_PROFESSIONS.getEntries()) {
            ResourceLocation resourceLocation = villagerProfession.getKey().registry();
            if(!resourceLocation.getNamespace().equals(NamespacedKey.MINECRAFT)) {
                String name = normalizeName(resourceLocation.toString());
                Villager.Profession vp = MohistEnumHelper.addEnum0(Villager.Profession.class, name, new Class[0]);
                profession.put(vp, resourceLocation);
                MohistMC.LOGGER.debug("Registered forge VillagerProfession as Profession {}", vp.name());
            }
        }
    }

    public static void addEnumAttribute() {
        for (Map.Entry<ResourceKey<Attribute>,Attribute> attribute : ForgeRegistries.ATTRIBUTES.getEntries()) {
            ResourceLocation resourceLocation = attribute.getKey().registry();
            String name = normalizeName(resourceLocation.getPath());
            if(!resourceLocation.getNamespace().equals(NamespacedKey.MINECRAFT)) {
                org.bukkit.attribute.Attribute ab = MohistEnumHelper.addEnum0(org.bukkit.attribute.Attribute.class, name, new Class[]{String.class}, resourceLocation.getPath());
                attributemap.put(ab, resourceLocation);
                MohistMC.LOGGER.debug("Registered forge Attribute as Attribute(Bukkit) {}", ab.name());
            }
        }
    }

    public static String normalizeName(String name) {
        return name.toUpperCase(java.util.Locale.ENGLISH).replaceAll("(:|\\s)", "_").replaceAll("\\W", "");
    }
}
