package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.converters.entity.ConverterAbstractEntityRename;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.minecraft.hooks.DataHookEnforceNamespacedID;
import ca.spottedleaf.dataconverter.minecraft.hooks.DataHookValueTypeEnforceNamespaced;
import ca.spottedleaf.dataconverter.minecraft.walkers.block_name.DataWalkerBlockNames;
import ca.spottedleaf.dataconverter.minecraft.walkers.itemstack.DataWalkerItemLists;
import ca.spottedleaf.dataconverter.minecraft.walkers.itemstack.DataWalkerItems;
import ca.spottedleaf.dataconverter.minecraft.walkers.tile_entity.DataWalkerTileEntities;
import ca.spottedleaf.dataconverter.minecraft.walkers.generic.WalkerUtils;
import ca.spottedleaf.dataconverter.types.ObjectType;
import ca.spottedleaf.dataconverter.types.ListType;
import ca.spottedleaf.dataconverter.types.MapType;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import java.util.HashMap;
import java.util.Map;

public final class V705 {

    private static final Logger LOGGER = LogUtils.getLogger();

    protected static final int VERSION = MCVersions.V1_10_2 + 193;

    protected static final Map<String, String> ENTITY_ID_UPDATE = new HashMap<>();
    static {
        ENTITY_ID_UPDATE.put("AreaEffectCloud", "minecraft:area_effect_cloud");
        ENTITY_ID_UPDATE.put("ArmorStand", "minecraft:armor_stand");
        ENTITY_ID_UPDATE.put("Arrow", "minecraft:arrow");
        ENTITY_ID_UPDATE.put("Bat", "minecraft:bat");
        ENTITY_ID_UPDATE.put("Blaze", "minecraft:blaze");
        ENTITY_ID_UPDATE.put("Boat", "minecraft:boat");
        ENTITY_ID_UPDATE.put("CaveSpider", "minecraft:cave_spider");
        ENTITY_ID_UPDATE.put("Chicken", "minecraft:chicken");
        ENTITY_ID_UPDATE.put("Cow", "minecraft:cow");
        ENTITY_ID_UPDATE.put("Creeper", "minecraft:creeper");
        ENTITY_ID_UPDATE.put("Donkey", "minecraft:donkey");
        ENTITY_ID_UPDATE.put("DragonFireball", "minecraft:dragon_fireball");
        ENTITY_ID_UPDATE.put("ElderGuardian", "minecraft:elder_guardian");
        ENTITY_ID_UPDATE.put("EnderCrystal", "minecraft:ender_crystal");
        ENTITY_ID_UPDATE.put("EnderDragon", "minecraft:ender_dragon");
        ENTITY_ID_UPDATE.put("Enderman", "minecraft:enderman");
        ENTITY_ID_UPDATE.put("Endermite", "minecraft:endermite");
        ENTITY_ID_UPDATE.put("EyeOfEnderSignal", "minecraft:eye_of_ender_signal");
        ENTITY_ID_UPDATE.put("FallingSand", "minecraft:falling_block");
        ENTITY_ID_UPDATE.put("Fireball", "minecraft:fireball");
        ENTITY_ID_UPDATE.put("FireworksRocketEntity", "minecraft:fireworks_rocket");
        ENTITY_ID_UPDATE.put("Ghast", "minecraft:ghast");
        ENTITY_ID_UPDATE.put("Giant", "minecraft:giant");
        ENTITY_ID_UPDATE.put("Guardian", "minecraft:guardian");
        ENTITY_ID_UPDATE.put("Horse", "minecraft:horse");
        ENTITY_ID_UPDATE.put("Husk", "minecraft:husk");
        ENTITY_ID_UPDATE.put("Item", "minecraft:item");
        ENTITY_ID_UPDATE.put("ItemFrame", "minecraft:item_frame");
        ENTITY_ID_UPDATE.put("LavaSlime", "minecraft:magma_cube");
        ENTITY_ID_UPDATE.put("LeashKnot", "minecraft:leash_knot");
        ENTITY_ID_UPDATE.put("MinecartChest", "minecraft:chest_minecart");
        ENTITY_ID_UPDATE.put("MinecartCommandBlock", "minecraft:commandblock_minecart");
        ENTITY_ID_UPDATE.put("MinecartFurnace", "minecraft:furnace_minecart");
        ENTITY_ID_UPDATE.put("MinecartHopper", "minecraft:hopper_minecart");
        ENTITY_ID_UPDATE.put("MinecartRideable", "minecraft:minecart");
        ENTITY_ID_UPDATE.put("MinecartSpawner", "minecraft:spawner_minecart");
        ENTITY_ID_UPDATE.put("MinecartTNT", "minecraft:tnt_minecart");
        ENTITY_ID_UPDATE.put("Mule", "minecraft:mule");
        ENTITY_ID_UPDATE.put("MushroomCow", "minecraft:mooshroom");
        ENTITY_ID_UPDATE.put("Ozelot", "minecraft:ocelot");
        ENTITY_ID_UPDATE.put("Painting", "minecraft:painting");
        ENTITY_ID_UPDATE.put("Pig", "minecraft:pig");
        ENTITY_ID_UPDATE.put("PigZombie", "minecraft:zombie_pigman");
        ENTITY_ID_UPDATE.put("PolarBear", "minecraft:polar_bear");
        ENTITY_ID_UPDATE.put("PrimedTnt", "minecraft:tnt");
        ENTITY_ID_UPDATE.put("Rabbit", "minecraft:rabbit");
        ENTITY_ID_UPDATE.put("Sheep", "minecraft:sheep");
        ENTITY_ID_UPDATE.put("Shulker", "minecraft:shulker");
        ENTITY_ID_UPDATE.put("ShulkerBullet", "minecraft:shulker_bullet");
        ENTITY_ID_UPDATE.put("Silverfish", "minecraft:silverfish");
        ENTITY_ID_UPDATE.put("Skeleton", "minecraft:skeleton");
        ENTITY_ID_UPDATE.put("SkeletonHorse", "minecraft:skeleton_horse");
        ENTITY_ID_UPDATE.put("Slime", "minecraft:slime");
        ENTITY_ID_UPDATE.put("SmallFireball", "minecraft:small_fireball");
        ENTITY_ID_UPDATE.put("SnowMan", "minecraft:snowman");
        ENTITY_ID_UPDATE.put("Snowball", "minecraft:snowball");
        ENTITY_ID_UPDATE.put("SpectralArrow", "minecraft:spectral_arrow");
        ENTITY_ID_UPDATE.put("Spider", "minecraft:spider");
        ENTITY_ID_UPDATE.put("Squid", "minecraft:squid");
        ENTITY_ID_UPDATE.put("Stray", "minecraft:stray");
        ENTITY_ID_UPDATE.put("ThrownEgg", "minecraft:egg");
        ENTITY_ID_UPDATE.put("ThrownEnderpearl", "minecraft:ender_pearl");
        ENTITY_ID_UPDATE.put("ThrownExpBottle", "minecraft:xp_bottle");
        ENTITY_ID_UPDATE.put("ThrownPotion", "minecraft:potion");
        ENTITY_ID_UPDATE.put("Villager", "minecraft:villager");
        ENTITY_ID_UPDATE.put("VillagerGolem", "minecraft:villager_golem");
        ENTITY_ID_UPDATE.put("Witch", "minecraft:witch");
        ENTITY_ID_UPDATE.put("WitherBoss", "minecraft:wither");
        ENTITY_ID_UPDATE.put("WitherSkeleton", "minecraft:wither_skeleton");
        ENTITY_ID_UPDATE.put("WitherSkull", "minecraft:wither_skull");
        ENTITY_ID_UPDATE.put("Wolf", "minecraft:wolf");
        ENTITY_ID_UPDATE.put("XPOrb", "minecraft:xp_orb");
        ENTITY_ID_UPDATE.put("Zombie", "minecraft:zombie");
        ENTITY_ID_UPDATE.put("ZombieHorse", "minecraft:zombie_horse");
        ENTITY_ID_UPDATE.put("ZombieVillager", "minecraft:zombie_villager");
    }

