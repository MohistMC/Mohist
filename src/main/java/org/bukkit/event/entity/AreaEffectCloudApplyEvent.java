package org.bukkit.event.entity;

import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;

import java.util.List;

/**
 * Called when a lingering potion applies it's effects. Happens
 * once every 5 ticks
 */
public class AreaEffectCloudApplyEvent extends EntityEvent {
    private static final HandlerList handlers = new HandlerList();
    private final List<LivingEntity> affectedEntities;

    public AreaEffectCloudApplyEvent(final AreaEffectCloud entity, final List<LivingEntity> affectedEntities) {
        super(entity);
        this.affectedEntities = affectedEntities;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public AreaEffectCloud getEntity() {
        return (AreaEffectCloud) entity;
    }

    /**
     * Retrieves a mutable list of the effected entities
     * <p>
     * It is important to note that not every entity in this list
     * is guaranteed to be effected.  The cloud may die during the
     * application of its effects due to the depletion of {@link AreaEffectCloud#getDurationOnUse()}
     * or {@link AreaEffectCloud#getRadiusOnUse()}
     *
     * @return the affected entity list
     */
    public List<LivingEntity> getAffectedEntities() {
        return affectedEntities;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
