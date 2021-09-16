package io.papermc.paper.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

public class PlayerArmSwingEvent extends PlayerAnimationEvent {

    private final EquipmentSlot equipmentSlot;

    public PlayerArmSwingEvent(@NotNull Player player, @NotNull EquipmentSlot equipmentSlot) {
        super(player, PlayerAnimationType.ARM_SWING);
        this.equipmentSlot = equipmentSlot;
    }

    /**
     * Returns the hand of the arm swing.
     *
     * @return the hand
     */
    @NotNull
    public EquipmentSlot getHand() {
        return this.equipmentSlot;
    }
}