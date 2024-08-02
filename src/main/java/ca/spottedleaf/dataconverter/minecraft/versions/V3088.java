package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.converters.chunk.ConverterAddBlendingData;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;

public final class V3088 {

    // this class originally targeted 3079 but was changed to target a later version without changing the converter, zero clue why
    // this class then targeted 3088 but was changed to target 3441
    // to maintain integrity of the data version, I chose to extract the converter to a separate class and use it in both versions
    // the reason it is important to never change old converters once released is that it creates _two_ versions under the same id.
    // Consider the case where a user force upgrades their world, but does not load the chunk. Then, consider the case where
    // the user does not force upgrade their world. Then, Mojang comes along and makes a decision like this and now both
    // players load the chunk - they went through a different conversion process, which ultimately creates two versions.
    // Unfortunately this fix doesn't exactly resolve it, as anyone running Mojang's converters will now be different
    // from DataConverter's. It's broadly a dumb situation all around that could be avoided if Mojang wasn't being careless here.
    protected static final int VERSION = MCVersions.V22W14A;

    public static void register() {
        MCTypeRegistry.CHUNK.addStructureConverter(new ConverterAddBlendingData(VERSION));
    }
}
