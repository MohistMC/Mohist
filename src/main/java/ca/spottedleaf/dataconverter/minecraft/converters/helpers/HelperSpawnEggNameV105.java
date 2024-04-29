package ca.spottedleaf.dataconverter.minecraft.converters.helpers;

public final class HelperSpawnEggNameV105 {

    private static final String[] ID_TO_STRING = new String[256];
    static {
        ID_TO_STRING[1] = "Item";
        ID_TO_STRING[2] = "XPOrb";
        ID_TO_STRING[7] = "ThrownEgg";
        ID_TO_STRING[8] = "LeashKnot";
        ID_TO_STRING[9] = "Painting";
        ID_TO_STRING[10] = "Arrow";
        ID_TO_STRING[11] = "Snowball";
        ID_TO_STRING[12] = "Fireball";
        ID_TO_STRING[13] = "SmallFireball";
        ID_TO_STRING[14] = "ThrownEnderpearl";
        ID_TO_STRING[15] = "EyeOfEnderSignal";
        ID_TO_STRING[16] = "ThrownPotion";
        ID_TO_STRING[17] = "ThrownExpBottle";
        ID_TO_STRING[18] = "ItemFrame";
        ID_TO_STRING[19] = "WitherSkull";
        ID_TO_STRING[20] = "PrimedTnt";
        ID_TO_STRING[21] = "FallingSand";
        ID_TO_STRING[22] = "FireworksRocketEntity";
        ID_TO_STRING[23] = "TippedArrow";
        ID_TO_STRING[24] = "SpectralArrow";
        ID_TO_STRING[25] = "ShulkerBullet";
        ID_TO_STRING[26] = "DragonFireball";
        ID_TO_STRING[30] = "ArmorStand";
        ID_TO_STRING[41] = "Boat";
        ID_TO_STRING[42] = "MinecartRideable";
        ID_TO_STRING[43] = "MinecartChest";
        ID_TO_STRING[44] = "MinecartFurnace";
        ID_TO_STRING[45] = "MinecartTNT";
        ID_TO_STRING[46] = "MinecartHopper";
        ID_TO_STRING[47] = "MinecartSpawner";
        ID_TO_STRING[40] = "MinecartCommandBlock";
        ID_TO_STRING[48] = "Mob";
        ID_TO_STRING[49] = "Monster";
        ID_TO_STRING[50] = "Creeper";
        ID_TO_STRING[51] = "Skeleton";
        ID_TO_STRING[52] = "Spider";
        ID_TO_STRING[53] = "Giant";
        ID_TO_STRING[54] = "Zombie";
        ID_TO_STRING[55] = "Slime";
        ID_TO_STRING[56] = "Ghast";
        ID_TO_STRING[57] = "PigZombie";
        ID_TO_STRING[58] = "Enderman";
        ID_TO_STRING[59] = "CaveSpider";
        ID_TO_STRING[60] = "Silverfish";
        ID_TO_STRING[61] = "Blaze";
        ID_TO_STRING[62] = "LavaSlime";
        ID_TO_STRING[63] = "EnderDragon";
        ID_TO_STRING[64] = "WitherBoss";
        ID_TO_STRING[65] = "Bat";
        ID_TO_STRING[66] = "Witch";
        ID_TO_STRING[67] = "Endermite";
        ID_TO_STRING[68] = "Guardian";
        ID_TO_STRING[69] = "Shulker";
        ID_TO_STRING[90] = "Pig";
        ID_TO_STRING[91] = "Sheep";
        ID_TO_STRING[92] = "Cow";
        ID_TO_STRING[93] = "Chicken";
        ID_TO_STRING[94] = "Squid";
        ID_TO_STRING[95] = "Wolf";
        ID_TO_STRING[96] = "MushroomCow";
        ID_TO_STRING[97] = "SnowMan";
        ID_TO_STRING[98] = "Ozelot";
        ID_TO_STRING[99] = "VillagerGolem";
        ID_TO_STRING[100] = "EntityHorse";
        ID_TO_STRING[101] = "Rabbit";
        ID_TO_STRING[120] = "Villager";
        ID_TO_STRING[200] = "EnderCrystal";
    }

    public static String getSpawnNameFromId(final short id) {
        return ID_TO_STRING[id & 255];
    }
}
