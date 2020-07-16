package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.util.Identifier;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;

public class CraftCreatureSpawner extends CraftBlockEntityState<MobSpawnerBlockEntity> implements CreatureSpawner {

    public CraftCreatureSpawner(final Block block) {
        super(block, MobSpawnerBlockEntity.class);
    }

    public CraftCreatureSpawner(final Material material, MobSpawnerBlockEntity te) {
        super(material, te);
    }

    @Override
    public EntityType getSpawnedType() {
        Identifier key = this.getSnapshot().getLogic().getEntityId();
        return (key == null) ? EntityType.PIG : EntityType.fromName(key.getPath());
    }

    @Override
    public void setSpawnedType(EntityType entityType) {
        if (entityType == null || entityType.getName() == null) {
            throw new IllegalArgumentException("Can't spawn EntityType " + entityType + " from mobspawners!");
        }

        this.getSnapshot().getLogic().setEntityId(net.minecraft.entity.EntityType.get(entityType.getName()).get());
    }

    @Override
    public String getCreatureTypeName() {
        return this.getSnapshot().getLogic().getEntityId().getPath();
    }

    @Override
    public void setCreatureTypeByName(String creatureType) {
        // Verify input
        EntityType type = EntityType.fromName(creatureType);
        if (type == null) {
            return;
        }
        setSpawnedType(type);
    }

    @Override
    public int getDelay() {
        return this.getSnapshot().getLogic().spawnDelay;
    }

    @Override
    public void setDelay(int delay) {
        this.getSnapshot().getLogic().spawnDelay = delay;
    }

    @Override
    public int getMinSpawnDelay() {
        return this.getSnapshot().getLogic().minSpawnDelay;
    }

    @Override
    public void setMinSpawnDelay(int spawnDelay) {
        Preconditions.checkArgument(spawnDelay <= getMaxSpawnDelay(), "Minimum Spawn Delay must be less than or equal to Maximum Spawn Delay");
        this.getSnapshot().getLogic().minSpawnDelay = spawnDelay;
    }

    @Override
    public int getMaxSpawnDelay() {
        return this.getSnapshot().getLogic().maxSpawnDelay;
    }

    @Override
    public void setMaxSpawnDelay(int spawnDelay) {
        Preconditions.checkArgument(spawnDelay > 0, "Maximum Spawn Delay must be greater than 0.");
        Preconditions.checkArgument(spawnDelay >= getMinSpawnDelay(), "Maximum Spawn Delay must be greater than or equal to Minimum Spawn Delay");
        this.getSnapshot().getLogic().maxSpawnDelay = spawnDelay;
    }

    @Override
    public int getMaxNearbyEntities() {
        return this.getSnapshot().getLogic().maxNearbyEntities;
    }

    @Override
    public void setMaxNearbyEntities(int maxNearbyEntities) {
        this.getSnapshot().getLogic().maxNearbyEntities = maxNearbyEntities;
    }

    @Override
    public int getSpawnCount() {
        return this.getSnapshot().getLogic().spawnCount;
    }

    @Override
    public void setSpawnCount(int count) {
        this.getSnapshot().getLogic().spawnCount = count;
    }

    @Override
    public int getRequiredPlayerRange() {
        return this.getSnapshot().getLogic().requiredPlayerRange;
    }

    @Override
    public void setRequiredPlayerRange(int requiredPlayerRange) {
        this.getSnapshot().getLogic().requiredPlayerRange = requiredPlayerRange;
    }

    @Override
    public int getSpawnRange() {
        return this.getSnapshot().getLogic().spawnRange;
    }

    @Override
    public void setSpawnRange(int spawnRange) {
        this.getSnapshot().getLogic().spawnRange = spawnRange;
    }
}
