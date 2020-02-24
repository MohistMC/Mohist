package red.mohist.forge;

import java.util.Arrays;
import java.util.List;

/**
 * Why is there such a class?
 * Because we have included some MOD optimizations and modifications,
 * as well as some mods that are only used on the client, these cannot be loaded in Mohist
 */
public class AutoDeleteMods {
    public static final List<String> classlist;

    static {
        classlist = Arrays.asList("com.tmtravlr.jaff.entities.EntityFish" /*JustAFewFish*/,
                "org.spongepowered.mod.SpongeMod" /*SpongeForge*/,
                "org.dimdev.vanillafix.VanillaFix" /*VanillaFix*/,
                "lumien.custommainmenu.CustomMainMenu" /*CustomMainMenu*/,
                "optifine.Differ" /*OptiFine*/);
    }
}
