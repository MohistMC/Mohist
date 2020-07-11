package org.bukkit.entity;

public interface Guardian extends Monster {

    /**
     * Check if the Guardian is an elder Guardian
     *
     * @return true if the Guardian is an Elder Guardian, false if not
     * @deprecated should check if instance of {@link ElderGuardian}.
     */
    @Deprecated
    boolean isElder();

    /**
     * @param shouldBeElder
     * @deprecated Must spawn a new {@link ElderGuardian}.
     */
    @Deprecated
    void setElder(boolean shouldBeElder);
}
