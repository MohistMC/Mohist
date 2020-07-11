package org.bukkit.block;

import org.bukkit.Nameable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.BeaconInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;

/**
 * Represents a captured state of a beacon.
 */
public interface Beacon extends Container, Nameable {

    @Override
    BeaconInventory getInventory();

    @Override
    BeaconInventory getSnapshotInventory();

    /**
     * Returns the list of players within the beacon's range of effect.
     * <p>
     * This will return an empty list if the block represented by this state is
     * no longer a beacon.
     *
     * @return the players in range
     * @throws IllegalStateException if this block state is not placed
     */
    Collection<LivingEntity> getEntitiesInRange();

    /**
     * Returns the tier of the beacon pyramid (0-4). The tier refers to the
     * beacon's power level, based on how many layers of blocks are in the
     * pyramid. Tier 1 refers to a beacon with one layer of 9 blocks under it.
     *
     * @return the beacon tier
     */
    int getTier();

    /**
     * Returns the primary effect set on the beacon
     *
     * @return the primary effect or null if not set
     */
    PotionEffect getPrimaryEffect();

    /**
     * Set the primary effect on this beacon, or null to clear.
     *
     * @param effect new primary effect
     */
    void setPrimaryEffect(PotionEffectType effect);

    /**
     * Returns the secondary effect set on the beacon.
     *
     * @return the secondary effect or null if no secondary effect
     */
    PotionEffect getSecondaryEffect();

    /**
     * Set the secondary effect on this beacon, or null to clear. Note that tier
     * must be &gt;= 4 for this effect to be active.
     *
     * @param effect desired secondary effect
     */
    void setSecondaryEffect(PotionEffectType effect);
}
