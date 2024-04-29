package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.converters.blockname.ConverterAbstractBlockRename;
import com.google.common.collect.ImmutableMap;
import java.util.Map;

public final class V1515 {

    protected static final int VERSION = MCVersions.V1_13_PRE7 + 2;

    public static final Map<String, String> RENAMED_BLOCK_IDS = ImmutableMap.<String, String>builder()
            .put("minecraft:tube_coral_fan", "minecraft:tube_coral_wall_fan")
            .put("minecraft:brain_coral_fan", "minecraft:brain_coral_wall_fan")
            .put("minecraft:bubble_coral_fan", "minecraft:bubble_coral_wall_fan")
            .put("minecraft:fire_coral_fan", "minecraft:fire_coral_wall_fan")
            .put("minecraft:horn_coral_fan", "minecraft:horn_coral_wall_fan")
            .build();

    private V1515() {}

    public static void register() {
        ConverterAbstractBlockRename.register(VERSION, RENAMED_BLOCK_IDS::get);
    }
}
