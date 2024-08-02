package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.converters.helpers.HelperItemNameV102;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.minecraft.hooks.DataHookEnforceNamespacedID;
import ca.spottedleaf.dataconverter.minecraft.hooks.DataHookValueTypeEnforceNamespaced;
import ca.spottedleaf.dataconverter.minecraft.walkers.block_name.DataWalkerBlockNames;
import ca.spottedleaf.dataconverter.minecraft.walkers.itemstack.DataWalkerItemLists;
import ca.spottedleaf.dataconverter.minecraft.walkers.item_name.DataWalkerItemNames;
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

public final class V99 {

    // Structure for all data before data upgrading was added to minecraft (pre 15w32a)

    private static final Logger LOGGER = LogUtils.getLogger();

    protected static final int VERSION = MCVersions.V15W32A - 1;

    protected static final Map<String, String> ITEM_ID_TO_TILE_ENTITY_ID = new HashMap<>();

    static {
        ITEM_ID_TO_TILE_ENTITY_ID.put("minecraft:furnace", "Furnace");
        ITEM_ID_TO_TILE_ENTITY_ID.put("minecraft:lit_furnace", "Furnace");
        ITEM_ID_TO_TILE_ENTITY_ID.put("minecraft:chest", "Chest");
        ITEM_ID_TO_TILE_ENTITY_ID.put("minecraft:trapped_chest", "Chest");
        ITEM_ID_TO_TILE_ENTITY_ID.put("minecraft:ender_chest", "EnderChest");
        ITEM_ID_TO_TILE_ENTITY_ID.put("minecraft:jukebox", "RecordPlayer");
        ITEM_ID_TO_TILE_ENTITY_ID.put("minecraft:dispenser", "Trap");
        ITEM_ID_TO_TILE_ENTITY_ID.put("minecraft:dropper", "Dropper");
        ITEM_ID_TO_TILE_ENTITY_ID.put("minecraft:sign", "Sign");
        ITEM_ID_TO_TILE_ENTITY_ID.put("minecraft:mob_spawner", "MobSpawner");
        ITEM_ID_TO_TILE_ENTITY_ID.put("minecraft:noteblock", "Music");
        ITEM_ID_TO_TILE_ENTITY_ID.put("minecraft:brewing_stand", "Cauldron");
        ITEM_ID_TO_TILE_ENTITY_ID.put("minecraft:enhanting_table", "EnchantTable");
        ITEM_ID_TO_TILE_ENTITY_ID.put("minecraft:command_block", "CommandBlock");
        ITEM_ID_TO_TILE_ENTITY_ID.put("minecraft:beacon", "Beacon");
        ITEM_ID_TO_TILE_ENTITY_ID.put("minecraft:skull", "Skull");
        ITEM_ID_TO_TILE_ENTITY_ID.put("minecraft:daylight_detector", "DLDetector");
        ITEM_ID_TO_TILE_ENTITY_ID.put("minecraft:hopper", "Hopper");
        ITEM_ID_TO_TILE_ENTITY_ID.put("minecraft:banner", "Banner");
        ITEM_ID_TO_TILE_ENTITY_ID.put("minecraft:flower_pot", "FlowerPot");
        ITEM_ID_TO_TILE_ENTITY_ID.put("minecraft:repeating_command_block", "CommandBlock");
        ITEM_ID_TO_TILE_ENTITY_ID.put("minecraft:chain_command_block", "CommandBlock");
        ITEM_ID_TO_TILE_ENTITY_ID.put("minecraft:standing_sign", "Sign");
        ITEM_ID_TO_TILE_ENTITY_ID.put("minecraft:wall_sign", "Sign");
        ITEM_ID_TO_TILE_ENTITY_ID.put("minecraft:piston_head", "Piston");
        ITEM_ID_TO_TILE_ENTITY_ID.put("minecraft:daylight_detector_inverted", "DLDetector");
        ITEM_ID_TO_TILE_ENTITY_ID.put("minecraft:unpowered_comparator", "Comparator");
        ITEM_ID_TO_TILE_ENTITY_ID.put("minecraft:powered_comparator", "Comparator");
        ITEM_ID_TO_TILE_ENTITY_ID.put("minecraft:wall_banner", "Banner");
        ITEM_ID_TO_TILE_ENTITY_ID.put("minecraft:standing_banner", "Banner");
        ITEM_ID_TO_TILE_ENTITY_ID.put("minecraft:structure_block", "Structure");
        ITEM_ID_TO_TILE_ENTITY_ID.put("minecraft:end_portal", "Airportal");
        ITEM_ID_TO_TILE_ENTITY_ID.put("minecraft:end_gateway", "EndGateway");
        ITEM_ID_TO_TILE_ENTITY_ID.put("minecraft:shield", "Banner");
    }

