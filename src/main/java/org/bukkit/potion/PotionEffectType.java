package org.bukkit.potion;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.Validate;
import org.bukkit.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a type of potion and its effect on an entity.
 */
public abstract class PotionEffectType {
    /**
     * Increases movement speed.
     */
    public static final PotionEffectType SPEED = new PotionEffectTypeWrapper(1);

    /**
     * Decreases movement speed.
     */
    public static final PotionEffectType SLOW = new PotionEffectTypeWrapper(2);

    /**
     * Increases dig speed.
     */
    public static final PotionEffectType FAST_DIGGING = new PotionEffectTypeWrapper(3);

    /**
     * Decreases dig speed.
     */
    public static final PotionEffectType SLOW_DIGGING = new PotionEffectTypeWrapper(4);

    /**
     * Increases damage dealt.
     */
    public static final PotionEffectType INCREASE_DAMAGE = new PotionEffectTypeWrapper(5);

    /**
     * Heals an entity.
     */
    public static final PotionEffectType HEAL = new PotionEffectTypeWrapper(6);

    /**
     * Hurts an entity.
     */
    public static final PotionEffectType HARM = new PotionEffectTypeWrapper(7);

    /**
     * Increases jump height.
     */
    public static final PotionEffectType JUMP = new PotionEffectTypeWrapper(8);

    /**
     * Warps vision on the client.
     */
    public static final PotionEffectType CONFUSION = new PotionEffectTypeWrapper(9);

    /**
     * Regenerates health.
     */
    public static final PotionEffectType REGENERATION = new PotionEffectTypeWrapper(10);

    /**
     * Decreases damage dealt to an entity.
     */
    public static final PotionEffectType DAMAGE_RESISTANCE = new PotionEffectTypeWrapper(11);

    /**
     * Stops fire damage.
     */
    public static final PotionEffectType FIRE_RESISTANCE = new PotionEffectTypeWrapper(12);

    /**
     * Allows breathing underwater.
     */
    public static final PotionEffectType WATER_BREATHING = new PotionEffectTypeWrapper(13);

    /**
     * Grants invisibility.
     */
    public static final PotionEffectType INVISIBILITY = new PotionEffectTypeWrapper(14);

    /**
     * Blinds an entity.
     */
    public static final PotionEffectType BLINDNESS = new PotionEffectTypeWrapper(15);

    /**
     * Allows an entity to see in the dark.
     */
    public static final PotionEffectType NIGHT_VISION = new PotionEffectTypeWrapper(16);

    /**
     * Increases hunger.
     */
    public static final PotionEffectType HUNGER = new PotionEffectTypeWrapper(17);

    /**
     * Decreases damage dealt by an entity.
     */
    public static final PotionEffectType WEAKNESS = new PotionEffectTypeWrapper(18);

    /**
     * Deals damage to an entity over time.
     */
    public static final PotionEffectType POISON = new PotionEffectTypeWrapper(19);

    /**
     * Deals damage to an entity over time and gives the health to the
     * shooter.
     */
    public static final PotionEffectType WITHER = new PotionEffectTypeWrapper(20);

    /**
     * Increases the maximum health of an entity.
     */
    public static final PotionEffectType HEALTH_BOOST = new PotionEffectTypeWrapper(21);

    /**
     * Increases the maximum health of an entity with health that cannot be
     * regenerated, but is refilled every 30 seconds.
     */
    public static final PotionEffectType ABSORPTION = new PotionEffectTypeWrapper(22);

    /**
     * Increases the food level of an entity each tick.
     */
    public static final PotionEffectType SATURATION = new PotionEffectTypeWrapper(23);

    /**
     * Outlines the entity so that it can be seen from afar.
     */
    public static final PotionEffectType GLOWING = new PotionEffectTypeWrapper(24);

    /**
     * Causes the entity to float into the air.
     */
    public static final PotionEffectType LEVITATION = new PotionEffectTypeWrapper(25);

    /**
     * Loot table luck.
     */
    public static final PotionEffectType LUCK = new PotionEffectTypeWrapper(26);

    /**
     * Loot table unluck.
     */
    public static final PotionEffectType UNLUCK = new PotionEffectTypeWrapper(27);

    /**
     * Slows entity fall rate.
     */
    public static final PotionEffectType SLOW_FALLING = new PotionEffectTypeWrapper(28);

    /**
     * Effects granted by a nearby conduit. Includes enhanced underwater abilities.
     */
    public static final PotionEffectType CONDUIT_POWER = new PotionEffectTypeWrapper(29);

