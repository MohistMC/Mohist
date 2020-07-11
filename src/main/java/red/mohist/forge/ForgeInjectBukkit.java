package red.mohist.forge;

import java.util.Map;
import java.util.Objects;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_15_R1.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftCustomEntity;
import org.bukkit.craftbukkit.v1_15_R1.potion.CraftPotionEffectType;
import org.bukkit.entity.EntityType;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.permissions.DefaultPermissions;
import red.mohist.Mohist;
import red.mohist.api.ItemAPI;
import red.mohist.api.ServerAPI;
import red.mohist.util.EnumHelper;
import red.mohist.util.i18n.Message;

public class ForgeInjectBukkit {

    public static void init(){
        addEnumMaterialInItems();
        addEnumMaterialsInBlocks();
        addEnumEnchantment();
        addEnumPotion();
        addEnumEntity();
    }

    public static void addEnumMaterialInItems(){
        for (Map.Entry<ResourceLocation, Item> entry : ForgeRegistries.ITEMS.getEntries()) {
            ResourceLocation key = entry.getKey();
            Item item = entry.getValue();
            if(!key.getNamespace().equals("minecraft")) {
                String materialName = Material.normalizeName(entry.getKey().toString());
                // inject item materials into Bukkit for FML
                Material material = Material.addMaterial(EnumHelper.addEnum(Material.class, materialName, new Class[]{Integer.TYPE, Integer.TYPE}, new Object[]{Item.getIdFromItem(item), item.getMaxStackSize()}));
                if (material != null) {
                    ServerAPI.injectmaterials.put(material.name(), material.getId());
                    Mohist.LOGGER.debug("Save: " + Message.getFormatString("injected.item", new Object[]{material.name(), String.valueOf(material.getId()), String.valueOf(ItemAPI.getBukkit(material).getDurability())}));
                }
            }
        }
    }

    public static void addEnumMaterialsInBlocks(){
        for (Map.Entry<ResourceLocation, Block> entry : ForgeRegistries.BLOCKS.getEntries()) {
            ResourceLocation key = entry.getKey();
            Block block = entry.getValue();
            if(!key.getNamespace().equals("minecraft")) {
                String materialName = Material.normalizeName(entry.getKey().toString());
                // inject block materials into Bukkit for FML
                Material material = Material.addMaterial(Objects.requireNonNull(EnumHelper.addEnum(Material.class, materialName, new Class[]{Integer.TYPE}, new Object[]{Item.getIdFromItem(block.asItem())})));
                if (material != null) {
                    ServerAPI.injectblock.put(material.name(), material.getId());
                    Mohist.LOGGER.debug("Save: " + Message.getFormatString("injected.block", new Object[]{material.name(), String.valueOf(material.getId())}));
                }
            }
        }

        for (Material material : Material.values()) {
            if (material.getId() < 256) {
                Material.addMaterial(material);
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
        for (Map.Entry<ResourceLocation, Effect> entry : ForgeRegistries.POTIONS.getEntries()) {
            PotionEffectType pet = new CraftPotionEffectType(entry.getValue());
            PotionEffectType.registerPotionEffectType(pet);
        }
        PotionEffectType.stopAcceptingRegistrations();
    }

    public static World.Environment addEnumEnvironment(int id, String name) {
        return (World.Environment)EnumHelper.addEnum(World.Environment.class, name, new Class[]{Integer.TYPE}, new Object[]{id});
    }

    public static void addEnumEntity() {
        Map<String, EntityType> NAME_MAP =  ObfuscationReflectionHelper.getPrivateValue(EntityType.class, null, "NAME_MAP");
        Map<Short, EntityType> ID_MAP =  ObfuscationReflectionHelper.getPrivateValue(EntityType.class, null, "ID_MAP");

        for (Map.Entry<ResourceLocation, net.minecraft.entity.EntityType<?>> entity : ForgeRegistries.ENTITIES.getEntries()) {
            String entityname = entity.getKey().getNamespace();
            String entityType = entityname.replace("-", "_").toUpperCase();

            EntityType bukkitType = EnumHelper.addEnum(EntityType.class, entityType, new Class[]{String.class, Class.class, Integer.TYPE, Boolean.TYPE}, new Object[]{entityname, CraftCustomEntity.class, entity.getKey().getNamespace().hashCode(), false});

            NAME_MAP.put(entityname.toLowerCase(), bukkitType);
            ID_MAP.put((short) entity.getKey().getNamespace().hashCode(), bukkitType);
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
