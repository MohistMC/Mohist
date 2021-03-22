/*
 * Minecraft Forge
 * Copyright (c) 2016-2020.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package net.minecraftforge.event.entity.player;

import com.google.common.base.Preconditions;
import java.util.List;
import javax.annotation.Nonnegative;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerFishEvent;

/**
 * This event is called when a player fishes an item.
 *
 * This event is {@link net.minecraftforge.fml.common.eventhandler.Cancelable}
 * Canceling the event will cause the player to receive no items at all.
 * The hook will still take the damage specified
 */
@Cancelable
public class ItemFishedEvent extends PlayerEvent
{
    private final NonNullList<ItemStack> stacks = NonNullList.create();
    private final EntityFishHook hook;
    private int rodDamage;
    private PlayerFishEvent event;

    public ItemFishedEvent(List<ItemStack> stacks, int rodDamage, EntityFishHook hook)
    {
        super(hook.getAngler());
        this.stacks.addAll(stacks);
        this.rodDamage = rodDamage;
        this.hook = hook;
        Entity bukkitItem = getBukkitItem(stacks);
        event = new PlayerFishEvent(Bukkit.getPlayer(hook.angler.getUniqueID()), bukkitItem,
                getBukkitHook(hook), bukkitItem != null ? PlayerFishEvent.State.CAUGHT_FISH : PlayerFishEvent.State.FAILED_ATTEMPT);
    }

    /**
     * This method allow us to create a Bukkit ItemStack from a list of forge ItemStacks.
     * It only handle the first one because for bukkit you can only catch one fish.
     * @param stacks The list of fishes that has been caught
     * @return A bukkit ItemStack
     */
    private Entity getBukkitItem(List<ItemStack> stacks) {
        if (stacks == null || stacks.size() == 0)
            return null;
        else {
            org.bukkit.inventory.ItemStack ret;
            ItemStack forgeItemStack = stacks.get(0);

            return ret;
        }
    }

    /**
     * Get the damage the rod will take.
     * @return The damage the rod will take
     */
    public int getRodDamage()
    {
        return rodDamage;
    }

    /**
     * Specifies the amount of damage that the fishing rod should take.
     * This is not added to the pre-existing damage to be taken.
     * @param rodDamage The damage the rod will take. Must be nonnegative
     */
    public void damageRodBy(@Nonnegative int rodDamage)
    {
        Preconditions.checkArgument(rodDamage >= 0);
        this.rodDamage = rodDamage;
    }

    /**
     * Use this to get the items the player will receive.
     * You cannot use this to modify the drops the player will get.
     * If you want to affect the loot, you should use LootTables.
     */
    public NonNullList<ItemStack> getDrops()
    {
        return stacks;
    }

    /**
     * Use this to stuff related to the hook itself, like the position of the bobber.
     */
    public EntityFishHook getHookEntity()
    {
        return hook;
    }
}
