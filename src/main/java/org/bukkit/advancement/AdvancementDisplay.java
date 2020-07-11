package org.bukkit.advancement;

import org.jetbrains.annotations.NotNull;

public interface AdvancementDisplay {
    /**
     * Get the title of this advancement
     *
     * @return Title text
     */
    @NotNull
    String getTitle();

    /**
     * Get the description of this advancement
     *
     * @return Description text
     */
    @NotNull
    String getDescription();

    /**
     * Get the frame type of this advancement
     *
     * @return Frame type
     */
    AdvancementDisplay getFrameType();

    /**
     * Get if this advancement should be announced in chat when completed
     *
     * @return True if should announce when completed
     */
    boolean shouldAnnounceToChat();

    /**
     * Set if this advancement should be announced in chat when completed
     *
     * @param announce True or false
     */
    void setShouldAnnounceToChat(boolean announce);

    /**
     * Get if this advancement (and all it's children) is hidden from the advancement screen until it has been completed
     * <p>
     * This has no effect on root advancements themselves, but will alter their children
     *
     * @return True if hidden until completed
     */
    boolean isHidden();
}