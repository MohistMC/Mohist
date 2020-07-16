package org.bukkit.craftbukkit.entity;

import java.util.Random;
import net.minecraft.entity.FireworkEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class CraftFirework extends CraftEntity implements Firework {

    private final Random random = new Random();
    private final CraftItemStack item;

    public CraftFirework(CraftServer server, FireworkEntity entity) {
        super(server, entity);

        ItemStack item = getHandle().getDataTracker().get(FireworkEntity.ITEM);

        if (item.isEmpty()) {
            item = new ItemStack(Items.FIREWORK_ROCKET);
            getHandle().getDataTracker().set(FireworkEntity.ITEM, item);
        }

        this.item = CraftItemStack.asCraftMirror(item);

        // Ensure the item is a firework...
        if (this.item.getType() != Material.FIREWORK_ROCKET) {
            this.item.setType(Material.FIREWORK_ROCKET);
        }
    }

    @Override
    public FireworkEntity getHandle() {
        return (FireworkEntity) entity;
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

        // Copied from EntityFireworks constructor, update firework lifetime/power
        getHandle().lifeTime = 10 * (1 + meta.getPower()) + random.nextInt(6) + random.nextInt(7);

        getHandle().getDataTracker().markDirty(FireworkEntity.ITEM);
    }

    @Override
    public void detonate() {
        getHandle().lifeTime = 0;
    }

    @Override
    public boolean isShotAtAngle() {
        return getHandle().wasShotAtAngle();
    }

    @Override
    public void setShotAtAngle(boolean shotAtAngle) {
        getHandle().getDataTracker().set(FireworkEntity.SHOT_AT_ANGLE, shotAtAngle);
    }
}
