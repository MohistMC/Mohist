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

import com.mohistmc.MohistMC;
import com.mojang.authlib.GameProfile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.client.CClientSettingsPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.stats.Stat;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.bukkit.Bukkit;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.UUID;

//Preliminary, simple Fake Player class
public class FakePlayer extends ServerPlayerEntity
{
    static public ArrayList<FakePlayer> fakePlayers=new ArrayList();
    static public boolean BukkitInited=false;

    public FakePlayer(ServerWorld world, GameProfile name)
    {
        super(world.getServer(), world, name, new PlayerInteractionManager(world));
        //Mohist Start
        if (!BukkitInited) {
            fakePlayers.add(this);
        } else {
            callBukkitLoginEvent();
        }
    }
    //Mohist End

    public void callBukkitLoginEvent(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                AsyncPlayerPreLoginEvent prelogin2 = new AsyncPlayerPreLoginEvent(getScoreboardName(), InetAddress.getLoopbackAddress(), getUniqueID());
                Bukkit.getPluginManager().callEvent(prelogin2);
                MinecraftServer.getServer().processQueue.add(new Runnable() {
                    @Override
                    public void run() {
                        PlayerPreLoginEvent prelogin1 = new PlayerPreLoginEvent(getScoreboardName(), InetAddress.getLoopbackAddress(), getUniqueID());
                        Bukkit.getPluginManager().callEvent(prelogin1);
                        PlayerLoginEvent login = new PlayerLoginEvent(getBukkitEntity(), "localhost", InetAddress.getLoopbackAddress());
                        Bukkit.getPluginManager().callEvent(login);
                        MohistMC.LOGGER.info("%s","Fakeplayer "+getScoreboardName()+" ("+getUniqueID()+") joined server");
                    }
                });
            }
        }).start();
    }
    @Override public Vector3d getPositionVec(){ return new Vector3d(0, 0, 0); }
    @Override public BlockPos getPosition(){ return BlockPos.ZERO; }
    @Override public void sendStatusMessage(ITextComponent chatComponent, boolean actionBar){}
    @Override public void sendMessage(ITextComponent component, UUID senderUUID) {}
    @Override public void addStat(Stat par1StatBase, int par2){}
    //@Override public void openGui(Object mod, int modGuiId, World world, int x, int y, int z){}
    @Override public boolean isInvulnerableTo(DamageSource source){ return true; }
    @Override public boolean canAttackPlayer(PlayerEntity player){ return false; }
    @Override public void onDeath(DamageSource source){ return; }
    @Override public void tick(){ return; }
    @Override public void handleClientSettings(CClientSettingsPacket pkt){ return; }
    @Override @Nullable public MinecraftServer getServer() { return ServerLifecycleHooks.getCurrentServer(); }
}
