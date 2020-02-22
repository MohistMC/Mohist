package org.bukkit.craftbukkit.entity;

public class CraftSizedFireball extends CraftFireball implements SizedFireball {

    public CraftSizedFireball(CraftServer server, EntityFireballFireball entity) {
        super(server, entity);
    }

    @Override
    public ItemStack getDisplayItem() {
        if (getHandle().l().isEmpty()) { // PAIL rename getItem
            return new ItemStack(Material.FIRE_CHARGE);
        } else {
            return CraftItemStack.asBukkitCopy(getHandle().l()); // PAIL rename getItem
        }
    }

    @Override
    public void setDisplayItem(ItemStack item) {
        getHandle().b(CraftItemStack.asNMSCopy(item)); // PAIL rename setItem
    }

    @Override
    public EntityFireballFireball getHandle() {
        return (EntityFireballFireball) entity;
    }
}
