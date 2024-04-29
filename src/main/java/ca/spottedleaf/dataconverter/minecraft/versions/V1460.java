package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.minecraft.walkers.block_name.DataWalkerBlockNames;
import ca.spottedleaf.dataconverter.minecraft.walkers.itemstack.DataWalkerItemLists;
import ca.spottedleaf.dataconverter.types.MapType;
import net.minecraft.resources.ResourceLocation;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class V1460 {

    private static final Map<String, String> MOTIVE_REMAP = new HashMap<>();

    static {
        MOTIVE_REMAP.put("donkeykong", "donkey_kong");
        MOTIVE_REMAP.put("burningskull", "burning_skull");
        MOTIVE_REMAP.put("skullandroses", "skull_and_roses");
    };

    protected static final int VERSION = MCVersions.V18W01A + 1;

    private static void registerMob(final String id) {
        MCTypeRegistry.ENTITY.addWalker(VERSION, id, new DataWalkerItemLists("ArmorItems", "HandItems"));
    }

    private static void registerThrowableProjectile(final String id) {
        MCTypeRegistry.ENTITY.addWalker(VERSION, id, new DataWalkerBlockNames("inTile"));
    }

    public static void register() {
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:painting", new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                String motive = data.getString("Motive");
                if (motive != null) {
                    motive = motive.toLowerCase(Locale.ROOT);
                    data.setString("Motive", new ResourceLocation(MOTIVE_REMAP.getOrDefault(motive, motive)).toString());
                }
                return null;
            }
        });

        // No idea why so many type redefines exist here in Vanilla. nothing about the data structure changed, it's literally a copy of
        // the existing types.
    }

    private V1460() {}

}
