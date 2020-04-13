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

package net.minecraftforge.common.util;

import javax.annotation.Nullable;

import com.mojang.authlib.GameProfile;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.client.CPacketClientSettings;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.stats.StatBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import red.mohist.Mohist;

import java.net.InetAddress;
import java.util.ArrayList;

//Preliminary, simple Fake Player class
public class FakePlayer extends EntityPlayerMP
{
    static public ArrayList<FakePlayer> fakePlayers=new ArrayList();
    static public boolean BukkitInited=false;
    public FakePlayer(WorldServer world, GameProfile name)
    {
        super(FMLCommonHandler.instance().getMinecraftServerInstance(), world, name, new PlayerInteractionManager(world));
        //KCauldronX Start
        if(MinecraftServer.mohistConfig.fakePlayerLogin.getValue()) {
            if (!BukkitInited) {
                fakePlayers.add(this);
            } else {
                callBukkitLoginEvent();
            }
        }
        //KCauldronX End
    }
    public void callBukkitLoginEvent(){
        new Thread(() -> {
            AsyncPlayerPreLoginEvent prelogin2 = new AsyncPlayerPreLoginEvent(getName(), InetAddress.getLoopbackAddress(), getUniqueID());
            Bukkit.getPluginManager().callEvent(prelogin2);
            MinecraftServer.getServerInst().processQueue.add(() -> {
                PlayerPreLoginEvent prelogin1 = new PlayerPreLoginEvent(getName(), InetAddress.getLoopbackAddress(), getUniqueID());
                Bukkit.getPluginManager().callEvent(prelogin1);
                PlayerLoginEvent login = new PlayerLoginEvent(getBukkitEntity(), "localhost", InetAddress.getLoopbackAddress());
                Bukkit.getPluginManager().callEvent(login);
                Mohist.LOGGER.info("%s","Fakeplayer "+getName()+" ("+getUniqueID()+") joined server");
            });
        }).start();
    }

    @Override public Vec3d getPositionVector(){ return new Vec3d(0, 0, 0); }
    @Override public boolean canUseCommand(int i, String s){ return false; }
    @Override public void sendStatusMessage(ITextComponent chatComponent, boolean actionBar){}
    @Override public void sendMessage(ITextComponent component) {}
    @Override public void addStat(StatBase par1StatBase, int par2){}
    @Override public void openGui(Object mod, int modGuiId, World world, int x, int y, int z){}
    @Override public boolean isEntityInvulnerable(DamageSource source){ return true; }
    @Override public boolean canAttackPlayer(EntityPlayer player){ return false; }
    @Override public void onDeath(DamageSource source){ return; }
    @Override public void onUpdate(){ return; }
    @Override public Entity changeDimension(int dim, ITeleporter teleporter){ return this; }
    @Override public void handleClientSettings(CPacketClientSettings pkt){ return; }
    @Override @Nullable public MinecraftServer getServer() { return FMLCommonHandler.instance().getMinecraftServerInstance(); }
}