package org.bukkit.entity;

/**
 * Represents a {@link Zombie} which was once a {@link Villager}.
 */
public interface ZombieVillager extends Zombie {

    /**
     * Returns the villager profession of this zombie.
     *
     * @return the profession or null
     */
    public Villager.Profession getVillagerProfession();

    /**
     * Sets the villager profession of this zombie.
     */
    public void setVillagerProfession(Villager.Profession profession);
}
