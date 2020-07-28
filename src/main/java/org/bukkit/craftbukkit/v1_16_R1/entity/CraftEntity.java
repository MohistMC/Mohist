package org.bukkit.craftbukkit.v1_16_R1.entity;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R1.block.CraftBlock;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pose;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import com.mojang.brigadier.LiteralMessage;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Texts;
import red.mohist.extra.command.ExtraCommandOutput;
import red.mohist.extra.world.ExtraWorld;

public class CraftEntity implements Entity, CommandSender, ExtraCommandOutput {

    protected static PermissibleBase perm;
    public net.minecraft.entity.Entity nms;

    public CraftEntity(net.minecraft.entity.Entity entity) {
        this.nms = entity;
    }

    public net.minecraft.entity.Entity getHandle() {
        return nms;
    }

    @Override
    public List<MetadataValue> getMetadata(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasMetadata(String arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void removeMetadata(String arg0, Plugin arg1) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setMetadata(String arg0, MetadataValue arg1) {
        // TODO Auto-generated method stub
    }

    @Override
    public String getName() {
        return nms.getEntityName();
    }

    @Override
    public void sendMessage(String message) {
        nms.sendSystemMessage(new LiteralText(message), UUID.randomUUID());
    }

    @Override
    public void sendMessage(String[] arg0) {
        for (String str : arg0) {
            sendMessage(str);
        }
    }

    @Override
    public PermissionAttachment addAttachment(Plugin arg0) {
        return getPermissibleBase().addAttachment(arg0);
    }

    @Override
    public  PermissionAttachment addAttachment(Plugin arg0, int arg1) {
        return getPermissibleBase().addAttachment(arg0, arg1);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin arg0, String arg1, boolean arg2) {
        return getPermissibleBase().addAttachment(arg0, arg1, arg2);
    }

    @Override
    public  PermissionAttachment addAttachment(Plugin arg0, String arg1, boolean arg2, int arg3) {
        return getPermissibleBase().addAttachment(arg0, arg1, arg2, arg3);
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return getPermissibleBase().getEffectivePermissions();
    }

    @Override
    public boolean hasPermission(String arg0) {
        return getPermissibleBase().hasPermission(arg0);
    }

    @Override
    public boolean hasPermission(Permission arg0) {
        return getPermissibleBase().hasPermission(arg0);
    }

    @Override
    public boolean isPermissionSet(String arg0) {
        return getPermissibleBase().isPermissionSet(arg0);
    }

    @Override
    public boolean isPermissionSet(Permission arg0) {
        return getPermissibleBase().isPermissionSet(arg0);
    }

    @Override
    public void recalculatePermissions() {
        getPermissibleBase().recalculatePermissions();
    }

    @Override
    public void removeAttachment(PermissionAttachment arg0) {
        getPermissibleBase().removeAttachment(arg0);
    }

    @Override
    public boolean isOp() {
        return getPermissibleBase().isOp();
    }

    @Override
    public void setOp(boolean arg0) {
        getPermissibleBase().setOp(arg0);
    }

    @Override
    public  String getCustomName() {
        return nms.getCustomName().asString();
    }

    @Override
    public void setCustomName( String arg0) {
        nms.setCustomName(Texts.toText(new LiteralMessage(arg0)));
    }

    @Override
    public PersistentDataContainer getPersistentDataContainer() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean addPassenger(Entity arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean addScoreboardTag(String arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean eject() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public BoundingBox getBoundingBox() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getEntityId() {
        return nms.getEntityId();
    }

    @Override
    public BlockFace getFacing() {
        return CraftBlock.notchToBlockFace(nms.getMovementDirection());
    }

    @Override
    public float getFallDistance() {
        return nms.fallDistance;
    }

    @Override
    public int getFireTicks() {
        return nms.fireTicks;
    }

    @Override
    public double getHeight() {
        return nms.getHeight();
    }

    @Override
    public EntityDamageEvent getLastDamageCause() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Location getLocation() {
        return new Location(getWorld(), nms.getX(), nms.getY(), nms.getZ(), nms.yaw, nms.pitch);
    }

    @Override
    public  Location getLocation(Location loc) {
        if (loc != null) {
            loc.setWorld(getWorld());
            loc.setX(nms.getX());
            loc.setY(nms.getY());
            loc.setZ(nms.getZ());
            loc.setYaw(nms.yaw);
            loc.setPitch(nms.pitch);
        }

        return loc;
    }

    @Override
    public int getMaxFireTicks() {
        return nms.getBurningDuration();
    }

    @Override
    public List<Entity> getNearbyEntities(double arg0, double arg1, double arg2) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public  Entity getPassenger() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Entity> getPassengers() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getPortalCooldown() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Pose getPose() {
        return Pose.values()[nms.getPose().ordinal()];
    }

    @Override
    public Set<String> getScoreboardTags() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Server getServer() {
        return Bukkit.getServer();
    }

    @Override
    public int getTicksLived() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public EntityType getType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UUID getUniqueId() {
        return nms.getUuid();
    }

    @Override
    public  Entity getVehicle() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Vector getVelocity() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public double getWidth() {
        return nms.getWidth();
    }

    @Override
    public World getWorld() {
        return (World) ((ExtraWorld)nms.world).getCraftWorld();
    }

    @Override
    public boolean hasGravity() {
        return !nms.hasNoGravity();
    }

    @Override
    public boolean isCustomNameVisible() {
        return nms.isCustomNameVisible();
    }

    @Override
    public boolean isDead() {
        return !nms.isAlive();
    }

    @Override
    public boolean isEmpty() {
        return !nms.hasPassengers();
    }

    @Override
    public boolean isGlowing() {
        return nms.isGlowing();
    }

    @Override
    public boolean isInsideVehicle() {
        return nms.hasVehicle();
    }

    @Override
    public boolean isInvulnerable() {
        return nms.isInvulnerable();
    }

    @Override
    public boolean isOnGround() {
        if (nms instanceof ProjectileEntity)
            return ((ProjectileEntity) nms).isOnGround();

        return nms.isOnGround();
    }

    @Override
    public boolean isPersistent() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isSilent() {
        return nms.isSilent();
    }

    @Override
    public boolean isValid() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean leaveVehicle() {
        if (!isInsideVehicle()) {
            return false;
        }

        nms.stopRiding();
        return true;
    }

    @Override
    public void playEffect(EntityEffect arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void remove() {
        nms.remove();
    }

    @Override
    public boolean removePassenger(Entity arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean removeScoreboardTag(String arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setCustomNameVisible(boolean arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setFallDistance(float arg0) {
        nms.fallDistance = arg0;
    }

    @Override
    public void setFireTicks(int arg0) {
        nms.setFireTicks(arg0);
    }

    @Override
    public void setGlowing(boolean arg0) {
        nms.setGlowing(arg0);
    }

    @Override
    public void setGravity(boolean arg0) {
        nms.setNoGravity(!arg0);
    }

    @Override
    public void setInvulnerable(boolean arg0) {
        nms.setInvulnerable(arg0);
    }

    @Override
    public void setLastDamageCause( EntityDamageEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean setPassenger(Entity arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setPersistent(boolean arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setPortalCooldown(int arg0) {
        nms.netherPortalCooldown = arg0;
    }

    @Override
    public void setRotation(float arg0, float arg1) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setSilent(boolean arg0) {
        nms.setSilent(arg0);
    }

    @Override
    public void setTicksLived(int arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setVelocity(Vector arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean teleport(Location arg0) {
        return teleport(arg0, TeleportCause.PLUGIN);
    }

    @Override
    public boolean teleport(Entity arg0) {
        return teleport(arg0, TeleportCause.PLUGIN);
    }

    @Override
    public boolean teleport(Location location, TeleportCause arg1) {
        location.checkFinite();

        if (nms.hasPassengers() || nms.removed) {
            return false;
        }

        nms.stopRiding();

        // TODO: Cross world teleporting
        //if (!location.getWorld().equals(getWorld())) {
        //    nms.teleportTo(((CraftWorld) location.getWorld()).getHandle(), new BlockPos(location.getX(), location.getY(), location.getZ()));
        //    return true;
        //}

        nms.updatePositionAndAngles(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        nms.setHeadYaw(location.getYaw());

        return true;
    }

    @Override
    public boolean teleport(Entity arg0, TeleportCause arg1) {
        return teleport(arg0.getLocation(), arg1);
    }

    private static PermissibleBase getPermissibleBase() {
        if (perm == null) {
            perm = new PermissibleBase(new ServerOperator() {

                @Override
                public boolean isOp() {
                    return false;
                }

                @Override
                public void setOp(boolean value) {
                }
            });
        }
        return perm;
    }

    @Override
    public CommandSender getBukkitSender(ServerCommandSource serverCommandSource) {
        return this;
    }
}