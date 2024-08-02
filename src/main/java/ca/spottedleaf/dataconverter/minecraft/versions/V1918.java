package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.Types;

public final class V1918 {

    protected static final int VERSION = MCVersions.V18W49A + 2;

    private V1918() {}

    private static String getProfessionString(final int professionId, final int careerId) {
        if (professionId == 0) {
            if (careerId == 2) {
                return "minecraft:fisherman";
            } else if (careerId == 3) {
                return "minecraft:shepherd";
            } else {
                return careerId == 4 ? "minecraft:fletcher" : "minecraft:farmer";
            }
        } else if (professionId == 1) {
            return careerId == 2 ? "minecraft:cartographer" : "minecraft:librarian";
        } else if (professionId == 2) {
            return "minecraft:cleric";
        } else if (professionId == 3) {
            if (careerId == 2) {
                return "minecraft:weaponsmith";
            } else {
                return careerId == 3 ? "minecraft:toolsmith" : "minecraft:armorer";
            }
        } else if (professionId == 4) {
            return careerId == 2 ? "minecraft:leatherworker" : "minecraft:butcher";
        } else {
            return professionId == 5 ? "minecraft:nitwit" : "minecraft:none";
        }
    }

    public static void register() {
        final DataConverter<MapType<String>, MapType<String>> converter = new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final int profession = data.getInt("Profession");
                final int career = data.getInt("Career");
                final int careerLevel = data.getInt("CareerLevel", 1);
                data.remove("Profession");
                data.remove("Career");
                data.remove("CareerLevel");

                final MapType<String> villagerData = Types.NBT.createEmptyMap();
                data.setMap("VillagerData", villagerData);
                villagerData.setString("type", "minecraft:plains");
                villagerData.setString("profession", getProfessionString(profession, career));
                villagerData.setInt("level", careerLevel);

                return null;
            }
        };

        MCTypeRegistry.ENTITY.addConverterForId("minecraft:villager", converter);
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:zombie_villager", converter);
    }
}
