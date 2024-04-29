package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.converters.advancements.ConverterCriteriaRename;
import ca.spottedleaf.dataconverter.minecraft.converters.entity.ConverterEntityToVariant;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.HashMap;
import java.util.Map;

public final class V3086 {

    protected static final int VERSION = MCVersions.V22W13A + 1;

    protected static final Int2ObjectOpenHashMap<String> CAT_ID_CONVERSION = new Int2ObjectOpenHashMap<>();
    static {
        CAT_ID_CONVERSION.defaultReturnValue("minecraft:tabby");
        CAT_ID_CONVERSION.put(0, "minecraft:tabby");
        CAT_ID_CONVERSION.put(1, "minecraft:black");
        CAT_ID_CONVERSION.put(2, "minecraft:red");
        CAT_ID_CONVERSION.put(3, "minecraft:siamese");
        CAT_ID_CONVERSION.put(4, "minecraft:british");
        CAT_ID_CONVERSION.put(5, "minecraft:calico");
        CAT_ID_CONVERSION.put(6, "minecraft:persian");
        CAT_ID_CONVERSION.put(7, "minecraft:ragdoll");
        CAT_ID_CONVERSION.put(8, "minecraft:white");
        CAT_ID_CONVERSION.put(9, "minecraft:jellie");
        CAT_ID_CONVERSION.put(10, "minecraft:all_black");
    }

    protected static final Map<String, String> CAT_ADVANCEMENTS_CONVERSION = new HashMap<>(ImmutableMap.<String, String>builder()
            .put("textures/entity/cat/tabby.png", "minecraft:tabby")
            .put("textures/entity/cat/black.png", "minecraft:black")
            .put("textures/entity/cat/red.png", "minecraft:red")
            .put("textures/entity/cat/siamese.png", "minecraft:siamese")
            .put("textures/entity/cat/british_shorthair.png", "minecraft:british")
            .put("textures/entity/cat/calico.png", "minecraft:calico")
            .put("textures/entity/cat/persian.png", "minecraft:persian")
            .put("textures/entity/cat/ragdoll.png", "minecraft:ragdoll")
            .put("textures/entity/cat/white.png", "minecraft:white")
            .put("textures/entity/cat/jellie.png", "minecraft:jellie")
            .put("textures/entity/cat/all_black.png", "minecraft:all_black")
            .build()
    );

    public static void register() {
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:cat", new ConverterEntityToVariant(VERSION, "CatType", CAT_ID_CONVERSION::get));
        MCTypeRegistry.ADVANCEMENTS.addStructureConverter(new ConverterCriteriaRename(VERSION, "minecraft:husbandry/complete_catalogue", CAT_ADVANCEMENTS_CONVERSION::get));
    }
}
