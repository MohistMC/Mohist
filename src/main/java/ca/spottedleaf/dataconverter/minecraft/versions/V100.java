package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.minecraft.walkers.block_name.DataWalkerBlockNames;
import ca.spottedleaf.dataconverter.minecraft.walkers.itemstack.DataWalkerItemLists;
import ca.spottedleaf.dataconverter.minecraft.walkers.itemstack.DataWalkerItems;
import ca.spottedleaf.dataconverter.minecraft.walkers.generic.WalkerUtils;
import ca.spottedleaf.dataconverter.types.ObjectType;
import ca.spottedleaf.dataconverter.types.ListType;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.Types;

public final class V100 {

    protected static final int VERSION = MCVersions.V15W32A;

    private static void registerMob(final String id) {
        MCTypeRegistry.ENTITY.addWalker(VERSION, id, new DataWalkerItemLists("ArmorItems", "HandItems"));
    }

    public static void register() {
        MCTypeRegistry.ENTITY.addStructureConverter(new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final ListType equipment = data.getList("Equipment", ObjectType.MAP);
                data.remove("Equipment");

                if (equipment != null) {
                    if (equipment.size() > 0 && data.getListUnchecked("HandItems") == null) {
                        final ListType handItems = Types.NBT.createEmptyList();
                        data.setList("HandItems", handItems);
                        handItems.addMap(equipment.getMap(0));
                        handItems.addMap(Types.NBT.createEmptyMap());
                    }

                    if (equipment.size() > 1 && data.getListUnchecked("ArmorItems") == null) {
                        final ListType armorItems = Types.NBT.createEmptyList();
                        data.setList("ArmorItems", armorItems);
                        for (int i = 1; i < Math.min(equipment.size(), 5); ++i) {
                            armorItems.addMap(equipment.getMap(i));
                        }
                    }
                }

                final ListType dropChances = data.getList("DropChances", ObjectType.FLOAT);
                data.remove("DropChances");

                if (dropChances != null) {
                    if (data.getListUnchecked("HandDropChances") == null) {
                        final ListType handDropChances = Types.NBT.createEmptyList();
                        data.setList("HandDropChances", handDropChances);
                        if (0 < dropChances.size()) {
                            handDropChances.addFloat(dropChances.getFloat(0));
                        } else {
                            handDropChances.addFloat(0.0F);
                        }
                        handDropChances.addFloat(0.0F);
                    }

                    if (data.getListUnchecked("ArmorDropChances") == null) {
                        final ListType armorDropChances = Types.NBT.createEmptyList();
                        data.setList("ArmorDropChances", armorDropChances);
                        for (int i = 1; i < 5; ++i) {
                            if (i < dropChances.size()) {
                                armorDropChances.addFloat(dropChances.getFloat(i));
                            } else {
                                armorDropChances.addFloat(0.0F);
                            }
                        }
                    }
                }

                return null;
            }
        });

        registerMob("ArmorStand");
        registerMob("Creeper");
        registerMob("Skeleton");
        registerMob("Spider");
        registerMob("Giant");
        registerMob("Zombie");
        registerMob("Slime");
        registerMob("Ghast");
        registerMob("PigZombie");
        registerMob("Enderman");
        MCTypeRegistry.ENTITY.addWalker(VERSION, "Enderman", new DataWalkerBlockNames("carried"));
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
        MCTypeRegistry.ENTITY.addWalker(VERSION, "EntityHorse", new DataWalkerItemLists("Items", "ArmorItems", "HandItems"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, "EntityHorse", new DataWalkerItems("ArmorItem", "SaddleItem"));
        registerMob("Rabbit");
        MCTypeRegistry.ENTITY.addWalker(VERSION, "Villager", (final MapType<String> data, final long fromVersion, final long toVersion) -> {
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
        registerMob("Shulker");

        MCTypeRegistry.STRUCTURE.addStructureWalker(VERSION, (final MapType<String> data, final long fromVersion, final long toVersion) -> {
            final ListType entities = data.getList("entities", ObjectType.MAP);
            if (entities != null) {
                for (int i = 0, len = entities.size(); i < len; ++i) {
                    WalkerUtils.convert(MCTypeRegistry.ENTITY, entities.getMap(i), "nbt", fromVersion, toVersion);
                }
            }

            final ListType blocks = data.getList("blocks", ObjectType.MAP);
            if (blocks != null) {
                for (int i = 0, len = blocks.size(); i < len; ++i) {
                    WalkerUtils.convert(MCTypeRegistry.TILE_ENTITY, blocks.getMap(i), "nbt", fromVersion, toVersion);
                }
            }

            WalkerUtils.convertList(MCTypeRegistry.BLOCK_STATE, data, "palette", fromVersion, toVersion);

            return null;
        });
    }

    private V100() {}

}
