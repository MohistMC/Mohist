package org.bukkit;

import com.google.common.collect.Maps;
import java.util.Map;
import org.bukkit.block.BlockFace;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.Potion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
     * Visual effect of an instant splash potion breaking. Needs color data
     * value as additional info.
     */
    INSTANT_POTION_BREAK(2007, Type.VISUAL, Color.class),
    /**
     * An ender eye signal; a visual effect.
     */
    ENDER_SIGNAL(2003, Type.VISUAL),
    /**
     * The flames seen on a mobspawner; a visual effect.
     */
    MOBSPAWNER_FLAMES(2004, Type.VISUAL),
    /**
     * The spark that comes off a fireworks
     */
    FIREWORKS_SPARK("fireworksSpark", Type.PARTICLE),
    /**
     * Critical hit particles
     */
    CRIT("crit", Type.PARTICLE),
    /**
     * Blue critical hit particles
     */
    MAGIC_CRIT("magicCrit", Type.PARTICLE),
    /**
     *  Multicolored potion effect particles
     */
    POTION_SWIRL("mobSpell", Type.PARTICLE),
    /**
     * Multicolored potion effect particles that are slightly transparent
     */
    POTION_SWIRL_TRANSPARENT("mobSpellAmbient", Type.PARTICLE),
    /**
     * A puff of white potion swirls
     */
    SPELL("spell", Type.PARTICLE),
    /**
     * A puff of white stars
     */
    INSTANT_SPELL("instantSpell", Type.PARTICLE),
    /**
     *  A puff of purple particles
     */
    WITCH_MAGIC("witchMagic", Type.PARTICLE),
    /**
     * The note that appears above note blocks
     */
    NOTE("note", Type.PARTICLE),
    /**
     * The particles shown at nether portals
     */
    PORTAL("portal", Type.PARTICLE),
    /**
     * The symbols that fly towards the enchantment table
     */
    FLYING_GLYPH("enchantmenttable", Type.PARTICLE),
    /**
     * Fire particles
     */
    FLAME("flame", Type.PARTICLE),
    /**
     * The particles that pop out of lava
     */
    LAVA_POP("lava", Type.PARTICLE),
    /**
     *  A small gray square
     */
    FOOTSTEP("footstep", Type.PARTICLE),
    /**
     * Water particles
     */
    SPLASH("splash", Type.PARTICLE),
    /**
     * Smoke particles
     */
    PARTICLE_SMOKE("smoke", Type.PARTICLE),
    /**
     *The biggest explosion particle effect
     */
    EXPLOSION_HUGE("hugeexplosion", Type.PARTICLE),
    /**
     * A larger version of the explode particle
     */
    EXPLOSION_LARGE("largeexplode", Type.PARTICLE),
    /**
     * Explosion particles
     */
    EXPLOSION("explode", Type.PARTICLE),
    /**
     * Small gray particles
     */
    VOID_FOG("depthsuspend", Type.PARTICLE),
    /**
     * Small gray particles
     */
    SMALL_SMOKE("townaura", Type.PARTICLE),
    /**
     *  A puff of white smoke
     */
    CLOUD("cloud", Type.PARTICLE),
    /**
     *  Multicolored dust particles
     */
    COLOURED_DUST("reddust", Type.PARTICLE),
    /**
     * Snowball breaking
     */
    SNOWBALL_BREAK("snowballpoof", Type.PARTICLE),
    /**
     * The water drip particle that appears on blocks under water
     */
    WATERDRIP("dripWater", Type.PARTICLE),
    /**
     * The lava drip particle that appears on blocks under lava
     */
    LAVADRIP("dripLava", Type.PARTICLE),
    /**
     * White particles
     */
    SNOW_SHOVEL("snowshovel", Type.PARTICLE),
    /**
     *  The particle shown when a slime jumps
     */
    SLIME("slime", Type.PARTICLE),
    /**
     *  The particle that appears when breading animals
     */
    HEART("heart", Type.PARTICLE),
    /**
     * The particle that appears when hitting a villager
     */
    VILLAGER_THUNDERCLOUD("angryVillager", Type.PARTICLE),
    /**
     * The particle that appears when trading with a villager
     */
    HAPPY_VILLAGER("happyVillager", Type.PARTICLE),
    /**
     * The smoke particles that appears on blazes, minecarts
     * with furnaces and fire
     */
    LARGE_SMOKE("largesmoke", Type.PARTICLE),
    /**
     * The particles generated when a tool breaks.
     * This particle requires a Material so that the client can select the correct texture.
     */
    ITEM_BREAK("iconcrack", Type.PARTICLE, Material.class),
    /**
     * The particles generated while breaking a block.
     * This particle requires a Material and data value so that the client can select the correct texture.
     */
    TILE_BREAK("blockcrack", Type.PARTICLE, MaterialData.class),
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
     *  The particles generated while sprinting a block
     *  This particle requires a Material and data value so that the client can select the correct texture.
     */
    TILE_DUST("blockdust", Type.PARTICLE, MaterialData.class);

    private final int id;
    private final Type type;
    private final Class<?> data;
    private static final Map<Integer, Effect> BY_ID = Maps.newHashMap();
    private static final Map<String, Effect> BY_NAME = Maps.newHashMap();
    private final String particleName;

    private Effect(int id, /*@NotNull*/ Type type) {
        this(id, type, null);
    }

    private Effect(int id, /*@NotNull*/ Type type, /*@Nullable*/ Class<?> data) {
        this.id = id;
        this.type = type;
        this.data = data;
        particleName = null;
    }

    private Effect(String particleName, Type type, Class<?> data) {
        this.particleName = particleName;
        this.type = type;
        id = 0;
        this.data = data;
    }

    private Effect(String particleName, Type type) {
        this.particleName = particleName;
        this.type = type;
        id = 0;
        this.data = null;
    }

    /**
     * Gets the ID for this effect.
     *
     * @return if this Effect isn't of type PARTICLE it returns ID of this effect
     * @deprecated Magic value
     */
    @Deprecated
    public int getId() {
        return this.id;
    }

    /**
     * Returns the effect's name. This returns null if the effect is not a particle
     *
     * @return The effect's name
     */
    public String getName() {
        return particleName;
    }
    /**
     * @return The type of the effect.
     */
    @NotNull
    public Type getType() {
        return this.type;
    }

    /**
     * @return if this Effect isn't of type PARTICLE it returns the class which represents data for this effect, or null if none
     */
    @Nullable
    public Class<?> getData() {
        return this.data;
    }

    /**
     * Gets the Effect associated with the given ID.
     *
     * @param id ID of the Effect to return
     * @return Effect with the given ID
     * @deprecated Magic value
     */
    @Deprecated
    @Nullable
    public static Effect getById(int id) {
        return BY_ID.get(id);
    }

    static {
        for (Effect effect : values()) {
            if (effect.type != Type.PARTICLE) {
                BY_ID.put(effect.id, effect);
            }
        }
    }

    /**
     * Gets the Effect associated with the given name.
     * @param name name of the Effect to return
     * @return Effect with the given name
     */
    public static Effect getByName(String name) {
        return BY_NAME.get(name);
    }

    static {
        for (Effect effect : values()) {
            if (effect.type == Type.PARTICLE) {
                BY_NAME.put(effect.particleName, effect);
            }
        }
    }
    /**
     * Represents the type of an effect.
     */
    public enum Type {SOUND, VISUAL,PARTICLE}
}