    /**
     * Squee'ek uh'k kk'kkkk squeek eee'eek.
     */
    public static final PotionEffectType DOLPHINS_GRACE = new PotionEffectTypeWrapper(30);

    /**
     * oof.
     */
    public static final PotionEffectType BAD_OMEN = new PotionEffectTypeWrapper(31);

    /**
     * \o/.
     */
    public static final PotionEffectType HERO_OF_THE_VILLAGE = new PotionEffectTypeWrapper(32);

    private final int id;

    protected PotionEffectType(int id) {
        this.id = id;
    }

    /**
     * Creates a PotionEffect from this PotionEffectType, applying duration
     * modifiers and checks.
     *
     * @see PotionBrewer#createEffect(PotionEffectType, int, int)
     * @param duration time in ticks
     * @param amplifier the effect's amplifier
     * @return a resulting potion effect
     */
    @NotNull
    public PotionEffect createEffect(int duration, int amplifier) {
        return new PotionEffect(this, isInstant() ? 1 : (int) (duration * getDurationModifier()), amplifier);
    }

    /**
     * Returns the duration modifier applied to effects of this type.
     *
     * @return duration modifier
     * @deprecated unused, always 1.0
     */
    @Deprecated
    public abstract double getDurationModifier();

    /**
     * Returns the unique ID of this type.
     *
     * @return Unique ID
     * @deprecated Magic value
     */
    @Deprecated
    public int getId() {
        return id;
    }

    /**
     * Returns the name of this effect type.
     *
     * @return The name of this effect type
     */
    @NotNull
    public abstract String getName();

    /**
     * Returns whether the effect of this type happens once, immediately.
     *
     * @return whether this type is normally instant
     */
    public abstract boolean isInstant();

    /**
     * Returns the color of this effect type.
     *
     * @return the color
     */
    @NotNull
    public abstract Color getColor();

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof PotionEffectType)) {
            return false;
        }
        final PotionEffectType other = (PotionEffectType) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "PotionEffectType[" + id + ", " + getName() + "]";
    }

    private static final  Map<Integer, PotionEffectType> byId =  new HashMap<Integer, PotionEffectType>(); // Cauldron change underlying storage to map
    private static final Map<String, PotionEffectType> byName = new HashMap<String, PotionEffectType>();
    // will break on updates.
    private static boolean acceptingNew = true;

    /**
     * Gets the effect type specified by the unique id.
     *
     * @param id Unique ID to fetch
     * @return Resulting type, or null if not found.
     * @deprecated Magic value
     */
    @Deprecated
    @Nullable
    public static PotionEffectType getById(int id) {
        if (id >= byId.size() || id < 0) {
            return null;
        }
        return byId.get(id);
    }

    /**
     * Gets the effect type specified by the given name.
     *
     * @param name Name of PotionEffectType to fetch
     * @return Resulting PotionEffectType, or null if not found.
     */
    @Nullable
    public static PotionEffectType getByName(@NotNull String name) {
        Validate.notNull(name, "name cannot be null");
        return byName.get(name.toLowerCase(java.util.Locale.ENGLISH));
    }

    /**
     * Registers an effect type with the given object.
     * <p>
     * Generally not to be used from within a plugin.
     *
     * @param type PotionType to register
     */
    public static void registerPotionEffectType(@NotNull PotionEffectType type) {
        // Cauldron start - allow vanilla to replace potions
        /*
        if (byId[type.id] != null || byName.containsKey(type.getName().toLowerCase(java.util.Locale.ENGLISH))) {
            throw new IllegalArgumentException("Cannot set already-set type");
        } else if (!acceptingNew) {
            throw new IllegalStateException(
                    "No longer accepting new potion effect types (can only be done by the server implementation)");
        }
         */
        byId.put(type.id, type);
        byName.put(type.getName().toLowerCase(java.util.Locale.ENGLISH), type);
        // Cauldron end
    }

    /**
     * Stops accepting any effect type registrations.
     */
    public static void stopAcceptingRegistrations() {
        acceptingNew = false;
    }

    /**
     * Returns an array of all the registered {@link PotionEffectType}s.
     * This array is not necessarily in any particular order.
     *
     * @return Array of types.
     */
    @NotNull
    public static PotionEffectType[] values() {
        // Cauldron start
        int maxId = 0;
        for(int id : byId.keySet()) {
            maxId = Math.max(maxId, id);
        }
        PotionEffectType[] result = new PotionEffectType[maxId + 1];
        return byId.values().toArray(result); // Cauldron change underlying storage to map
        // Cauldron end
    }
}
