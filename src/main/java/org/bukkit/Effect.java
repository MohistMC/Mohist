package org.bukkit;

import com.google.common.collect.Maps;
import org.bukkit.block.BlockFace;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.Potion;

import java.util.Map;

/**
 * A list of effects that the server is able to send to players.
 */
public enum Effect {
    /**
     * An alternate click sound.
     */
    CLICK2(1000, Type.SOUND),
    /**
     * A click sound.
     */
    CLICK1(1001, Type.SOUND),
    /**
     * Sound of a bow firing.
     */
    BOW_FIRE(1002, Type.SOUND),
    /**
     * Sound of a door opening.
     */
    DOOR_TOGGLE(1006, Type.SOUND),
    /**
     * Sound of a door opening.
     */
    IRON_DOOR_TOGGLE(1005, Type.SOUND),
    /**
     * Sound of a trapdoor opening.
     */
    TRAPDOOR_TOGGLE(1007, Type.SOUND),
    /**
     * Sound of a door opening.
     */
    IRON_TRAPDOOR_TOGGLE(1037, Type.SOUND),
    /**
     * Sound of a door opening.
     */
    FENCE_GATE_TOGGLE(1008, Type.SOUND),
    /**
     * Sound of a door closing.
     */
    DOOR_CLOSE(1012, Type.SOUND),
    /**
     * Sound of a door closing.
     */
    IRON_DOOR_CLOSE(1011, Type.SOUND),
    /**
     * Sound of a trapdoor closing.
     */
    TRAPDOOR_CLOSE(1013, Type.SOUND),
    /**
     * Sound of a door closing.
     */
    IRON_TRAPDOOR_CLOSE(1036, Type.SOUND),
    /**
     * Sound of a door closing.
     */
    FENCE_GATE_CLOSE(1014, Type.SOUND),
    /**
     * Sound of fire being extinguished.
     */
    EXTINGUISH(1009, Type.SOUND),
    /**
     * A song from a record. Needs the record item ID as additional info
     */
    RECORD_PLAY(1010, Type.SOUND, Material.class),
    /**
     * Sound of ghast shrieking.
     */
    GHAST_SHRIEK(1015, Type.SOUND),
    /**
     * Sound of ghast firing.
     */
    GHAST_SHOOT(1016, Type.SOUND),
    /**
     * Sound of blaze firing.
     */
    BLAZE_SHOOT(1018, Type.SOUND),
    /**
     * Sound of zombies chewing on wooden doors.
     */
    ZOMBIE_CHEW_WOODEN_DOOR(1019, Type.SOUND),
    /**
     * Sound of zombies chewing on iron doors.
     */
    ZOMBIE_CHEW_IRON_DOOR(1020, Type.SOUND),
    /**
     * Sound of zombies destroying a door.
     */
    ZOMBIE_DESTROY_DOOR(1021, Type.SOUND),
    /**
     * A visual smoke effect. Needs direction as additional info.
     */
    SMOKE(2000, Type.VISUAL, BlockFace.class),
    /**
     * Sound of a block breaking. Needs block ID as additional info.
     */
    STEP_SOUND(2001, Type.SOUND, Material.class),
    /**
     * Visual effect of a splash potion breaking. Needs potion data value as
     * additional info.
     */
    POTION_BREAK(2002, Type.VISUAL, Potion.class),
    /**
     * An ender eye signal; a visual effect.
     */
    ENDER_SIGNAL(2003, Type.VISUAL),
    /**
     * The flames seen on a mobspawner; a visual effect.
     */
    MOBSPAWNER_FLAMES(2004, Type.VISUAL),
    /**
     * The sound played by brewing stands when brewing
     */
    BREWING_STAND_BREW(1035, Type.SOUND),
    /**
     * The sound played when a chorus flower grows
     */
    CHORUS_FLOWER_GROW(1033, Type.SOUND),
    /**
     * The sound played when a chorus flower dies
     */
    CHORUS_FLOWER_DEATH(1034, Type.SOUND),
    /**
     * The sound played when traveling through a portal
     */
    PORTAL_TRAVEL(1032, Type.SOUND),
    /**
     * The sound played when launching an endereye
     */
    ENDEREYE_LAUNCH(1003, Type.SOUND),
    /**
     * The sound played when launching a firework
     */
    FIREWORK_SHOOT(1004, Type.SOUND),
    /**
     * Particles displayed when a villager grows a plant, data
     * is the number of particles
     */
    VILLAGER_PLANT_GROW(2005, Type.VISUAL, Integer.class),
    /**
     * The sound/particles used by the enderdragon's breath
     * attack.
     */
    DRAGON_BREATH(2006, Type.VISUAL),
    /**
     * The sound played when an anvil breaks
     */
    ANVIL_BREAK(1029, Type.SOUND),
    /**
     * The sound played when an anvil is used
     */
    ANVIL_USE(1030, Type.SOUND),
    /**
     * The sound played when an anvil lands after
     * falling
     */
    ANVIL_LAND(1031, Type.SOUND),
    /**
     * Sound of an enderdragon firing
     */
    ENDERDRAGON_SHOOT(1017, Type.SOUND),
    /**
     * The sound played when a wither breaks a block
     */
    WITHER_BREAK_BLOCK(1022, Type.SOUND),
    /**
     * Sound of a wither shooting
     */
    WITHER_SHOOT(1024, Type.SOUND),
    /**
     * The sound played when a zombie infects a target
     */
    ZOMBIE_INFECT(1026, Type.SOUND),
    /**
     * The sound played when a villager is converted by
     * a zombie
     */
    ZOMBIE_CONVERTED_VILLAGER(1027, Type.SOUND),
    /**
     * Sound played by a bat taking off
     */
    BAT_TAKEOFF(1025, Type.SOUND),
    /**
     * The sound/particles caused by a end gateway spawning
     */
    END_GATEWAY_SPAWN(3000, Type.VISUAL),
    /**
     * The sound of an enderdragon growling
     */
    ENDERDRAGON_GROWL(3001, Type.SOUND),
    /**
     * The spark that comes off a fireworks
     *
     * @deprecated Spigot specific API, use {@link Particle}.
     */

