package red.mohist.forge.registry;

import com.google.common.collect.ImmutableSet;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import java.util.Set;

@SuppressWarnings({"unchecked", "ConstantConditions"})
public class BukkitRegistry {

    private static Set<IForgeRegistry<?>> registries() {
        return ImmutableSet.of(ForgeRegistries.BLOCKS, ForgeRegistries.ITEMS,
                ForgeRegistries.POTION_TYPES, ForgeRegistries.POTIONS,
                ForgeRegistries.ENTITIES, ForgeRegistries.TILE_ENTITIES,
                ForgeRegistries.BIOMES);
    }

    public static void unlockRegistries() {
        for (IForgeRegistry<?> registry : registries()) {
            if (registry instanceof ForgeRegistry) {
                ((ForgeRegistry<?>) registry).unfreeze();
            }
        }
    }

    public static void lockRegistries() {
        for (IForgeRegistry<?> registry : registries()) {
            if (registry instanceof ForgeRegistry) {
                ((ForgeRegistry<?>) registry).freeze();
            }
        }
    }
}