    private static void registerMob(final String id) {
        MCTypeRegistry.ENTITY.addWalker(VERSION, id, new DataWalkerItemLists("Equipment"));
    }

    private static void registerProjectile(final String id) {
        MCTypeRegistry.ENTITY.addWalker(VERSION, id, new DataWalkerBlockNames("inTile"));
    }

    private static void registerInventory(final String id) {
        MCTypeRegistry.TILE_ENTITY.addWalker(VERSION, id, new DataWalkerItemLists("Items"));
    }

    public static void register() {
        // entities
        MCTypeRegistry.ENTITY.addStructureWalker(VERSION, (final MapType<String> data, final long fromVersion, final long toVersion) -> {
            WalkerUtils.convert(MCTypeRegistry.ENTITY, data, "Riding", fromVersion, toVersion);

            return null;
        });
        MCTypeRegistry.ENTITY.addWalker(VERSION, "Item", new DataWalkerItems("Item"));
        registerProjectile("ThrownEgg");
        MCTypeRegistry.ENTITY.addWalker(VERSION, "Arrow", new DataWalkerBlockNames("inTile"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, "TippedArrow", new DataWalkerBlockNames("inTile"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, "SpectralArrow", new DataWalkerBlockNames("inTile"));
        registerProjectile("Snowball");
        registerProjectile("Fireball");
        registerProjectile("SmallFireball");
        registerProjectile("ThrownEnderpearl");
        MCTypeRegistry.ENTITY.addWalker(VERSION, "ThrownPotion", new DataWalkerBlockNames("inTile"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, "ThrownPotion", new DataWalkerItems("Potion"));
        registerProjectile("ThrownExpBottle");
        MCTypeRegistry.ENTITY.addWalker(VERSION, "ItemFrame", new DataWalkerItems("Item"));
        registerProjectile("WitherSkull");
        MCTypeRegistry.ENTITY.addWalker(VERSION, "FallingSand", new DataWalkerBlockNames("Block"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, "FallingSand", new DataWalkerTileEntities("TileEntityData"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, "FireworksRocketEntity", new DataWalkerItems("FireworksItem"));
        // Note: Minecart is the generic entity. It can be subtyped via an int to become one of the specific minecarts
        // (i.e rideable, chest, furnace, tnt, etc)
        // Because of this, we add all walkers to the generic type, even though they might not be needed.
        // Vanilla does not make the generic minecart convert spawners, but we do.
        MCTypeRegistry.ENTITY.addWalker(VERSION, "Minecart", new DataWalkerBlockNames("DisplayTile")); // for all minecart types
        MCTypeRegistry.ENTITY.addWalker(VERSION, "Minecart", new DataWalkerItemLists("Items")); // for chest types
        MCTypeRegistry.ENTITY.addWalker(VERSION, "Minecart", (final MapType<String> data, final long fromVersion, final long toVersion) -> {
            MCTypeRegistry.UNTAGGED_SPAWNER.convert(data, fromVersion, toVersion);
            return null;
        }); // for spawner type
        MCTypeRegistry.ENTITY.addWalker(VERSION, "MinecartRideable", new DataWalkerBlockNames("DisplayTile"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, "MinecartChest", new DataWalkerBlockNames("DisplayTile"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, "MinecartChest", new DataWalkerItemLists("Items"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, "MinecartFurnace", new DataWalkerBlockNames("DisplayTile"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, "MinecartTNT", new DataWalkerBlockNames("DisplayTile"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, "MinecartSpawner", new DataWalkerBlockNames("DisplayTile"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, "MinecartSpawner", (final MapType<String> data, final long fromVersion, final long toVersion) -> {
            MCTypeRegistry.UNTAGGED_SPAWNER.convert(data, fromVersion, toVersion);
            return null;
        });
        MCTypeRegistry.ENTITY.addWalker(VERSION, "MinecartHopper", new DataWalkerBlockNames("DisplayTile"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, "MinecartHopper", new DataWalkerItemLists("Items"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, "MinecartCommandBlock", new DataWalkerBlockNames("DisplayTile"));
        registerMob("ArmorStand");
        registerMob("Creeper");
        registerMob("Skeleton");
        registerMob("Spider");
        registerMob("Giant");
        registerMob("Zombie");
        registerMob("Slime");
        registerMob("Ghast");
        registerMob("PigZombie");
        MCTypeRegistry.ENTITY.addWalker(VERSION, "Enderman", new DataWalkerBlockNames("carried"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, "Enderman", new DataWalkerItemLists("Equipment"));
        registerMob("CaveSpider");
        registerMob("Silverfish");
        registerMob("Blaze");
        registerMob("LavaSlime");
        registerMob("EnderDragon");
        registerMob("WitherBoss");
        registerMob("Bat");
        registerMob("Witch");
        registerMob("Endermite");
        registerMob("Guardian");
        registerMob("Pig");
        registerMob("Sheep");
        registerMob("Cow");
        registerMob("Chicken");
        registerMob("Squid");
        registerMob("Wolf");
        registerMob("MushroomCow");
        registerMob("SnowMan");
        registerMob("Ozelot");
        registerMob("VillagerGolem");
        MCTypeRegistry.ENTITY.addWalker(VERSION, "EntityHorse", new DataWalkerItemLists("Items", "Equipment"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, "EntityHorse", new DataWalkerItems("ArmorItem", "SaddleItem"));
        registerMob("Rabbit");
        MCTypeRegistry.ENTITY.addWalker(VERSION, "Villager", new DataWalkerItemLists("Inventory", "Equipment"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, "Villager", (final MapType<String> data, final long fromVersion, final long toVersion) -> {
            final MapType<String> offers = data.getMap("Offers");
            if (offers != null) {
                final ListType recipes = offers.getList("Recipes", ObjectType.MAP);
                if (recipes != null) {
                    for (int i = 0; i < recipes.size(); ++i) {
                        final MapType<String> recipe = recipes.getMap(i);

                        WalkerUtils.convert(MCTypeRegistry.ITEM_STACK, recipe, "buy", fromVersion, toVersion);
                        WalkerUtils.convert(MCTypeRegistry.ITEM_STACK, recipe, "buyB", fromVersion, toVersion);
                        WalkerUtils.convert(MCTypeRegistry.ITEM_STACK, recipe, "sell", fromVersion, toVersion);
                    }
                }
            }

            return null;
        });
        registerMob("Shulker");

        // tile entities

        // Inventory -> new DataWalkerItemLists("Items")
        registerInventory("Furnace");
        registerInventory("Chest");
        MCTypeRegistry.TILE_ENTITY.addWalker(VERSION, "RecordPlayer", new DataWalkerItems("RecordItem"));
        registerInventory("Trap");
        registerInventory("Dropper");
        MCTypeRegistry.TILE_ENTITY.addWalker(VERSION, "MobSpawner", (final MapType<String> data, final long fromVersion, final long toVersion) -> {
            MCTypeRegistry.UNTAGGED_SPAWNER.convert(data, fromVersion, toVersion);
            return null;
        });
        registerInventory("Cauldron");
        registerInventory("Hopper");
        // Note: Vanilla does not properly handle this case, it will not convert int ids!
        MCTypeRegistry.TILE_ENTITY.addWalker(VERSION, "FlowerPot", new DataWalkerItemNames("Item"));

        // rest

        MCTypeRegistry.ITEM_STACK.addStructureWalker(VERSION, (final MapType<String> data, final long fromVersion, final long toVersion) -> {
            WalkerUtils.convert(MCTypeRegistry.ITEM_NAME, data, "id", fromVersion, toVersion);

            final MapType<String> tag = data.getMap("tag");
            if (tag == null) {
                return null;
            }

            // only things here are in tag, if changed update if above

            WalkerUtils.convertList(MCTypeRegistry.ITEM_STACK, tag, "Items", fromVersion, toVersion);

            MapType<String> entityTag = tag.getMap("EntityTag");
            if (entityTag != null) {
                String itemId = getStringId(data.getString("id"));
                final String entityId;
                if ("minecraft:armor_stand".equals(itemId)) {
                    // The check for version id is removed here. For whatever reason, the legacy
                    // data converters used entity id "minecraft:armor_stand" when version was greater-than 514,
                    // but entity ids were not namespaced until V705! So somebody fucked up the legacy converters.
                    // DFU agrees with my analysis here, it will only set the entityId here to the namespaced variant
                    // with the V705 schema.
                    entityId = "ArmorStand";
                } else if ("minecraft:item_frame".equals(itemId)) {
                    // add missing item_frame entity id
                    entityId = "ItemFrame";
                } else {
                    entityId = entityTag.getString("id");
                }

                final boolean removeId;
                if (entityId == null) {
                    if (!"minecraft:air".equals(itemId)) {
                        LOGGER.warn("Unable to resolve Entity for ItemStack (V99): " + data.getGeneric("id"));
                    }
                    removeId = false;
                } else {
                    removeId = !entityTag.hasKey("id", ObjectType.STRING);
                    if (removeId) {
                        entityTag.setString("id", entityId);
                    }
                }

                final MapType<String> replace = MCTypeRegistry.ENTITY.convert(entityTag, fromVersion, toVersion);

                if (replace != null) {
                    entityTag = replace;
                    tag.setMap("EntityTag", entityTag);
                }
                if (removeId) {
                    entityTag.remove("id");
                }
            }

            MapType<String> blockEntityTag = tag.getMap("BlockEntityTag");
            if (blockEntityTag != null) {
                final String itemId = getStringId(data.getString("id"));
                final String entityId = ITEM_ID_TO_TILE_ENTITY_ID.get(itemId);
                final boolean removeId;
                if (entityId == null) {
                    if (!"minecraft:air".equals(itemId)) {
                        LOGGER.warn("Unable to resolve BlockEntity for ItemStack (V99): " + data.getGeneric("id"));
                    }
                    removeId = false;
                } else {
                    removeId = !blockEntityTag.hasKey("id", ObjectType.STRING);
                    blockEntityTag.setString("id", entityId);
                }
                final MapType<String> replace = MCTypeRegistry.TILE_ENTITY.convert(blockEntityTag, fromVersion, toVersion);
                if (replace != null) {
                    blockEntityTag = replace;
                    tag.setMap("BlockEntityTag", blockEntityTag);
                }
                if (removeId) {
                    blockEntityTag.remove("id");
                }
            }

            WalkerUtils.convertList(MCTypeRegistry.BLOCK_NAME, tag, "CanDestroy", fromVersion, toVersion);
            WalkerUtils.convertList(MCTypeRegistry.BLOCK_NAME, tag, "CanPlaceOn", fromVersion, toVersion);

            return null;
        });

        MCTypeRegistry.PLAYER.addStructureWalker(VERSION, new DataWalkerItemLists("Inventory", "EnderItems"));

        MCTypeRegistry.CHUNK.addStructureWalker(VERSION, (final MapType<String> data, final long fromVersion, final long toVersion) -> {
            final MapType<String> level = data.getMap("Level");
            if (level == null) {
                return null;
            }

            WalkerUtils.convertList(MCTypeRegistry.ENTITY, level, "Entities", fromVersion, toVersion);
            WalkerUtils.convertList(MCTypeRegistry.TILE_ENTITY, level, "TileEntities", fromVersion, toVersion);

            final ListType tileTicks = level.getList("TileTicks", ObjectType.MAP);
            if (tileTicks != null) {
                for (int i = 0, len = tileTicks.size(); i < len; ++i) {
                    final MapType<String> tileTick = tileTicks.getMap(i);
                    WalkerUtils.convert(MCTypeRegistry.BLOCK_NAME, tileTick, "i", fromVersion, toVersion);
                }
            }

            return null;
        });

        MCTypeRegistry.ENTITY_CHUNK.addStructureWalker(VERSION, (final MapType<String> data, final long fromVersion, final long toVersion) -> {
            WalkerUtils.convertList(MCTypeRegistry.ENTITY, data, "Entities", fromVersion, toVersion);

            return null;
        });

        MCTypeRegistry.SAVED_DATA.addStructureWalker(VERSION, (final MapType<String> root, final long fromVersion, final long toVersion) -> {
            final MapType<String> data = root.getMap("data");
            if (data == null) {
                return null;
            }

            WalkerUtils.convertValues(MCTypeRegistry.STRUCTURE_FEATURE, data, "Features", fromVersion, toVersion);
            WalkerUtils.convertList(MCTypeRegistry.OBJECTIVE, data, "Objectives", fromVersion, toVersion);
            WalkerUtils.convertList(MCTypeRegistry.TEAM, data, "Teams", fromVersion, toVersion);

            return null;
        });


        // Enforce namespacing for ids
        MCTypeRegistry.BLOCK_NAME.addStructureHook(VERSION, new DataHookValueTypeEnforceNamespaced());
        MCTypeRegistry.ITEM_NAME.addStructureHook(VERSION, new DataHookValueTypeEnforceNamespaced());
        MCTypeRegistry.ITEM_STACK.addStructureHook(VERSION, new DataHookEnforceNamespacedID());

        // Entity is absent; the String form is not yet namespaced, unlike the above.
    }

    protected static String getStringId(final Object id) {
        if (id instanceof String) {
            return (String)id;
        } else if (id instanceof Number) {
            return HelperItemNameV102.getNameFromId(((Number)id).intValue());
        } else {
            return null;
        }
    }

    private V99() {
        throw new RuntimeException();
    }
}
