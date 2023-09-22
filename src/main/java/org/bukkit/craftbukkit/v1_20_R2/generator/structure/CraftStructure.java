package org.bukkit.craftbukkit.v1_20_R2.generator.structure;

import com.google.common.base.Preconditions;
import net.minecraft.core.registries.Registries;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.v1_20_R2.CraftRegistry;
import org.bukkit.craftbukkit.v1_20_R2.util.CraftNamespacedKey;
import org.bukkit.generator.structure.Structure;
import org.bukkit.generator.structure.StructureType;

public class CraftStructure extends Structure {

    public static Structure minecraftToBukkit(net.minecraft.world.level.levelgen.structure.Structure minecraft, net.minecraft.core.RegistryAccess registryHolder) {
        Preconditions.checkArgument(minecraft != null);

        net.minecraft.core.Registry<net.minecraft.world.level.levelgen.structure.Structure> registry = CraftRegistry.getMinecraftRegistry(Registries.STRUCTURE);
        Structure bukkit = Registry.STRUCTURE.get(CraftNamespacedKey.fromMinecraft(registry.getResourceKey(minecraft).orElseThrow().location()));

        Preconditions.checkArgument(bukkit != null);

        return bukkit;
    }

    public static net.minecraft.world.level.levelgen.structure.Structure bukkitToMinecraft(Structure bukkit) {
        Preconditions.checkArgument(bukkit != null);
        return ((CraftStructure) bukkit).getHandle();
    }

    private final NamespacedKey key;
    private final net.minecraft.world.level.levelgen.structure.Structure structure;
    private final StructureType structureType;

    public CraftStructure(NamespacedKey key, net.minecraft.world.level.levelgen.structure.Structure structure) {
        this.key = key;
        this.structure = structure;
        this.structureType = CraftStructureType.minecraftToBukkit(structure.type());
    }

    public net.minecraft.world.level.levelgen.structure.Structure getHandle() {
        return structure;
    }

    @Override
    public StructureType getStructureType() {
        return structureType;
    }

    @Override
    public NamespacedKey getKey() {
        return key;
    }
}
