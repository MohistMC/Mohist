package org.bukkit.craftbukkit.entity;

import java.util.Random;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class CraftFirework extends CraftProjectile implements Firework {

    private final Random random = new Random();
    private final CraftItemStack item;

    public CraftFirework(CraftServer server, FireworkRocketEntity entity) {
        super(server, entity);

        ItemStack item = getHandle().getDataManager().get(FireworkRocketEntity.FIREWORK_ITEM);

        if (item.isEmpty()) {
            item = new ItemStack(Items.FIREWORK_ROCKET);
            getHandle().getDataManager().set(FireworkRocketEntity.FIREWORK_ITEM, item);
        }

        this.item = CraftItemStack.asCraftMirror(item);

        // Ensure the item is a firework...
        if (this.item.getType() != Material.FIREWORK_ROCKET) {
            this.item.setType(Material.FIREWORK_ROCKET);
        }
    }

    @Override
    public FireworkRocketEntity getHandle() {
        return (FireworkRocketEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftFirework";
    }

    @Override
    public EntityType getType() {
        return EntityType.FIREWORK;
    }

    @Override
    public FireworkMeta getFireworkMeta() {
        return (FireworkMeta) item.getItemMeta();
    }

    @Override
    public void setFireworkMeta(FireworkMeta meta) {
        item.setItemMeta(meta);

        // Copied from FireworkRocketEntity constructor, update firework lifetime/power
        getHandle().lifetime = 10 * (1 + meta.getPower()) + random.nextInt(6) + random.nextInt(7);

        getHandle().getDataManager().markDirty(FireworkRocketEntity.FIREWORK_ITEM);
    }

    @Override
    public void detonate() {
        getHandle().lifetime = 0;
    }

    @Override
    public boolean isShotAtAngle() {
        return getHandle().func_213889_i();
    }

    @Override
    public void setShotAtAngle(boolean shotAtAngle) {
        getHandle().getDataManager().set(FireworkRocketEntity.field_213895_d, shotAtAngle);
    }
}
