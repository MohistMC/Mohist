package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.MapType;

public final class V3094 {

    public static final int VERSION = MCVersions.V22W17A + 1;

    private static final String[] SOUND_VARIANT_TO_INSTRUMENT = new String[] {
            "minecraft:ponder_goat_horn",
            "minecraft:sing_goat_horn",
            "minecraft:seek_goat_horn",
            "minecraft:feel_goat_horn",
            "minecraft:admire_goat_horn",
            "minecraft:call_goat_horn",
            "minecraft:yearn_goat_horn",
            "minecraft:dream_goat_horn"
    };

    public static void register() {
        MCTypeRegistry.ITEM_STACK.addConverterForId("minecraft:goat_horn", new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final MapType<String> tag = data.getMap("tag");

                if (tag == null) {
                    return null;
                }

                final int soundVariant = tag.getInt("SoundVariant");
                tag.remove("SoundVariant");

                tag.setString("instrument", SOUND_VARIANT_TO_INSTRUMENT[soundVariant < 0 || soundVariant >= SOUND_VARIANT_TO_INSTRUMENT.length ? 0 : soundVariant]);

                return null;
            }
        });
    }
}
