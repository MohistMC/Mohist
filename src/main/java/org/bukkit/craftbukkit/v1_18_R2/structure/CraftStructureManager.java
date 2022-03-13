package org.bukkit.craftbukkit.v1_18_R2.structure;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.apache.commons.lang3.Validate;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_18_R2.util.CraftNamespacedKey;
import org.bukkit.structure.Structure;
import org.bukkit.structure.StructureManager;

public class CraftStructureManager implements StructureManager {

    private final net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager structureManager;

    public CraftStructureManager(net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager structureManager) {
        this.structureManager = structureManager;
    }

    @Override
    public Map<NamespacedKey, Structure> getStructures() {
        Map<NamespacedKey, Structure> cachedStructures = new HashMap<>();
        for (Map.Entry<ResourceLocation, Optional<StructureTemplate>> entry : structureManager.structureRepository.entrySet()) {
            entry.getValue().ifPresent(definedStructure -> {
                cachedStructures.put(CraftNamespacedKey.fromMinecraft(entry.getKey()), new CraftStructure(definedStructure));
            });
        }
        return Collections.unmodifiableMap(cachedStructures);
    }

    @Override
    public Structure getStructure(NamespacedKey structureKey) {
        Validate.notNull(structureKey, "structureKey cannot be null");

        final Optional<StructureTemplate> definedStructure = structureManager.structureRepository.get(CraftNamespacedKey.toMinecraft(structureKey));
        if (definedStructure == null) {
            return null;
        }
        return definedStructure.map(CraftStructure::new).orElse(null);
    }

    @Override
    public Structure loadStructure(NamespacedKey structureKey, boolean register) {
        ResourceLocation minecraftKey = createAndValidateMinecraftStructureKey(structureKey);

        Optional<StructureTemplate> structure = structureManager.structureRepository.get(minecraftKey);
        structure = structure == null ? Optional.empty() : structure;
        structure = structure.isPresent() ? structure : structureManager.loadFromGenerated(minecraftKey);
        structure = structure.isPresent() ? structure : structureManager.loadFromResource(minecraftKey);

        if (register) {
            structureManager.structureRepository.put(minecraftKey, structure);
        }

        return structure.map(CraftStructure::new).orElse(null);
    }

    @Override
    public Structure loadStructure(NamespacedKey structureKey) {
        return loadStructure(structureKey, true);
    }

    @Override
    public void saveStructure(NamespacedKey structureKey) {
        ResourceLocation minecraftKey = createAndValidateMinecraftStructureKey(structureKey);

        structureManager.save(minecraftKey);
    }

    @Override
    public void saveStructure(NamespacedKey structureKey, Structure structure) throws IOException {
        Validate.notNull(structure, "structure cannot be null");

        File structureFile = getStructureFile(structureKey);
        Files.createDirectories(structureFile.toPath().getParent());
        saveStructure(structureFile, structure);
    }

    @Override
    public Structure registerStructure(NamespacedKey structureKey, Structure structure) {
        Validate.notNull(structure, "structure cannot be null");
        ResourceLocation minecraftKey = createAndValidateMinecraftStructureKey(structureKey);

        final Optional<StructureTemplate> optionalDefinedStructure = Optional.of(((CraftStructure) structure).getHandle());
        final Optional<StructureTemplate> previousStructure = structureManager.structureRepository.put(minecraftKey, optionalDefinedStructure);
        return previousStructure == null ? null : previousStructure.map(CraftStructure::new).orElse(null);
    }

    @Override
    public Structure unregisterStructure(NamespacedKey structureKey) {
        ResourceLocation minecraftKey = createAndValidateMinecraftStructureKey(structureKey);

        final Optional<StructureTemplate> previousStructure = structureManager.structureRepository.remove(minecraftKey);
        return previousStructure == null ? null : previousStructure.map(CraftStructure::new).orElse(null);
    }

    @Override
    public void deleteStructure(NamespacedKey structureKey) throws IOException {
        deleteStructure(structureKey, true);
    }

    @Override
    public void deleteStructure(NamespacedKey structureKey, boolean unregister) throws IOException {
        ResourceLocation key = CraftNamespacedKey.toMinecraft(structureKey);

        if (unregister) {
            structureManager.structureRepository.remove(key);
        }
        Path path = structureManager.createAndValidatePathToStructure(key, ".nbt");
        Files.deleteIfExists(path);
    }

    @Override
    public File getStructureFile(NamespacedKey structureKey) {
        ResourceLocation minecraftKey = createAndValidateMinecraftStructureKey(structureKey);
        return structureManager.createAndValidatePathToStructure(minecraftKey, ".nbt").toFile();
    }

    @Override
    public Structure loadStructure(File file) throws IOException {
        Validate.notNull(file, "file cannot be null");

        FileInputStream fileinputstream = new FileInputStream(file);
        return loadStructure(fileinputstream);
    }

    @Override
    public Structure loadStructure(InputStream inputStream) throws IOException {
        Validate.notNull(inputStream, "inputStream cannot be null");

        return new CraftStructure(structureManager.readStructure(inputStream));
    }

    @Override
    public void saveStructure(File file, Structure structure) throws IOException {
        Validate.notNull(file, "file cannot be null");
        Validate.notNull(structure, "structure cannot be null");

        FileOutputStream fileoutputstream = new FileOutputStream(file);
        saveStructure(fileoutputstream, structure);
    }

    @Override
    public void saveStructure(OutputStream outputStream, Structure structure) throws IOException {
        Validate.notNull(outputStream, "outputStream cannot be null");
        Validate.notNull(structure, "structure cannot be null");

        CompoundTag nbttagcompound = ((CraftStructure) structure).getHandle().save(new CompoundTag());
        NbtIo.writeCompressed(nbttagcompound, outputStream);
    }

    @Override
    public Structure createStructure() {
        return new CraftStructure(new StructureTemplate());
    }

    private ResourceLocation createAndValidateMinecraftStructureKey(NamespacedKey structureKey) {
        Validate.notNull(structureKey, "structureKey cannot be null");

        ResourceLocation minecraftkey = CraftNamespacedKey.toMinecraft(structureKey);
        if (minecraftkey.getPath().contains("//")) {
            throw new IllegalArgumentException("Resource key for Structures can not contain \"//\"");
        }
        return minecraftkey;
    }

    @Override
    public Structure copy(Structure structure) {
        return new CraftStructure(structureManager.readStructure(((CraftStructure) structure).getHandle().save(new CompoundTag())));
    }
}
