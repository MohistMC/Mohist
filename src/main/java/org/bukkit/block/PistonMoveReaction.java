package org.bukkit.block;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents how a block or entity will react when interacting with a piston
 * when it is extending or retracting.
 */
public enum PistonMoveReaction {

    /**
     * Indicates that the block can be pushed or pulled.
     */
    MOVE(0),
    /**
     * Indicates the block is fragile and will break if pushed on.
     */
    BREAK(1),
    /**
     * Indicates that the block will resist being pushed or pulled.
     */
    BLOCK(2),
    /**
     * Indicates that the entity will ignore any interaction(s) with
     * pistons.
     * <br>
     * Blocks should use {@link PistonMoveReaction#BLOCK}.
     */
    IGNORE(3),
    /**
     * Indicates that the block can only be pushed by pistons, not pulled.
     */
    PUSH_ONLY(4);

    private static final Map<Integer, PistonMoveReaction> byId = new HashMap<>();

    static {
        for (PistonMoveReaction reaction : PistonMoveReaction.values()) {
            byId.put(reaction.id, reaction);
        }
    }

    private final int id;

    PistonMoveReaction(int id) {
        this.id = id;
    }

    /**
     * @param id An ID
     * @return The move reaction with that ID
     * @deprecated Magic value
     */
    public static PistonMoveReaction getById(int id) {
        return byId.get(id);
    }

    /**
     * @return The ID of the move reaction
     * @deprecated Magic value
     */
    public int getId() {
        return this.id;
    }
}
