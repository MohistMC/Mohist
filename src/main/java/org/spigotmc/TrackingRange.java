package org.spigotmc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.item.PaintingEntity;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.server.ServerWorld;

public class TrackingRange
{

    /**
     * Gets the range an entity should be 'tracked' by players and visible in
     * the client.
     *
     * @param entity
     * @param defaultRange Default range defined by Mojang
     * @return
     */
    public static int getEntityTrackingRange(Entity entity, int defaultRange)
    {
        SpigotWorldConfig config = entity.level.spigotConfig;
        if ( entity instanceof PlayerEntity )
        {
            return config.playerTrackingRange;
        }
        switch (entity.activationType) {
            case RAIDER:
            case MONSTER:
            case FLYING_MONSTER:
                return config.monsterTrackingRange;
            case WATER:
            case VILLAGER:
            case ANIMAL:
                return config.animalTrackingRange;
            case MISC:
        }
        if ( entity instanceof ItemFrameEntity || entity instanceof PaintingEntity || entity instanceof ItemEntity || entity instanceof ExperienceOrbEntity )
        // Paper end
        {
            return config.miscTrackingRange;
        } else
        {
            if (entity instanceof EnderDragonEntity) return ((ServerWorld)(entity.getEntity().level)).getChunkSource().chunkMap.getLoadViewDistance(); // Paper - enderdragon is exempt
            return config.otherTrackingRange;
        }
    }
}
