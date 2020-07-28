package red.mohist.forge.registry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftNamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import red.mohist.api.EnumHelper;
import red.mohist.api.Unsafe;
import red.mohist.forge.MohistMod;
import red.mohist.forge.util.ResourceLocationUtil;

import java.lang.reflect.Field;
import java.util.*;

@SuppressWarnings({"unchecked", "ConstantConditions"})
public class BukkitRegistry {


    private static final List<Class<?>> MAT_CTOR = ImmutableList.of(int.class);
    private static final List<Class<?>> ENTITY_CTOR = ImmutableList.of(String.class, Class.class, int.class);
    private static final List<Class<?>> ENV_CTOR = ImmutableList.of(int.class);
    private static final Map<String, EntityType> ENTITY_NAME_MAP = getStatic(EntityType.class, "NAME_MAP");
    private static final Map<Integer, World.Environment> ENVIRONMENT_MAP = getStatic(World.Environment.class, "lookup");

    public static void registerAll() {
        loadBiomes();
        loadEntities();
        registerEnvironments();
        loadVillagerProfessions();
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

    private static void loadBiomes() {
        int i = Biome.values().length;
        List<Biome> newTypes = new ArrayList<>();
        Field key = Arrays.stream(Biome.class.getDeclaredFields()).filter(it -> it.getName().equals("key")).findAny().orElse(null);
        long keyOffset = Unsafe.objectFieldOffset(key);
        for (net.minecraft.world.biome.Biome biome : ForgeRegistries.BIOMES) {
            String name = ResourceLocationUtil.standardize(biome.getRegistryName());
            Biome bukkit;
            try {
                bukkit = Biome.valueOf(name);
            } catch (Throwable t) {
                bukkit = null;
            }
            if (bukkit == null) {
                bukkit = EnumHelper.makeEnum(Biome.class, name, i++, ImmutableList.of(), ImmutableList.of());
                newTypes.add(bukkit);
                Unsafe.putObject(bukkit, keyOffset, CraftNamespacedKey.fromMinecraft(biome.getRegistryName()));
                MohistMod.LOGGER.debug("Registered {} as biome {}", biome.getRegistryName(), bukkit);
            }
        }
        EnumHelper.addEnums(Biome.class, newTypes);
        MohistMod.LOGGER.info("registry.biome", newTypes.size());
    }

    private static void loadVillagerProfessions() {
        int i = Villager.Profession.values().length;
        List<Villager.Profession> newTypes = new ArrayList<>();
        Field key = Arrays.stream(Villager.Profession.class.getDeclaredFields()).filter(it -> it.getName().equals("key")).findAny().orElse(null);
        long keyOffset = Unsafe.objectFieldOffset(key);
        for (VillagerProfession villagerProfession : ForgeRegistries.PROFESSIONS) {
            String name = ResourceLocationUtil.standardize(villagerProfession.getRegistryName());
            Villager.Profession profession;
            try {
                profession = Villager.Profession.valueOf(name);
            } catch (Throwable t) {
                profession = null;
            }
            if (profession == null) {
                profession = EnumHelper.makeEnum(Villager.Profession.class, name, i++, ImmutableList.of(), ImmutableList.of());
                newTypes.add(profession);
                Unsafe.putObject(profession, keyOffset, CraftNamespacedKey.fromMinecraft(villagerProfession.getRegistryName()));
                MohistMod.LOGGER.debug("Registered {} as villager profession {}", villagerProfession.getRegistryName(), profession);
            }
        }
        EnumHelper.addEnums(Villager.Profession.class, newTypes);
        MohistMod.LOGGER.info("registry.villager-profession", newTypes.size());
    }

    private static void loadEntities() {
        int origin = EntityType.values().length;
        int i = origin;
        List<EntityType> newTypes = new ArrayList<>(ForgeRegistries.ENTITIES.getEntries().size() - origin + 1); // UNKNOWN
        for (Map.Entry<ResourceLocation, net.minecraft.entity.EntityType<?>> entry : ForgeRegistries.ENTITIES.getEntries()) {
            ResourceLocation location = entry.getKey();
            net.minecraft.entity.EntityType<?> type = entry.getValue();
            EntityType entityType = null;
            boolean found = false;
            if (location.getNamespace().equals(NamespacedKey.MINECRAFT)) {
                entityType = EntityType.fromName(location.getPath());
                if (entityType != null) found = true;
                else MohistMod.LOGGER.warn("Not found {} in {}", location, EntityType.class);
            }
            if (!found) {
                String name = ResourceLocationUtil.standardize(location);
                entityType = EnumHelper.makeEnum(EntityType.class, name, i++, ENTITY_CTOR, ImmutableList.of(location.getPath(), Entity.class, -1));
                ((EntityType) (Object) entityType).setup(location, type);
                newTypes.add(entityType);
                MohistMod.LOGGER.debug("Registered {} as entity {}", location, entityType);
            }
            ENTITY_NAME_MAP.put(location.toString(), entityType);
        }
        EnumHelper.addEnums(EntityType.class, newTypes);
        MohistMod.LOGGER.info("registry.entity-type", newTypes.size());
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
