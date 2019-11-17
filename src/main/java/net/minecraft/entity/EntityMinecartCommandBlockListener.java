package net.minecraft.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
// CraftBukkit - package-private -> public
public class EntityMinecartCommandBlockListener extends CommandBlockLogic
{
    final EntityMinecartCommandBlock field_145768_a;

    EntityMinecartCommandBlockListener(EntityMinecartCommandBlock p_i45320_1_)
    {
        this.field_145768_a = p_i45320_1_;
        this.sender = (org.bukkit.craftbukkit.entity.CraftMinecartCommand) p_i45320_1_.getBukkitEntity(); // CraftBukkit - Set the sender
    }

    public void func_145756_e()
    {
        this.field_145768_a.getDataWatcher().updateObject(23, this.func_145753_i());
        this.field_145768_a.getDataWatcher().updateObject(24, IChatComponent.Serializer.func_150696_a(this.func_145749_h()));
    }

    /**
     * Return the position for this command sender.
     */
    public ChunkCoordinates getPlayerCoordinates()
    {
        return new ChunkCoordinates(MathHelper.floor_double(this.field_145768_a.posX), MathHelper.floor_double(this.field_145768_a.posY + 0.5D), MathHelper.floor_double(this.field_145768_a.posZ));
    }

    public World getEntityWorld()
    {
        return this.field_145768_a.worldObj;
    }

    @SideOnly(Side.CLIENT)
    public int func_145751_f()
    {
        return 1;
    }
    @SideOnly(Side.CLIENT)
    public void func_145757_a(ByteBuf p_145757_1_)
    {
        p_145757_1_.writeInt(field_145768_a.getEntityId());
    }
}