    FIREWORKS_SPARK("fireworksSpark", Type.PARTICLE),
    /**
     * Critical hit particles
     *
     * @deprecated Spigot specific API, use {@link Particle}.
     */

    CRIT("crit", Type.PARTICLE),
    /**
     * Blue critical hit particles
     *
     * @deprecated Spigot specific API, use {@link Particle}.
     */

    MAGIC_CRIT("magicCrit", Type.PARTICLE),
    /**
     * Multicolored potion effect particles
     *
     * @deprecated Spigot specific API, use {@link Particle}.
     */

    POTION_SWIRL("mobSpell", Type.PARTICLE),
    /**
     * Multicolored potion effect particles that are slightly transparent
     *
     * @deprecated Spigot specific API, use {@link Particle}.
     */

    POTION_SWIRL_TRANSPARENT("mobSpellAmbient", Type.PARTICLE),
    /**
     * A puff of white potion swirls
     *
     * @deprecated Spigot specific API, use {@link Particle}.
     */

    SPELL("spell", Type.PARTICLE),
    /**
     * A puff of white stars
     *
     * @deprecated Spigot specific API, use {@link Particle}.
     */

    INSTANT_SPELL("instantSpell", Type.PARTICLE),
    /**
     * A puff of purple particles
     *
     * @deprecated Spigot specific API, use {@link Particle}.
     */

    WITCH_MAGIC("witchMagic", Type.PARTICLE),
    /**
     * The note that appears above note blocks
     *
     * @deprecated Spigot specific API, use {@link Particle}.
     */

    NOTE("note", Type.PARTICLE),
    /**
     * The particles shown at nether portals
     *
     * @deprecated Spigot specific API, use {@link Particle}.
     */

    PORTAL("portal", Type.PARTICLE),
    /**
     * The symbols that fly towards the enchantment table
     *
     * @deprecated Spigot specific API, use {@link Particle}.
     */

    FLYING_GLYPH("enchantmenttable", Type.PARTICLE),
    /**
     * Fire particles
     *
     * @deprecated Spigot specific API, use {@link Particle}.
     */

    FLAME("flame", Type.PARTICLE),
    /**
     * The particles that pop out of lava
     *
     * @deprecated Spigot specific API, use {@link Particle}.
     */

    LAVA_POP("lava", Type.PARTICLE),
    /**
     * A small gray square
     *
     * @deprecated Spigot specific API, use {@link Particle}.
     */

    FOOTSTEP("footstep", Type.PARTICLE),
    /**
     * Water particles
     *
     * @deprecated Spigot specific API, use {@link Particle}.
     */

    SPLASH("splash", Type.PARTICLE),
    /**
     * Smoke particles
     *
     * @deprecated Spigot specific API, use {@link Particle}.
     */

    PARTICLE_SMOKE("smoke", Type.PARTICLE),
    /**
     * The biggest explosion particle effect
     *
     * @deprecated Spigot specific API, use {@link Particle}.
     */

    EXPLOSION_HUGE("hugeexplosion", Type.PARTICLE),
    /**
     * A larger version of the explode particle
     *
     * @deprecated Spigot specific API, use {@link Particle}.
     */

    EXPLOSION_LARGE("largeexplode", Type.PARTICLE),
    /**
     * Explosion particles
     *
     * @deprecated Spigot specific API, use {@link Particle}.
     */

    EXPLOSION("explode", Type.PARTICLE),
    /**
     * Small gray particles
     *
     * @deprecated Spigot specific API, use {@link Particle}.
     */

    VOID_FOG("depthsuspend", Type.PARTICLE),
    /**
     * Small gray particles
     *
     * @deprecated Spigot specific API, use {@link Particle}.
     */

    SMALL_SMOKE("townaura", Type.PARTICLE),
    /**
     * A puff of white smoke
     *
     * @deprecated Spigot specific API, use {@link Particle}.
     */

    CLOUD("cloud", Type.PARTICLE),
    /**
     * Multicolored dust particles
     *
     * @deprecated Spigot specific API, use {@link Particle}.
     */

