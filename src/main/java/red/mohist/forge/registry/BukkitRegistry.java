package red.mohist.forge.registry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_15_R1.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftNamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Villager;
import red.mohist.api.EnumHelper;
import red.mohist.api.Unsafe;
import red.mohist.forge.MohistMod;
import red.mohist.forge.util.ResourceLocationUtil;
import red.mohist.util.i18n.Message;

import java.lang.reflect.Field;
import java.util.*;

@SuppressWarnings({"unchecked", "ConstantConditions"})
public class BukkitRegistry {

    private static final List<Class<?>> MAT_CTOR = ImmutableList.of(int.class);
    private static final List<Class<?>> ENTITY_CTOR = ImmutableList.of(String.class, Class.class, int.class);
    private static final List<Class<?>> ENV_CTOR = ImmutableList.of(int.class);
    private static final Map<String, Material> BY_NAME = getStatic(Material.class, "BY_NAME");
    private static final Map<Block, Material> BLOCK_MATERIAL = getStatic(CraftMagicNumbers.class, "BLOCK_MATERIAL");
    private static final Map<Item, Material> ITEM_MATERIAL = getStatic(CraftMagicNumbers.class, "ITEM_MATERIAL");
    private static final Map<Material, Item> MATERIAL_ITEM = getStatic(CraftMagicNumbers.class, "MATERIAL_ITEM");
    private static final Map<Material, Block> MATERIAL_BLOCK = getStatic(CraftMagicNumbers.class, "MATERIAL_BLOCK");
    private static final Map<String, EntityType> ENTITY_NAME_MAP = getStatic(EntityType.class, "NAME_MAP");
    private static final Map<Integer, World.Environment> ENVIRONMENT_MAP = getStatic(World.Environment.class, "lookup");

    public static void registerAll() {
        registerEnvironments();
    }
    
    public static void registerEnvironments() {
        int i = World.Environment.values().length;
        List<World.Environment> newTypes = new ArrayList<>();
        for (DimensionType dimensionType : DimensionManager.getRegistry()) {
            DimensionType actual = ((DimensionType) dimensionType).getType();
            World.Environment environment = World.Environment.getEnvironment(actual.getId());
            if (environment == null) {
                String name = ResourceLocationUtil.standardize(actual.getRegistryName());
                environment = EnumHelper.makeEnum(World.Environment.class, name, i++, ENV_CTOR, ImmutableList.of(actual.getId()));
                newTypes.add(environment);
                ENVIRONMENT_MAP.put(actual.getId(), environment);
                MohistMod.LOGGER.debug("Registered {} as environment {}", actual.getRegistryName(), environment);
            }
        }
        EnumHelper.addEnums(World.Environment.class, newTypes);
        MohistMod.LOGGER.info("registry.environment", newTypes.size());
    }

    private static <T> T getStatic(Class<?> cl, String name) {
        try {
            Unsafe.ensureClassInitialized(cl);
            Field field = cl.getDeclaredField(name);
            Object materialByNameBase = Unsafe.staticFieldBase(field);
            long materialByNameOffset = Unsafe.staticFieldOffset(field);
            return (T) Unsafe.getObject(materialByNameBase, materialByNameOffset);
        } catch (Exception e) {
            return null;
        }
    }

    private static void putStatic(Class<?> cl, String name, Object o) {
        try {
            Unsafe.ensureClassInitialized(cl);
            Field field = cl.getDeclaredField(name);
            Object materialByNameBase = Unsafe.staticFieldBase(field);
            long materialByNameOffset = Unsafe.staticFieldOffset(field);
            Unsafe.putObject(materialByNameBase, materialByNameOffset, o);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void putBool(Class<?> cl, String name, boolean b) {
        try {
            Unsafe.ensureClassInitialized(cl);
            Field field = cl.getDeclaredField(name);
            Object materialByNameBase = Unsafe.staticFieldBase(field);
            long materialByNameOffset = Unsafe.staticFieldOffset(field);
            Unsafe.putBoolean(materialByNameBase, materialByNameOffset, b);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Set<IForgeRegistry<?>> registries() {
        return ImmutableSet.of(ForgeRegistries.BLOCKS, ForgeRegistries.ITEMS,
                ForgeRegistries.POTION_TYPES, ForgeRegistries.POTIONS,
                ForgeRegistries.ENTITIES, ForgeRegistries.TILE_ENTITIES,
                ForgeRegistries.BIOMES);
    }

    public static void unlockRegistries() {
        for (IForgeRegistry<?> registry : registries()) {
            if (registry instanceof ForgeRegistry) {
                ((ForgeRegistry<?>) registry).unfreeze();
            }
        }
    }

    public static void lockRegistries() {
        for (IForgeRegistry<?> registry : registries()) {
            if (registry instanceof ForgeRegistry) {
                ((ForgeRegistry<?>) registry).freeze();
            }
        }
    }
}