    private static void registerMob(final String id) {
        MCTypeRegistry.ENTITY.addWalker(VERSION, id, new DataWalkerItemLists("ArmorItems", "HandItems"));
    }

    private static void registerThrowableProjectile(final String id) {
        MCTypeRegistry.ENTITY.addWalker(VERSION, id, new DataWalkerBlockNames("inTile"));
    }

    public static void register() {
        ConverterAbstractEntityRename.register(VERSION, ENTITY_ID_UPDATE::get);

        registerMob("minecraft:armor_stand");
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:arrow", new DataWalkerBlockNames("inTile"));
        registerMob("minecraft:bat");
        registerMob("minecraft:blaze");
        registerMob("minecraft:cave_spider");
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:chest_minecart", new DataWalkerBlockNames("DisplayTile"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:chest_minecart", new DataWalkerItemLists("Items"));
        registerMob("minecraft:chicken");
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:commandblock_minecart", new DataWalkerBlockNames("DisplayTile"));
        registerMob("minecraft:cow");
        registerMob("minecraft:creeper");
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:donkey", new DataWalkerItemLists("Items", "ArmorItems", "HandItems"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:donkey", new DataWalkerItems("SaddleItem"));
        registerThrowableProjectile("minecraft:egg");
        registerMob("minecraft:elder_guardian");
        registerMob("minecraft:ender_dragon");
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:enderman", new DataWalkerItemLists("ArmorItems", "HandItems"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:enderman", new DataWalkerBlockNames("carried"));
        registerMob("minecraft:endermite");
        registerThrowableProjectile("minecraft:ender_pearl");
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:falling_block", new DataWalkerBlockNames("Block"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:falling_block", new DataWalkerTileEntities("TileEntityData"));
        registerThrowableProjectile("minecraft:fireball");
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:fireworks_rocket", new DataWalkerItems("FireworksItem"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:furnace_minecart", new DataWalkerBlockNames("DisplayTile"));
        registerMob("minecraft:ghast");
        registerMob("minecraft:giant");
        registerMob("minecraft:guardian");
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:hopper_minecart", new DataWalkerBlockNames("DisplayTile"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:hopper_minecart", new DataWalkerItemLists("Items"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:horse", new DataWalkerItems("ArmorItem", "SaddleItem"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:horse", new DataWalkerItemLists("ArmorItems", "HandItems"));
        registerMob("minecraft:husk");
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:item", new DataWalkerItems("Item"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:item_frame", new DataWalkerItems("Item"));
        registerMob("minecraft:magma_cube");
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:minecart", new DataWalkerBlockNames("DisplayTile"));
        registerMob("minecraft:mooshroom");
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:mule", new DataWalkerItemLists("Items", "ArmorItems", "HandItems"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:mule", new DataWalkerItems("SaddleItem"));
        registerMob("minecraft:ocelot");
        registerMob("minecraft:pig");
        registerMob("minecraft:polar_bear");
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:potion", new DataWalkerItems("Potion"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:potion", new DataWalkerBlockNames("inTile"));
        registerMob("minecraft:rabbit");
        registerMob("minecraft:sheep");
        registerMob("minecraft:shulker");
        registerMob("minecraft:silverfish");
        registerMob("minecraft:skeleton");
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:skeleton_horse", new DataWalkerItemLists("ArmorItems", "HandItems"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:skeleton_horse", new DataWalkerItems("SaddleItem"));
        registerMob("minecraft:slime");
        registerThrowableProjectile("minecraft:small_fireball");
        registerThrowableProjectile("minecraft:snowball");
        registerMob("minecraft:snowman");
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:spawner_minecart", new DataWalkerBlockNames("DisplayTile"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:spawner_minecart", (final MapType<String> data, final long fromVersion, final long toVersion) -> {
            MCTypeRegistry.UNTAGGED_SPAWNER.convert(data, fromVersion, toVersion);
            return null;
        });
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:spectral_arrow", new DataWalkerBlockNames("inTile"));
        registerMob("minecraft:spider");
        registerMob("minecraft:squid");
        registerMob("minecraft:stray");
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:tnt_minecart", new DataWalkerBlockNames("DisplayTile"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:villager", (final MapType<String> data, final long fromVersion, final long toVersion) -> {
            WalkerUtils.convertList(MCTypeRegistry.ITEM_STACK, data, "Inventory", fromVersion, toVersion);

            final MapType<String> offers = data.getMap("Offers");
            if (offers != null) {
                final ListType recipes = offers.getList("Recipes", ObjectType.MAP);
                if (recipes != null) {
                    for (int i = 0, len = recipes.size(); i < len; ++i) {
                        final MapType<String> recipe = recipes.getMap(i);
                        WalkerUtils.convert(MCTypeRegistry.ITEM_STACK, recipe, "buy", fromVersion, toVersion);
                        WalkerUtils.convert(MCTypeRegistry.ITEM_STACK, recipe, "buyB", fromVersion, toVersion);
                        WalkerUtils.convert(MCTypeRegistry.ITEM_STACK, recipe, "sell", fromVersion, toVersion);
                    }
                }
            }

            WalkerUtils.convertList(MCTypeRegistry.ITEM_STACK, data, "ArmorItems", fromVersion, toVersion);
            WalkerUtils.convertList(MCTypeRegistry.ITEM_STACK, data, "HandItems", fromVersion, toVersion);

            return null;
        });
        registerMob("minecraft:villager_golem");
        registerMob("minecraft:witch");
        registerMob("minecraft:wither");
        registerMob("minecraft:wither_skeleton");
        registerThrowableProjectile("minecraft:wither_skull");
        registerMob("minecraft:wolf");
        registerThrowableProjectile("minecraft:xp_bottle");
        registerMob("minecraft:zombie");
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:zombie_horse", new DataWalkerItems("SaddleItem"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:zombie_horse", new DataWalkerItemLists("ArmorItems", "HandItems"));
        registerMob("minecraft:zombie_pigman");
        registerMob("minecraft:zombie_villager");
        registerMob("minecraft:evocation_illager");
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:llama", new DataWalkerItemLists("Items", "ArmorItems", "HandItems"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:llama", new DataWalkerItems("SaddleItem", "DecorItem"));
        registerMob("minecraft:vex");
        registerMob("minecraft:vindication_illager");
        // Don't need to re-register itemstack walker, the V704 will correctly choose the right id for armorstand based on
        // the source version

        // Enforce namespace for ids
        MCTypeRegistry.ENTITY.addStructureHook(VERSION, new DataHookEnforceNamespacedID());
        MCTypeRegistry.ENTITY_NAME.addStructureHook(VERSION, new DataHookValueTypeEnforceNamespaced());
    }

    private V705() {}

}
