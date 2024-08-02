package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.converters.entity.ConverterEntityToVariant;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public final class V3087 {

    protected static final int VERSION = MCVersions.V22W13A + 2;

    protected static Int2ObjectOpenHashMap<String> FROG_ID_CONVERSION = new Int2ObjectOpenHashMap<>();
    static {
        FROG_ID_CONVERSION.put(0, "minecraft:temperate");
        FROG_ID_CONVERSION.put(1, "minecraft:warm");
        FROG_ID_CONVERSION.put(2, "minecraft:cold");
    }

    public static void register() {
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:frog", new ConverterEntityToVariant(VERSION, "Variant", FROG_ID_CONVERSION::get));
    }
}
