package org.bukkit.entity;

/**
 * Represents a Zombie.
 */
public interface Zombie extends Monster {

    /**
     * Gets whether the zombie is a baby
     *
     * @return Whether the zombie is a baby
     */
    boolean isBaby();

    /**
     * Sets whether the zombie is a baby
     *
     * @param flag Whether the zombie is a baby
     */
    void setBaby(boolean flag);

    /**
     * Gets whether the zombie is a villager
     *
     * @return Whether the zombie is a villager
     * @deprecated check if instanceof {@link ZombieVillager}.
     */
    @Deprecated
    boolean isVillager();

    /**
     * @param flag
     * @deprecated must spawn {@link ZombieVillager}.
     */
    @Deprecated
    void setVillager(boolean flag);

    /**
     * @return profession
     * @see ZombieVillager#getVillagerProfession()
     */
    @Deprecated
    Villager.Profession getVillagerProfession();

    /**
     * @param profession
     * @see ZombieVillager#getVillagerProfession()
     */
    @Deprecated
    void setVillagerProfession(Villager.Profession profession);
}
