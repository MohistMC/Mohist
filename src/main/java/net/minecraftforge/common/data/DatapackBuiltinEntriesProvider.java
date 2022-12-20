package net.minecraftforge.common.data;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.RegistriesDatapackGenerator;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import net.minecraftforge.registries.DataPackRegistriesHooks;

public class DatapackBuiltinEntriesProvider extends RegistriesDatapackGenerator
{
    /**
     * Constructs a new datapack provider which generates all registry objects
     * from the provided mods using the holder.
     *
     * @param output the target directory of the data generator
     * @param registries a future of a lookup for registries and their objects
     * @param modIds a set of mod ids to generate the dynamic registry objects of
     */
    public DatapackBuiltinEntriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, Set<String> modIds)
    {
        super(output, registries, modIds);
    }

    /**
     * Constructs a new datapack provider which generates all registry objects
     * from the provided mods using the holder. All entries that need to be
     * bootstrapped are provided within the {@link RegistrySetBuilder}.
     *
     * @param output the target directory of the data generator
     * @param registries a future of a lookup for registries and their objects
     * @param datapackEntriesBuilder a builder containing the dynamic registry objects added by this provider
     * @param modIds a set of mod ids to generate the dynamic registry objects of
     */
    public DatapackBuiltinEntriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, RegistrySetBuilder datapackEntriesBuilder, Set<String> modIds)
    {
        this(output, registries.thenApply(r -> constructRegistries(r, datapackEntriesBuilder)), modIds);
    }

    /**
     * A method used to construct empty bootstraps for all registries this provider
     * did not touch such that existing dynamic registry objects do not get inlined.
     *
     * @param original a future of a lookup for registries and their objects
     * @param datapackEntriesBuilder a builder containing the dynamic registry objects added by this provider
     * @return a new lookup containing the existing and to be generated registries and their objects
     */
    private static HolderLookup.Provider constructRegistries(HolderLookup.Provider original, RegistrySetBuilder datapackEntriesBuilder)
    {
        var builderKeys = new HashSet<>(datapackEntriesBuilder.getEntryKeys());
        DataPackRegistriesHooks.getDataPackRegistries().stream().filter(data -> !builderKeys.contains(data.key())).forEach(data -> datapackEntriesBuilder.add(data.key(), context -> {}));
        return datapackEntriesBuilder.buildPatch(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), original);
    }
}
