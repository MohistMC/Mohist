package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.converters.entity.ConverterAbstractEntityRename;

public final class V143 {

    protected static final int VERSION = MCVersions.V15W44B;

    public static void register() {
        ConverterAbstractEntityRename.register(VERSION, (final String input) -> {
            return "TippedArrow".equals(input) ? "Arrow" : null;
        });
    }

    private V143() {}

}
