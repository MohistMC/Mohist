package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.converters.blockname.ConverterAbstractBlockRename;
import ca.spottedleaf.dataconverter.minecraft.converters.itemname.ConverterAbstractItemRename;
import com.google.common.collect.ImmutableMap;
import java.util.Map;

public final class V1480 {

    protected static final int VERSION = MCVersions.V18W14A + 1;

    public static final Map<String, String> RENAMED_IDS = ImmutableMap.<String, String>builder()
            .put("minecraft:blue_coral", "minecraft:tube_coral_block")
            .put("minecraft:pink_coral", "minecraft:brain_coral_block")
            .put("minecraft:purple_coral", "minecraft:bubble_coral_block")
            .put("minecraft:red_coral", "minecraft:fire_coral_block")
            .put("minecraft:yellow_coral", "minecraft:horn_coral_block")
            .put("minecraft:blue_coral_plant", "minecraft:tube_coral")
            .put("minecraft:pink_coral_plant", "minecraft:brain_coral")
            .put("minecraft:purple_coral_plant", "minecraft:bubble_coral")
            .put("minecraft:red_coral_plant", "minecraft:fire_coral")
            .put("minecraft:yellow_coral_plant", "minecraft:horn_coral")
            .put("minecraft:blue_coral_fan", "minecraft:tube_coral_fan")
            .put("minecraft:pink_coral_fan", "minecraft:brain_coral_fan")
            .put("minecraft:purple_coral_fan", "minecraft:bubble_coral_fan")
            .put("minecraft:red_coral_fan", "minecraft:fire_coral_fan")
            .put("minecraft:yellow_coral_fan", "minecraft:horn_coral_fan")
            .put("minecraft:blue_dead_coral", "minecraft:dead_tube_coral")
            .put("minecraft:pink_dead_coral", "minecraft:dead_brain_coral")
            .put("minecraft:purple_dead_coral", "minecraft:dead_bubble_coral")
            .put("minecraft:red_dead_coral", "minecraft:dead_fire_coral")
            .put("minecraft:yellow_dead_coral", "minecraft:dead_horn_coral")
            .build();

    public static void register() {
        ConverterAbstractBlockRename.register(VERSION, RENAMED_IDS::get);
        ConverterAbstractItemRename.register(VERSION, RENAMED_IDS::get);
    }

    private V1480() {}
}
