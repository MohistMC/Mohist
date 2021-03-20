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

package net.minecraftforge.event.world;

import com.mojang.authlib.GameProfile;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

/** ExplosionEvent triggers when an explosion happens in the world.<br>
 * <br>
 * ExplosionEvent.Start is fired before the explosion actually occurs.<br>
 * ExplosionEvent.Detonate is fired once the explosion has a list of affected blocks and entities.<br>
 * <br>
 * ExplosionEvent.Start is {@link Cancelable}.<br>
 * ExplosionEvent.Detonate can modify the affected blocks and entities.<br>
 * Children do not use {@link HasResult}.<br>
 * Children of this event are fired on the {@link MinecraftForge#EVENT_BUS}.<br>
 */
public class ExplosionEvent extends Event
{
    private final World world;
    private final Explosion explosion;
    public static FakePlayer exploder_fake = null;
    public static final GameProfile exploder_profile = new GameProfile(null, "[Explosive]");

    public ExplosionEvent(World world, Explosion explosion)
    {
        if(exploder_fake == null || !exploder_fake.world.equals(world))
        {
            exploder_fake = FakePlayerFactory.get((WorldServer) world, exploder_profile);
        }
        this.world = world;
        this.explosion = explosion;
    }

    public World getWorld()
    {
        return world;
    }

    public Explosion getExplosion()
    {
        return explosion;
    }

    /** ExplosionEvent.Start is fired before the explosion actually occurs.  Canceling this event will stop the explosion.<br>
     * <br>
     * This event is {@link Cancelable}.<br>
     * This event does not use {@link HasResult}.<br>
     * This event is fired on the {@link MinecraftForge#EVENT_BUS}.<br>
     */
    @Cancelable
    public static class Start extends ExplosionEvent
    {
        private ExplosionPrimeEvent event;
        public Start(World world, Explosion explosion)
        {
            super(world, explosion);
            // CraftBukkit start
            // float f = 4.0F;
            org.bukkit.craftbukkit.v1_12_R1.CraftServer server = world.getServer();
            org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity ce = null;
            if(explosion.exploder != null && explosion.exploder instanceof EntityLivingBase)
            {
                ce = new org.bukkit.craftbukkit.v1_12_R1.entity.CraftTNTPrimed(server, new EntityTNTPrimed(world, explosion.x, explosion.y, explosion.z, (EntityLivingBase) explosion.exploder ));
            }
            if(ce == null)
            {
                ce = new org.bukkit.craftbukkit.v1_12_R1.entity.CraftTNTPrimed(server, new EntityTNTPrimed(world, explosion.x, explosion.y, explosion.z, exploder_fake ));
            }
            event = new ExplosionPrimeEvent(ce, 8.0F, true);
            server.getPluginManager().callEvent(event);
        }

        @Override
        public boolean isCanceled()
        {
            return super.isCanceled() || this.event.isCancelled();
        }

        @Override
        public void setCanceled(boolean cancel)
        {
            if (!isCancelable())
            {
                throw new IllegalArgumentException("Attempted to cancel a uncancelable event");
            }
            super.setCanceled(cancel);
            this.event.setCancelled(cancel);
        }
    }

    /** ExplosionEvent.Detonate is fired once the explosion has a list of affected blocks and entities.  These lists can be modified to change the outcome.<br>
     * <br>
     * This event is not {@link Cancelable}.<br>
     * This event does not use {@link HasResult}.<br>
     * This event is fired on the {@link MinecraftForge#EVENT_BUS}.<br>
     */
    public static class Detonate extends ExplosionEvent
    {
        private List<Entity> entityList;
        private EntityExplodeEvent event;

        public Detonate(World world, Explosion explosion, List<Entity> entityList)
        {
            super(world, explosion);
            org.bukkit.craftbukkit.v1_12_R1.CraftServer server = world.getServer();
            org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity ce = null;
            if(explosion.exploder != null && explosion.exploder instanceof EntityLivingBase)
            {
                ce = new org.bukkit.craftbukkit.v1_12_R1.entity.CraftTNTPrimed(server, new EntityTNTPrimed(world, explosion.x, explosion.y, explosion.z, (EntityLivingBase) explosion.exploder ));
            }
            if(ce == null)
            {
                ce = new org.bukkit.craftbukkit.v1_12_R1.entity.CraftTNTPrimed(server, new EntityTNTPrimed(world, explosion.x, explosion.y, explosion.z, exploder_fake ));
            }
            List<Block> bukkitBlocks = createBukkitBlocks(getAffectedBlocks(), Bukkit.getWorld(world.getWorld().getUID()));

            // we put the yield at 0 so bukkit is not handling the explosion otherwise drops might drop two times
            event = new EntityExplodeEvent(ce, new Location(world.getWorld(), explosion.x, explosion.y, explosion.z),
                    bukkitBlocks, 0f);
            server.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                explosion.clearAffectedBlockPositions();
                this.entityList = new ArrayList<>();
            } else {
                this.entityList = entityList;
            }
        }

        /**
         * This function allow us to get the Bukkit blocks from the forge position of blocks
         * @param blocks The list of position of blocks
         * @param world The world that the explosion occurred in
         * @return A list of Bukkit Blocks that are impacted by the explosion
         */
        private List<Block> createBukkitBlocks(List<BlockPos> blocks, org.bukkit.World world) {
            final ArrayList<Block> ret = new ArrayList<>();
            for (BlockPos pos : blocks)
                ret.add(world.getBlockAt(pos.getX(), pos.getY(), pos.getZ()));

            return ret;
        }

        /** return the list of blocks affected by the explosion. */
        public List<BlockPos> getAffectedBlocks()
        {
            return getExplosion().getAffectedBlockPositions();
        }

        /** return the list of entities affected by the explosion. */
        public List<Entity> getAffectedEntities()
        {
            return entityList;
        }
    }
}