package red.mohist.forge;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.block.Biome;
import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.v1_12_R1.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.v1_12_R1.potion.CraftPotionEffectType;
import org.bukkit.entity.EntityType;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.permissions.DefaultPermissions;
import red.mohist.Mohist;
import red.mohist.api.ItemAPI;
import red.mohist.api.ServerAPI;
import red.mohist.entity.CraftCustomEntity;
import red.mohist.util.i18n.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ForgeInjectBukkit {

    public static void init() {
        addEnumMaterialInItems();
        addEnumMaterialsInBlocks();
        addEnumEnchantment();
        addEnumPotion();
        addEnumBiome();
        addEnumPattern();
        addEnumEntity();
    }

    public static void addEnumMaterialInItems() {
        for (Map.Entry<ResourceLocation, Item> entry : ForgeRegistries.ITEMS.getEntries()) {
            if (!entry.getKey().getResourceDomain().equals("minecraft")) {
                String materialName = Material.normalizeName(entry.getKey().toString());
                // inject item materials into Bukkit for FML
                Item item = entry.getValue();
                Material material = Material.addMaterial(Objects.requireNonNull(EnumHelper.addEnum(Material.class, materialName, new Class[]{Integer.TYPE, Integer.TYPE}, Item.getIdFromItem(item), item.getItemStackLimit())));
                if (material != null) {
                    ServerAPI.injectmaterials.put(material.name(), material.getId());
                    Mohist.LOGGER.debug("Save: " + Message.getFormatString("injected.item", new Object[]{material.name(), String.valueOf(material.getId()), String.valueOf(ItemAPI.getBukkit(material).getDurability())}));
                }
            }
        }
    }

    public static void addEnumMaterialsInBlocks() {
        for (Map.Entry<ResourceLocation, Block> entry : ForgeRegistries.BLOCKS.getEntries()) {
            if (!entry.getKey().getResourceDomain().equals("minecraft")) {
                String materialName = Material.normalizeName(entry.getKey().toString());
                // inject block materials into Bukkit for FML
                Block block = entry.getValue();
                Material material = Material.addBlockMaterial(Objects.requireNonNull(EnumHelper.addEnum(Material.class, materialName, new Class[]{Integer.TYPE}, Block.getIdFromBlock(block))));
                if (material != null) {
                    ServerAPI.injectblock.put(material.name(), material.getId());
                    Mohist.LOGGER.debug("Save: " + Message.getFormatString("injected.block", new Object[]{material.name(), String.valueOf(material.getId())}));
                }
            }
        }

        for (Material material : Material.values()) {
            if (material.getId() < 256) {
                Material.addBlockMaterial(material);
            }
        }
    }

    public static void addEnumEnchantment() {
        // Enchantment
        for (Map.Entry<ResourceLocation, Enchantment> entry : ForgeRegistries.ENCHANTMENTS.getEntries()) {
            org.bukkit.enchantments.Enchantment.registerEnchantment(new CraftEnchantment(entry.getValue()));
        }
        org.bukkit.enchantments.Enchantment.stopAcceptingRegistrations();
    }

    public static void addEnumPotion() {
        // Points
        for (Map.Entry<ResourceLocation, Potion> entry : ForgeRegistries.POTIONS.getEntries()) {
            PotionEffectType pet = new CraftPotionEffectType(entry.getValue());
            PotionEffectType.registerPotionEffectType(pet);
        }
        PotionEffectType.stopAcceptingRegistrations();
    }

    public static void addEnumBiome() {
        List<String> map = new ArrayList<>();
        for (Map.Entry<ResourceLocation, net.minecraft.world.biome.Biome> entry : ForgeRegistries.BIOMES.getEntries()) {
            String biomeName = entry.getKey().getResourcePath().toUpperCase(java.util.Locale.ENGLISH);
            if (!entry.getKey().getResourceDomain().equals("minecraft") && !map.contains(biomeName)) {
                map.add(biomeName);
                EnumHelper.addEnum(Biome.class, biomeName, new Class[0]);
            }
        }
        map.clear();
    }

    public static void addEnumPattern() {
        Map<String, PatternType> PATTERN_MAP = ObfuscationReflectionHelper.getPrivateValue(PatternType.class, null, "byString");
        for (BannerPattern bannerpattern : BannerPattern.values()) {
            String p_i47246_3_ = bannerpattern.name();
            String hashname = bannerpattern.getHashname();
            if (PatternType.getByIdentifier(hashname) == null) {
                PatternType patternType = EnumHelper.addEnum(PatternType.class, p_i47246_3_, new Class[]{String.class}, hashname);
                PATTERN_MAP.put(hashname, patternType);
            }
        }
    }

    public static World.Environment addEnumEnvironment(int id, String name) {
        return EnumHelper.addEnum(World.Environment.class, name, new Class[]{Integer.TYPE}, new Object[]{id});
    }

    public static WorldType addEnumWorldType(String name) {
        WorldType worldType = EnumHelper.addEnum(WorldType.class, name, new Class[]{String.class}, name);
        Map<String, WorldType> BY_NAME = ObfuscationReflectionHelper.getPrivateValue(WorldType.class, null, "BY_NAME");
        BY_NAME.put(name.toUpperCase(), worldType);
        return worldType;
    }

    public static void addEnumEntity() {
        Map<String, EntityType> NAME_MAP = ObfuscationReflectionHelper.getPrivateValue(EntityType.class, null, "NAME_MAP");
        Map<Short, EntityType> ID_MAP = ObfuscationReflectionHelper.getPrivateValue(EntityType.class, null, "ID_MAP");

        for (Map.Entry<String, Class<? extends Entity>> entity : EntityRegistry.entityClassMap.entrySet()) {
            String name = entity.getKey();
            String entityType = name.replace("-", "_").toUpperCase();
            int typeId = GameData.getEntityRegistry().getID(EntityRegistry.getEntry(entity.getValue()));
            EntityType bukkitType = EnumHelper.addEnum(EntityType.class, entityType, new Class[]{String.class, Class.class, Integer.TYPE, Boolean.TYPE}, name, CraftCustomEntity.class, typeId, false);

            NAME_MAP.put(name.toLowerCase(), bukkitType);
            ID_MAP.put((short) typeId, bukkitType);
        }
    }

    public static void registerDefaultPermission(String name, DefaultPermissionLevel level, String desc) {
        PermissionDefault permissionDefault;
        switch (level) {
            case ALL:
                permissionDefault = PermissionDefault.TRUE;
                break;
            case OP:
                permissionDefault = PermissionDefault.OP;
                break;
            case NONE:
            default:
                permissionDefault = PermissionDefault.FALSE;
                break;
        }
        Permission permission = new Permission(name, desc, permissionDefault);
        DefaultPermissions.registerPermission(permission, false);
    }
}
