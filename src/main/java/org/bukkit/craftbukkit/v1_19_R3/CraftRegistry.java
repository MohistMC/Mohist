package org.bukkit.craftbukkit.v1_19_R3;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.v1_19_R3.generator.strucutre.CraftStructure;
import org.bukkit.craftbukkit.v1_19_R3.generator.strucutre.CraftStructureType;
import org.bukkit.craftbukkit.v1_19_R3.util.CraftNamespacedKey;
import org.bukkit.craftbukkit.v1_19_R3.inventory.trim.CraftTrimMaterial;
import org.bukkit.craftbukkit.v1_19_R3.inventory.trim.CraftTrimPattern;
import org.bukkit.generator.structure.Structure;
import org.bukkit.generator.structure.StructureType;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;

public class CraftRegistry<B extends Keyed, M> implements Registry<B> {

    public static <B extends Keyed> Registry<?> createRegistry(Class<B> bukkitClass, RegistryAccess registryHolder) {
        if (bukkitClass == Structure.class) {
            return new CraftRegistry<>(registryHolder.registryOrThrow(Registries.STRUCTURE), CraftStructure::new);
        }
        if (bukkitClass == StructureType.class) {
            return new CraftRegistry<>(BuiltInRegistries.STRUCTURE_TYPE, CraftStructureType::new);
        }
        if (bukkitClass == TrimMaterial.class) {
            return new CraftRegistry<>(registryHolder.registryOrThrow(Registries.TRIM_MATERIAL), CraftTrimMaterial::new);
        }
        if (bukkitClass == TrimPattern.class) {
            return new CraftRegistry<>(registryHolder.registryOrThrow(Registries.TRIM_PATTERN), CraftTrimPattern::new);
        }

        return null;
    }

    private final Map<NamespacedKey, B> cache = new HashMap<>();
    private final net.minecraft.core.Registry<M> minecraftRegistry;
    private final BiFunction<NamespacedKey, M, B> minecraftToBukkit;

    public CraftRegistry(net.minecraft.core.Registry<M> minecraftRegistry, BiFunction<NamespacedKey, M, B> minecraftToBukkit) {
        this.minecraftRegistry = minecraftRegistry;
        this.minecraftToBukkit = minecraftToBukkit;
    }

    @Override
    public B get(NamespacedKey namespacedKey) {
        B cached = cache.get(namespacedKey);
        if (cached != null) {
            return cached;
        }

        B bukkit = createBukkit(namespacedKey, minecraftRegistry.getOptional(CraftNamespacedKey.toMinecraft(namespacedKey)).orElse(null));
        if (bukkit == null) {
            return null;
        }

        cache.put(namespacedKey, bukkit);

        return bukkit;
    }

    @Override
    public Iterator<B> iterator() {
        return values().iterator();
    }

    public B createBukkit(NamespacedKey namespacedKey, M minecraft) {
        if (minecraft == null) {
            return null;
        }

        return minecraftToBukkit.apply(namespacedKey, minecraft);
    }

    public Stream<B> values() {
        return minecraftRegistry.keySet().stream().map(minecraftKey -> get(CraftNamespacedKey.fromMinecraft(minecraftKey)));
    }
}
