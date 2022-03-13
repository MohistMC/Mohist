package org.bukkit.craftbukkit.v1_18_R2.structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockRotProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.RegionAccessor;
import org.bukkit.World;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.craftbukkit.v1_18_R2.CraftRegionAccessor;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.structure.Palette;
import org.bukkit.structure.Structure;
import org.bukkit.util.BlockVector;

public class CraftStructure implements Structure {

    private final StructureTemplate structure;

    public CraftStructure(StructureTemplate structure) {
        this.structure = structure;
    }

    @Override
    public void place(Location location, boolean includeEntities, StructureRotation structureRotation, Mirror mirror, int palette, float integrity, Random random) {
        location.checkFinite();
        World world = location.getWorld();
        Validate.notNull(world, "location#getWorld() cannot be null");

        BlockVector blockVector = new BlockVector(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        place(world, blockVector, includeEntities, structureRotation, mirror, palette, integrity, random);
    }

    @Override
    public void place(RegionAccessor regionAccessor, BlockVector location, boolean includeEntities, StructureRotation structureRotation, Mirror mirror, int palette, float integrity, Random random) {
        Validate.notNull(regionAccessor, "regionAccessor can not be null");
        location.checkFinite();

        if (integrity < 0F || integrity > 1F) {
            throw new IllegalArgumentException("Integrity must be between 0 and 1 inclusive. Was \"" + integrity + "\"");
        }

        StructurePlaceSettings definedstructureinfo = new StructurePlaceSettings()
                .setMirror(net.minecraft.world.level.block.Mirror.valueOf(mirror.name()))
                .setRotation(net.minecraft.world.level.block.Rotation.valueOf(structureRotation.name()))
                .setIgnoreEntities(!includeEntities)
                .addProcessor(new BlockRotProcessor(integrity))
                .setRandom(random);
        definedstructureinfo.palette = palette;

        BlockPos blockPosition = new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        structure.placeInWorld(((CraftRegionAccessor) regionAccessor).getHandle(), blockPosition, blockPosition, definedstructureinfo, random, 2);
    }

    @Override
    public void fill(Location corner1, Location corner2, boolean includeEntities) {
        Validate.notNull(corner1, "corner1 cannot be null");
        Validate.notNull(corner2, "corner2 cannot be null");
        World world = corner1.getWorld();
        Validate.notNull(world, "corner1#getWorld() cannot be null");

        Location origin = new Location(world, Math.min(corner1.getBlockX(), corner2.getBlockX()), Math.min(corner1.getBlockY(), corner2.getBlockY()), Math.min(corner1.getBlockZ(), corner2.getBlockZ()));
        BlockVector size = new BlockVector(Math.abs(corner1.getBlockX() - corner2.getBlockX()), Math.abs(corner1.getBlockY() - corner2.getBlockY()), Math.abs(corner1.getBlockZ() - corner2.getBlockZ()));
        fill(origin, size, includeEntities);
    }

    @Override
    public void fill(Location origin, BlockVector size, boolean includeEntities) {
        Validate.notNull(origin, "origin cannot be null");
        World world = origin.getWorld();
        Validate.notNull(world, "origin#getWorld() cannot be null");
        Validate.notNull(size, "size cannot be null");
        if (size.getBlockX() < 1 || size.getBlockY() < 1 || size.getBlockZ() < 1) {
            throw new IllegalArgumentException("Size must be at least 1x1x1 but was " + size.getBlockX() + "x" + size.getBlockY() + "x" + size.getBlockZ());
        }

        structure.fillFromWorld(((CraftWorld) world).getHandle(), new BlockPos(origin.getBlockX(), origin.getBlockY(), origin.getBlockZ()), new BlockPos(size.getBlockX(), size.getBlockY(), size.getBlockZ()), includeEntities, Blocks.STRUCTURE_VOID);
    }

    @Override
    public BlockVector getSize() {
        return new BlockVector(structure.getSize().getX(), structure.getSize().getY(), structure.getSize().getZ());
    }

    @Override
    public List<Entity> getEntities() {
        List<Entity> entities = new ArrayList<>();
        for (StructureTemplate.StructureEntityInfo entity : structure.entityInfoList) {
            net.minecraft.world.entity.EntityType.create(entity.nbt, ((CraftWorld) Bukkit.getServer().getWorlds().get(0)).getHandle()).ifPresent(dummyEntity -> {
                dummyEntity.setPos(entity.pos.x, entity.pos.y, entity.pos.z);
                entities.add(dummyEntity.getBukkitEntity());
            });
        }
        return Collections.unmodifiableList(entities);
    }

    @Override
    public int getEntityCount() {
        return structure.entityInfoList.size();
    }

    @Override
    public List<Palette> getPalettes() {
        return structure.palettes.stream().map(CraftPalette::new).collect(Collectors.toList());
    }

    @Override
    public int getPaletteCount() {
        return structure.palettes.size();
    }

    @Override
    public PersistentDataContainer getPersistentDataContainer() {
        return getHandle().persistentDataContainer;
    }

    public StructureTemplate getHandle() {
        return structure;
    }
}
