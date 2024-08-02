package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.ListType;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.ObjectType;

import java.util.HashSet;
import java.util.Set;

public final class V3322 {

    private static final int VERSION = MCVersions.V23W04A + 1;

    private static final Set<String> EFFECT_ITEM_TYPES = new HashSet<>(
            Set.of(
                    "minecraft:potion",
                    "minecraft:splash_potion",
                    "minecraft:lingering_potion",
                    "minecraft:tipped_arrow"
            )
    );

    private static void updateEffectList(final MapType<String> root, final String path) {
        if (root == null) {
            return;
        }

        final ListType effects = root.getList(path, ObjectType.MAP);

        if (effects == null) {
            return;
        }

        for (int i = 0, len = effects.size(); i < len; ++i) {
            final MapType<String> data = effects.getMap(i);
            final MapType<String> factorData = data.getMap("FactorCalculationData");
            if (factorData == null) {
                continue;
            }

            final int timestamp = factorData.getInt("effect_changed_timestamp", -1);
            factorData.remove("effect_changed_timestamp");

            final int duration = data.getInt("Duration", -1);

            final int ticksActive = timestamp - duration;
            factorData.setInt("ticks_active", ticksActive);
        }
    }

    public static void register() {
        final DataConverter<MapType<String>, MapType<String>> entityEffectFix = new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                updateEffectList(data, "Effects");
                updateEffectList(data, "ActiveEffects");
                updateEffectList(data, "CustomPotionEffects");
                return null;
            }
        };

        MCTypeRegistry.PLAYER.addStructureConverter(entityEffectFix);
        MCTypeRegistry.ENTITY.addStructureConverter(entityEffectFix);

        MCTypeRegistry.ITEM_STACK.addStructureConverter(new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final String id = data.getString("id");
                if (!EFFECT_ITEM_TYPES.contains(id)) {
                    return null;
                }

                updateEffectList(data.getMap("tag"), "CustomPotionEffects");

                return null;
            }
        });
    }
}
