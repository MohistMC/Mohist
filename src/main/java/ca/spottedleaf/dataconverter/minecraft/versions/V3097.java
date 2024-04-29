package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.converters.advancements.ConverterCriteriaRename;
import ca.spottedleaf.dataconverter.minecraft.converters.entity.ConverterEntityVariantRename;
import ca.spottedleaf.dataconverter.minecraft.converters.poi.ConverterPoiDelete;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.MapType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class V3097 {

    private static final int VERSION = MCVersions.V22W19A + 1;

    public static void register() {
        final DataConverter<MapType<String>, MapType<String>> removeFilteredBookText = new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final MapType<String> tag = data.getMap("tag");
                if (tag == null) {
                    return null;
                }

                tag.remove("filtered_title");
                tag.remove("filtered_pages");

                return null;
            }
        };
        MCTypeRegistry.ITEM_STACK.addConverterForId("minecraft:writable_book", removeFilteredBookText);
        MCTypeRegistry.ITEM_STACK.addConverterForId("minecraft:written_book", removeFilteredBookText);

        MCTypeRegistry.TILE_ENTITY.addConverterForId("minecraft:sign", new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                data.remove("FilteredText1");
                data.remove("FilteredText2");
                data.remove("FilteredText3");
                data.remove("FilteredText4");

                return null;
            }
        });

        final Map<String, String> britishRenamer = new HashMap<>(Map.of(
                "minecraft:british", "minecraft:british_shorthair"
        ));
        final Set<String> poiRemove = new HashSet<>(Set.of(
                "minecraft:unemployed",
                "minecraft:nitwit"
        ));

        MCTypeRegistry.ENTITY.addConverterForId("minecraft:cat", new ConverterEntityVariantRename(VERSION, britishRenamer::get));
        MCTypeRegistry.ADVANCEMENTS.addStructureConverter(new ConverterCriteriaRename(VERSION, "minecraft:husbandry/complete_catalogue", britishRenamer::get));
        MCTypeRegistry.POI_CHUNK.addStructureConverter(new ConverterPoiDelete(VERSION, poiRemove::contains));
    }
}