    COLOURED_DUST("reddust", Type.PARTICLE),
    /**
     * Snowball breaking
     *
     * @deprecated Spigot specific API, use {@link Particle}.
     */

    SNOWBALL_BREAK("snowballpoof", Type.PARTICLE),
    /**
     * The water drip particle that appears on blocks under water
     *
     * @deprecated Spigot specific API, use {@link Particle}.
     */

    WATERDRIP("dripWater", Type.PARTICLE),
    /**
     * The lava drip particle that appears on blocks under lava
     *
     * @deprecated Spigot specific API, use {@link Particle}.
     */

    LAVADRIP("dripLava", Type.PARTICLE),
    /**
     * White particles
     *
     * @deprecated Spigot specific API, use {@link Particle}.
     */

    SNOW_SHOVEL("snowshovel", Type.PARTICLE),
    /**
     * The particle shown when a slime jumps
     *
     * @deprecated Spigot specific API, use {@link Particle}.
     */

    SLIME("slime", Type.PARTICLE),
    /**
     * The particle that appears when breading animals
     *
     * @deprecated Spigot specific API, use {@link Particle}.
     */

    HEART("heart", Type.PARTICLE),
    /**
     * The particle that appears when hitting a villager
     *
     * @deprecated Spigot specific API, use {@link Particle}.
     */

    VILLAGER_THUNDERCLOUD("angryVillager", Type.PARTICLE),
    /**
     * The particle that appears when trading with a villager
     *
     * @deprecated Spigot specific API, use {@link Particle}.
     */

    HAPPY_VILLAGER("happyVillager", Type.PARTICLE),
    /**
     * The smoke particles that appears on blazes, minecarts
     * with furnaces and fire
     *
     * @deprecated Spigot specific API, use {@link Particle}.
     */

    LARGE_SMOKE("largesmoke", Type.PARTICLE),
    /**
     * The particles generated when a tool breaks.
     * This particle requires a Material so that the client can select the correct texture.
     *
     * @deprecated Spigot specific API, use {@link Particle}.
     */

    ITEM_BREAK("iconcrack", Type.PARTICLE, Material.class),
    /**
     * The particles generated while breaking a block.
     * This particle requires a Material and data value so that the client can select the correct texture.
     *
     * @deprecated Spigot specific API, use {@link Particle}.
     */

    TILE_BREAK("blockcrack", Type.PARTICLE, MaterialData.class),
    /**
     * The particles generated while sprinting a block
     * This particle requires a Material and data value so that the client can select the correct texture.
     *
     * @deprecated Spigot specific API, use {@link Particle}.
     */

    TILE_DUST("blockdust", Type.PARTICLE, MaterialData.class);

    private static final Map<Integer, Effect> BY_ID = Maps.newHashMap();
    private static final Map<String, Effect> BY_NAME = Maps.newHashMap();

    static {
        for (Effect effect : values()) {
            if (effect.type != Type.PARTICLE) {
                BY_ID.put(effect.id, effect);
            }
        }
    }

    static {
        for (Effect effect : values()) {
            if (effect.type == Type.PARTICLE) {
                BY_NAME.put(effect.particleName, effect);
            }
        }
    }

    private final int id;
    private final Type type;
    private final Class<?> data;
    private final String particleName;

    Effect(int id, Type type) {
        this(id, type, null);
    }

    Effect(int id, Type type, Class<?> data) {
        this.id = id;
        this.type = type;
        this.data = data;
        particleName = null;
    }

    Effect(String particleName, Type type, Class<?> data) {
        this.particleName = particleName;
        this.type = type;
        id = 0;
        this.data = data;
    }

    Effect(String particleName, Type type) {
        this.particleName = particleName;
        this.type = type;
        id = 0;
        this.data = null;
    }

    /**
     * Gets the Effect associated with the given ID.
     *
     * @param id ID of the Effect to return
     * @return Effect with the given ID
     * @deprecated Magic value
     */

    public static Effect getById(int id) {
        return BY_ID.get(id);
    }

    /**
     * Gets the Effect associated with the given name.
     *
     * @param name name of the Effect to return
     * @return Effect with the given name
     * @deprecated Spigot specific API, use {@link Particle}.
     */

    public static Effect getByName(String name) {
        return BY_NAME.get(name);
    }

    /**
     * Gets the ID for this effect.
     *
     * @return if this Effect isn't of type PARTICLE it returns ID of this effect
     * @deprecated Magic value
     */

    public int getId() {
        return this.id;
    }

    /**
     * Returns the effect's name. This returns null if the effect is not a particle
     *
     * @return The effect's name
     * @deprecated Spigot specific API, use {@link Particle}.
     */

    public String getName() {
        return particleName;
    }

    /**
     * @return The type of the effect.
     */
    public Type getType() {
        return this.type;
    }

    /**
     * @return if this Effect isn't of type PARTICLE it returns the class which represents data for this effect, or null if none
     */
    public Class<?> getData() {
        return this.data;
    }

    /**
     * Represents the type of an effect.
     */
    public enum Type {
        SOUND, VISUAL, PARTICLE
    }
}
